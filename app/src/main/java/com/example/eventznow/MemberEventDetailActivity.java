package com.example.eventznow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MemberEventDetailActivity extends AppCompatActivity {

    private TextView eventName, eventDate, eventTime, eventLocation, eventSlot, eventPrice;
    private DatabaseReference eventsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_event_detail);

        Intent intent = getIntent();
        String eventID = intent.getStringExtra("eventID");

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
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MemberEventDetailActivity.this, MemberMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
