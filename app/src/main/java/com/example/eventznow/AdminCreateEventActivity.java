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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AdminCreateEventActivity extends AppCompatActivity {
    private Button btnDatePicker;
    private Button btnTimePicker;
    private Calendar calendar;
    private int year, month, dayOfMonth;
    private Button btnEventSlot, btnEventPrice;
    private Button btnCreateButton;
    private EditText textEventName, textEventLocation;
    private TextView textEventDate, textEventTime;
    private String eventID;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_event);

        textEventName = findViewById(R.id.eventName);
        textEventLocation = findViewById(R.id.eventLocation);
        textEventDate = findViewById(R.id.eventDate);
        textEventTime = findViewById(R.id.eventTime);
        btnDatePicker = findViewById(R.id.buttonDatePicker);
        btnTimePicker = findViewById(R.id.buttonTimePicker);
        btnEventPrice = findViewById(R.id.btn_price);
        btnEventSlot = findViewById(R.id.btn_memberpicker);
        btnCreateButton = findViewById(R.id.btCreateEvent);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Set the initial value of the text view
        String initialDate = dayOfMonth + "/" + (month + 1) + "/" + year;
        textEventDate.setText(initialDate);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminCreateEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Update the text view with the selected date
                                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                textEventDate.setText(selectedDate);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        btnEventPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show price picker dialog or set price to 0
                showPricePickerDialog();
            }
        });

        btnEventSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show member picker dialog or set slot to 0
                showMemberPickerDialog();
            }
        });

        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        btnCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("events");
                if (FirebaseDatabase.getInstance().getReference() == null) {
                    Toast.makeText(AdminCreateEventActivity.this, "Failed to connect to database", Toast.LENGTH_SHORT).show();
                    return;
                }
                eventID = generateEventID();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String eventname = textEventName.getText().toString();
                String date = textEventDate.getText().toString();
                String time = textEventTime.getText().toString();
                String location = textEventLocation.getText().toString();
                String slot = btnEventSlot.getText().toString();
                String price = btnEventPrice.getText().toString();
                String creator = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                // Create the event object with an empty list of joinedUsers
                List<String> joinedUsersList = new ArrayList<>();

                HelperClassEvents helperClassEvents = new HelperClassEvents(eventID, userId, eventname, date, time, location, slot, price, joinedUsersList, creator);

                // Add the event to the database
                reference.child(eventID).setValue(helperClassEvents, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            // Update the joinedUsersList in the database
                            DatabaseReference joinedUsersRef = ref.child("joinedUsersList");
                            joinedUsersRef.setValue(joinedUsersList)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(AdminCreateEventActivity.this, "You have created the event successfully!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(AdminCreateEventActivity.this, AdminMenuActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(AdminCreateEventActivity.this, "Failed to create the event", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(AdminCreateEventActivity.this, "Failed to create the event", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



        // Make the text view read-only
        textEventDate.setKeyListener(null);
        textEventTime.setKeyListener(null);
    }

    private String generateEventID() {
        return UUID.randomUUID().toString();
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
                        textEventTime.setText(time);
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
                        btnEventPrice.setText(selectedPrice == 0 ? "Free" : String.format("Rp %,d", selectedPrice));
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
                        btnEventSlot.setText(selectedSlot == 0 ? "No Limit" : String.format("%,d", selectedSlot));
                    }
                })
                .setNegativeButton("Cancel", null)
                .setView(memberPicker);

        builder.show();
    }
}