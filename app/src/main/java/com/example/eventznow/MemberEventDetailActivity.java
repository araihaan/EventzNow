package com.example.eventznow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemberEventDetailActivity extends AppCompatActivity {

    private TextView eventName, eventDate, eventTime, eventLocation, eventSlot, eventPrice;
    private Button joinEvent;
    private String eventID;
    private DatabaseReference eventsRef;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_event_detail);

        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");

        eventName = findViewById(R.id.txtEventName);
        eventDate = findViewById(R.id.txtDateString);
        eventTime = findViewById(R.id.txtTimeString);
        eventLocation = findViewById(R.id.txtLocationString);
        eventSlot = findViewById(R.id.txtSlotString);
        eventPrice = findViewById(R.id.txtPriceString);

        eventsRef = FirebaseDatabase.getInstance().getReference().child("events").child(eventID);

        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HelperClassEvents event = dataSnapshot.getValue(HelperClassEvents.class);

                    if (event != null) {
                        eventName.setText(event.getEventname());
                        eventDate.setText(event.getDate());
                        eventTime.setText(event.getTime());
                        eventLocation.setText(event.getLocation());
                        eventSlot.setText(event.getSlot());
                        eventPrice.setText(event.getPrice());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors here
            }
        });

        Button joinEventButton = findViewById(R.id.btJoinEvent);
        joinEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String price = eventPrice.getText().toString();
                DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("events").child(eventID);
                DatabaseReference eventsOrder = FirebaseDatabase.getInstance().getReference("orders");
                eventRef.child("joinedUsersList").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<String> joinedUsersList = new ArrayList<>();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String userID = snapshot.getValue(String.class);
                                if (!joinedUsersList.contains(userID)) {
                                    joinedUsersList.add(userID);
                                }
                            }

                            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            if (joinedUsersList.contains(userID)) {
                                Toast.makeText(MemberEventDetailActivity.this, "You already joined this event", Toast.LENGTH_SHORT).show();
                            } else {
                                if (price.equalsIgnoreCase("Free")) {
                                    joinedUsersList.add(userID);
                                    eventRef.child("joinedUsersList").setValue(joinedUsersList).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            String orderID = generateOrderID();
                                            String eventname = eventName.getText().toString();
                                            HelperEventOrder helperEventOrder = new HelperEventOrder(eventID, orderID, eventname, "-", "1", "Free");
                                            eventsOrder.child(orderID).setValue(helperEventOrder);
                                            Intent intent = new Intent(MemberEventDetailActivity.this, MemberJoinedEventActivity.class);
                                            intent.putExtra("orderID", orderID);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MemberEventDetailActivity.this, "Failed to join event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Intent intent = new Intent(MemberEventDetailActivity.this, MemberEventBuyActivity.class);
                                    intent.putExtra("eventID", eventID);
                                    startActivity(intent);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MemberEventDetailActivity.this, "Failed to retrieve joined users: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private String generateOrderID() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uniqueID = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        return timestamp + uniqueID;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MemberEventDetailActivity.this, MemberMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
