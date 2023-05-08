package com.example.eventznow;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AdminCreateEventActivity extends AppCompatActivity {
    private Button BtnDatePicker;
    private Button BtnTimePicker;
    private Calendar calendar;
    private int year, month, dayOfMonth;
    private Button BtnEventSlot, BtnEventPrice;
    private Button BtnCreateButton;
    private EditText TextEventName, TextEventLocation;
    private TextView TextEventDate, TextEventTime;

    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_event);

        TextEventName = findViewById(R.id.eventName);
        TextEventLocation = findViewById(R.id.eventLocation);
        TextEventDate = findViewById(R.id.eventDate);
        TextEventTime = findViewById(R.id.eventTime);
        BtnDatePicker = findViewById(R.id.buttonDatePicker);
        BtnTimePicker = findViewById(R.id.buttonTimePicker);
        BtnEventPrice = findViewById(R.id.btn_price);
        BtnEventSlot = findViewById(R.id.btn_memberpicker);
        BtnCreateButton = findViewById(R.id.btCreateEvent);

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminCreateEventActivity.this,
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

        BtnCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("events");
                if (FirebaseDatabase.getInstance().getReference() == null) {
                    Toast.makeText(AdminCreateEventActivity.this, "Failed to connect to database", Toast.LENGTH_SHORT).show();
                    return;
                }
                String eventname = TextEventName.getText().toString();
                String date = TextEventDate.getText().toString();
                String time = TextEventTime.getText().toString();
                String location = TextEventLocation.getText().toString();
                String slot = BtnEventSlot.getText().toString();
                String price = BtnEventPrice.getText().toString();
                HelperClassEvents HelperClassEvents = new HelperClassEvents(eventname, date, time, location, slot, price);
                reference.child(eventname).setValue(HelperClassEvents);
                Toast.makeText(AdminCreateEventActivity.this, "You have create event successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminCreateEventActivity.this, AdminHomeFragment.class);
                startActivity(intent);
                finish();
            }
        });

        // Make the text view read-only
        TextEventDate.setKeyListener(null);
        TextEventTime.setKeyListener(null);
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
