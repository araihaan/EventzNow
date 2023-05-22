package com.example.eventznow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AdminEditEventActivity extends AppCompatActivity {
    private Button BtnDatePicker;
    private Button BtnTimePicker;
    private Calendar calendar;
    private int year, month, dayOfMonth;
    private Button BtnEventSlot, BtnEventPrice;
    private Button BtnUpdateButton;
    private EditText TextEventName, TextEventLocation;
    private TextView TextEventDate, TextEventTime;
    private String joinedUsers;

    FirebaseDatabase database;
    DatabaseReference reference;
    String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_event);

        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");

        // Retrieve event details from Firebase
        retrieveEventDetails(eventID);

        TextEventName = findViewById(R.id.eventName);
        TextEventLocation = findViewById(R.id.eventLocation);
        TextEventDate = findViewById(R.id.eventDate);
        TextEventTime = findViewById(R.id.eventTime);
        BtnDatePicker = findViewById(R.id.buttonDatePicker);
        BtnTimePicker = findViewById(R.id.buttonTimePicker);
        BtnEventPrice = findViewById(R.id.btn_price);
        BtnEventSlot = findViewById(R.id.btn_memberpicker);
        BtnUpdateButton = findViewById(R.id.btUpdateEvent);

        FirebaseDatabase database;
        DatabaseReference eventsRef;

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Set the initial value of the text view
        String initialDate = dayOfMonth + "/" + (month + 1) + "/" + year;
        TextEventDate.setText(initialDate);

        BtnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminEditEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Update the text view with the selected date
                                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                TextEventDate.setText(selectedDate);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        BtnEventPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Show price picker dialog or set price to 0
                showPricePickerDialog();
            }
        });

        BtnEventSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Show price picker dialog or set price to 0
                showMemberPickerDialog();
            }
        });

        BtnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        BtnUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEvent();
            }
        });

        // Make the text view read-only
        TextEventDate.setKeyListener(null);
        TextEventTime.setKeyListener(null);
    }

    private void retrieveEventDetails(String eventID) {
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("events").child(eventID);
        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HelperClassEvents event = dataSnapshot.getValue(HelperClassEvents.class);
                    if (event != null) {
                        TextEventName.setText(event.getEventname());
                        TextEventLocation.setText(event.getLocation());
                        TextEventDate.setText(event.getDate());
                        TextEventTime.setText(event.getTime());
                        BtnEventSlot.setText(event.getSlot());
                        BtnEventPrice.setText(event.getPrice());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminEditEventActivity.this, "Failed to retrieve event details: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEvent() {
        String eventname = TextEventName.getText().toString();
        String date = TextEventDate.getText().toString();
        String time = TextEventTime.getText().toString();
        String location = TextEventLocation.getText().toString();
        String slot = BtnEventSlot.getText().toString();
        String price = BtnEventPrice.getText().toString();
        String creator = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if (eventname.isEmpty() || date.isEmpty() || time.isEmpty() || location.isEmpty() || slot.isEmpty() || price.isEmpty()) {
            Toast.makeText(AdminEditEventActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("events").child(eventID);

        // Retrieve the current joinedUsersList from the database
        reference.child("joinedUsersList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> joinedUsersList = new ArrayList<>();

                    // Add the current user's ID to the joinedUsersList
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    HelperClassEvents updatedEvent = new HelperClassEvents(eventID, userId, eventname, date, time, location, slot, price, joinedUsersList, creator);

                    reference.setValue(updatedEvent)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AdminEditEventActivity.this, "Event updated successfully!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AdminEditEventActivity.this, "Failed to update event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminEditEventActivity.this, "Failed to retrieve joined users: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Update the event name in the "orders" node
        DatabaseReference ordersRef = database.getReference("orders");
        Query query = ordersRef.orderByChild("eventID").equalTo(eventID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String orderID = snapshot.getKey();
                    DatabaseReference orderRef = ordersRef.child(orderID);
                    orderRef.child("eventname").setValue(eventname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }



    private void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Update the TextView with the selected time
                        String time = String.format("%02d:%02d", hourOfDay, minute);
                        TextEventTime.setText(time);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void showPricePickerDialog() {
        final NumberPicker pricePicker = new NumberPicker(this);
        pricePicker.setMinValue(0);
        pricePicker.setMaxValue(1000000);
        pricePicker.setWrapSelectorWheel(false);
        pricePicker.setValue(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Select Price")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int selectedPrice = pricePicker.getValue() * 1000;
                        BtnEventPrice.setText(selectedPrice == 0 ? "Free" : String.format("Rp %,d", selectedPrice));
                    }
                })
                .setNegativeButton("Cancel", null)
                .setView(pricePicker);

        builder.show();
    }

    private void showMemberPickerDialog() {
        final NumberPicker memberPicker = new NumberPicker(this);
        memberPicker.setMinValue(0);
        memberPicker.setMaxValue(1000);
        memberPicker.setWrapSelectorWheel(true);
        memberPicker.setValue(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Select Slot")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int selectedSlot = memberPicker.getValue();
                        BtnEventSlot.setText(selectedSlot == 0 ? "No Limit" : String.format("%,d", selectedSlot));
                    }
                })
                .setNegativeButton("Cancel", null)
                .setView(memberPicker);

        builder.show();
    }
}
