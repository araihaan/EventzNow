package com.example.eventznow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataEventMemberActivity extends AppCompatActivity {

    private TextView eventName, eventDate, eventTime, eventLocation, eventSlot, eventPrice, orderStatus;
    private DatabaseReference ordersRef, eventsRef;
    private FirebaseDatabase database;
    private Button btCheck;
    private String orderID, eventID, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_event_member);

        orderStatus = findViewById(R.id.txtStatusString);
        eventName = findViewById(R.id.txtEventName);
        eventDate = findViewById(R.id.txtDateString);
        eventTime = findViewById(R.id.txtTimeString);
        eventLocation = findViewById(R.id.txtLocationString);
        eventSlot = findViewById(R.id.txtSlotString);
        eventPrice = findViewById(R.id.txtPriceString);
        btCheck = findViewById(R.id.btnCheck);

        Intent intent = getIntent();
        orderID = intent.getStringExtra("orderID");

        database = FirebaseDatabase.getInstance();

        if (orderID != null) {
            ordersRef = database.getReference("orders").child(orderID);

            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        HelperEventOrder order = dataSnapshot.getValue(HelperEventOrder.class);
                        if (order != null) {
                            eventID = order.getEventID();
                            orderStatus.setText(order.getStatus());
                            eventName.setText(order.getEventname());

                            if (eventID != null) {
                                eventsRef = database.getReference("events").child(eventID);
                                eventsRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            HelperClassEvents event = dataSnapshot.getValue(HelperClassEvents.class);
                                            if (event != null) {
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
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle potential errors here
                }
            });
        }

        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DataEventMemberActivity.this, MemberJoinedEventActivity.class);
                intent.putExtra("orderID", orderID);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
