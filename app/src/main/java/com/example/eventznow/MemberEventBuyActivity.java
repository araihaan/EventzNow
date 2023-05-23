package com.example.eventznow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemberEventBuyActivity extends AppCompatActivity {

    private TextView eventName, eventDate, eventTime, eventLocation, eventSlot, eventPrice, eventPriceDetail;
    private Button paymentMethod;
    private String eventID;
    private DatabaseReference eventsRef;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_event_buy);

        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");

        eventName = findViewById(R.id.txtEventName);
        eventDate = findViewById(R.id.txtDateString);
        eventTime = findViewById(R.id.txtTimeString);
        eventLocation = findViewById(R.id.txtLocationString);
        eventSlot = findViewById(R.id.txtSlotString);
        eventPrice = findViewById(R.id.txtPriceString);
        eventPriceDetail = findViewById(R.id.txtPriceDetailString);
        paymentMethod = findViewById(R.id.btPaymentMethod);

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
                        eventPriceDetail.setText(event.getPrice());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors here
            }
        });

        paymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("events").child(eventID);
                DatabaseReference eventsOrder = FirebaseDatabase.getInstance().getReference("orders");
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            HelperClassEvents event = dataSnapshot.getValue(HelperClassEvents.class);
                            List<String> joinedUsersList = event.getJoinedUsersList();
                            if (joinedUsersList == null) {
                                joinedUsersList = new ArrayList<>();
                            }
                            if (!joinedUsersList.contains(userID)) {
                                joinedUsersList.add(userID);
                            }
                            eventRef.child("joinedUsersList").setValue(joinedUsersList);
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            String orderID = generateOrderID();
                            String eventname = eventName.getText().toString();
                            String price = eventPrice.getText().toString();
                            HelperEventOrder helperEventOrder = new HelperEventOrder(userId, eventID, orderID, eventname, "under maintenance", "1", price, "Registered");
                            eventsOrder.child(orderID).setValue(helperEventOrder);
                            Intent intent = new Intent(MemberEventBuyActivity.this, MemberJoinedEventActivity.class);
                            intent.putExtra("orderID", orderID);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle potential errors here
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
}
