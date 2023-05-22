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

public class MemberJoinedEventActivity extends AppCompatActivity {

    private TextView orderEventId, orderId, orderEventname, orderPayment, orderAmount, orderTotalpay;
    private DatabaseReference ordersRef;
    private FirebaseDatabase database;
    private Button btCheck;
    private String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_joined_event);

        orderEventId = findViewById(R.id.txtEventIdString);
        orderId = findViewById(R.id.txtOrderIdString);
        orderEventname = findViewById(R.id.txtEventName);
        orderPayment = findViewById(R.id.txtPaymentString);
        orderAmount = findViewById(R.id.txtAmountString);
        orderTotalpay = findViewById(R.id.txtTotalpayString);
        btCheck = findViewById(R.id.btCheck);

        Intent intent = getIntent();
        orderID = intent.getStringExtra("orderID");

        database = FirebaseDatabase.getInstance();
        ordersRef = database.getReference("orders").child(orderID);

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HelperEventOrder order = dataSnapshot.getValue(HelperEventOrder.class);

                    if (order != null) {
                        orderEventId.setText(order.getEventID());
                        orderId.setText(order.getOrderID());
                        orderEventname.setText(order.getEventname());
                        orderPayment.setText(order.getPayment());
                        orderAmount.setText(order.getAmount());
                        orderTotalpay.setText(order.getTotalpay());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors here
            }
        });

        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemberJoinedEventActivity.this, DataEventMemberActivity.class);
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
