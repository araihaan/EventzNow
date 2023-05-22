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

public class AdminTicketFragment extends Fragment {

    private DatabaseReference ticketsRef;
    private RecyclerView recyclerView;
    private TicketAdapterAdmin adapter;
    private String userId;

    public AdminTicketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_ticket, container, false);

        recyclerView = view.findViewById(R.id.reTicketListAdmin);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TicketAdapterAdmin(getContext(), userId);
        recyclerView.setAdapter(adapter);

        ticketsRef = FirebaseDatabase.getInstance().getReference().child("events");

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            userId = firebaseAuth.getCurrentUser().getUid();
        }

        ticketsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HelperClassEvents> eventsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String eventID = snapshot.child("eventID").getValue(String.class);
                    String userIds = snapshot.child("userId").getValue(String.class);
                    String eventname = snapshot.child("eventname").getValue(String.class);
                    String date = snapshot.child("date").getValue(String.class);
                    String time = snapshot.child("time").getValue(String.class);
                    String location = snapshot.child("location").getValue(String.class);
                    String slot = snapshot.child("slot").getValue(String.class);
                    String price = snapshot.child("price").getValue(String.class);
                    String creator = snapshot.child("creator").getValue(String.class);

                    List<String> joinedUsersList = new ArrayList<>();
                    for (DataSnapshot userSnapshot : snapshot.child("joinedUsersList").getChildren()) {
                        String userID = userSnapshot.getValue(String.class);
                        joinedUsersList.add(userID);
                    }

                    if (userIds != null && userIds.equals(userId)) {
                    HelperClassEvents event = new HelperClassEvents(eventID, userId, eventname, date, time, location, slot, price, joinedUsersList, creator);
                    eventsList.add(event);
                    }
                }
                adapter.setEventsList(eventsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });

        return view;
    }
}
