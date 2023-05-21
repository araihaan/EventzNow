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

import java.util.Locale;

public class MemberEventBuyActivity extends AppCompatActivity {

    private TextView eventName, eventDate, eventTime, eventLocation, eventSlot, eventPrice, eventPriceDetail, amountString, totalpaymentString;
    private Button paymentMethod, addAmount, removeAmount;
    private String eventID;
    private int amount = 1;
    private DatabaseReference eventsRef;

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
        amountString = findViewById(R.id.txtAmountString);
        totalpaymentString = findViewById(R.id.txtTotalpaymentDetailString);
        addAmount = findViewById(R.id.btAddAmount);
        removeAmount = findViewById(R.id.btRemoveAmount);

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

                        updateTotalPayment();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors here
            }
        });

        addAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount++;
                updateAmountString();
                updateTotalPayment();
            }
        });

        removeAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount > 1) {
                    amount--;
                    updateAmountString();
                    updateTotalPayment();
                }
            }
        });
    }

    private void updateAmountString() {
        amountString.setText(String.valueOf(amount));
    }

    private void updateTotalPayment() {
        String priceString = eventPrice.getText().toString();
        String numericPriceString = priceString.replaceAll("[^\\d.]", ""); // Remove non-numeric characters

        if (!numericPriceString.isEmpty()) {
            double price = Double.parseDouble(numericPriceString);
            double totalPayment = price * amount;
            String formattedTotalPayment = formatTotalPayment(totalPayment); // Format the total payment
            totalpaymentString.setText(formattedTotalPayment);
        }
    }

    private String formatTotalPayment(double totalPayment) {
        // Add comma separator for thousands
        String formattedTotalPayment = String.format(Locale.US, "%,.0f", totalPayment);
        return formattedTotalPayment;
    }
}
