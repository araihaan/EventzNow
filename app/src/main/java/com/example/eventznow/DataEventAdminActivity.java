package com.example.eventznow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DataEventAdminActivity extends AppCompatActivity {

    private TextView eventName, eventCreator, eventMemberJoin;
    private RecyclerView recyclerView;
    private MembersJoinedAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference eventRef;
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_event_admin);

        eventName = findViewById(R.id.txtEventName);
        eventCreator = findViewById(R.id.txtCreatorEmail);
        eventMemberJoin = findViewById(R.id.txtMembersString);

        recyclerView = findViewById(R.id.reMemberJoined);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MembersJoinedAdapter(this);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");

        database = FirebaseDatabase.getInstance();
        eventRef = database.getReference("events").child(eventID);

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HelperClassEvents event = dataSnapshot.getValue(HelperClassEvents.class);
                    if (event != null) {
                        eventName.setText(event.getEventname());
                        String creator = event.getCreator();
                        eventCreator.setText(creator);

                        List<String> joinedUsersList = event.getJoinedUsersList();
                        if (joinedUsersList != null) {
                            getUsers(joinedUsersList);
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

    private void getUsers(List<String> userIds) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        for (String userId : userIds) {
            DatabaseReference userRef = usersRef.child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            adapter.addUser(user);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle potential errors here
                }
            });
        }
    }
}
