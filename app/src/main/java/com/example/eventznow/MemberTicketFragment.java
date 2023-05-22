package com.example.eventznow;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MemberTicketFragment extends Fragment {

    private DatabaseReference ticketsRef;
    private RecyclerView recyclerView;
    private TicketAdapterMember adapter;
    private String userId;

    public MemberTicketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_ticket, container, false);

        recyclerView = view.findViewById(R.id.reTicketListMember);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TicketAdapterMember(getContext(), userId);
        recyclerView.setAdapter(adapter);

        ticketsRef = FirebaseDatabase.getInstance().getReference().child("orders");

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            userId = firebaseAuth.getCurrentUser().getUid();
        }

        ticketsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HelperEventOrder> ordersList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String eventID = snapshot.child("eventID").getValue(String.class);
                    String orderID = snapshot.child("orderID").getValue(String.class);
                    String eventname = snapshot.child("eventname").getValue(String.class);
                    String payment = snapshot.child("payment").getValue(String.class);
                    String amount = snapshot.child("amount").getValue(String.class);
                    String totalpay = snapshot.child("totalpay").getValue(String.class);
                    String status = snapshot.child("status").getValue(String.class);
                    String ticketUserId = snapshot.child("userId").getValue(String.class);

                    if (ticketUserId != null && ticketUserId.equals(userId)) {
                        HelperEventOrder order = new HelperEventOrder(userId, eventID, orderID, eventname, payment, amount, totalpay, status);
                        ordersList.add(order);
                    }
                }
                adapter.setOrdersList(ordersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });

        return view;
    }
}