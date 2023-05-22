package com.example.eventznow;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<HelperClassEvents> eventsList;
    private Context context;
    private String userId;
    private DatabaseReference usersRef;

    public EventsAdapter(Context context, FirebaseAuth firebaseAuth) {
        this.context = context;
        this.userId = firebaseAuth.getCurrentUser().getUid();
        this.usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
    }

    public void setEventsList(List<HelperClassEvents> eventsList) {
        this.eventsList = eventsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        HelperClassEvents event = eventsList.get(position);

        holder.eventName.setText(event.getEventname());
        holder.eventDate.setText(event.getDate());
        holder.eventTime.setText(event.getTime());
        holder.eventLocation.setText(event.getLocation());
        holder.eventSlot.setText(event.getSlot());
        holder.eventPrice.setText(event.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the visibility of eventItemAdmin LinearLayout
                if (holder.eventItemAdmin.getVisibility() == View.GONE) {
                    holder.eventItemAdmin.setVisibility(View.VISIBLE);
                } else {
                    holder.eventItemAdmin.setVisibility(View.GONE);
                }
            }
        });

        holder.btDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click here
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Perform the deletion of the event at the given position
                    deleteEvent(position);
                }
            }
        });

        holder.btEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit button click here
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Get the event ID of the event to be edited
                    String eventID = eventsList.get(position).getEventID();

                    // Start the EditEventActivity and pass the event ID
                    Intent intent = new Intent(context, AdminEditEventActivity.class);
                    intent.putExtra("eventID", eventID);
                    context.startActivity(intent);
                }
            }
        });
    }

    // Method to delete an event at the given position
    private void deleteEvent(int position) {
        // Get the event ID of the event to be deleted
        String eventID = eventsList.get(position).getEventID();

        // Create a database reference to the "events" node
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference().child("events");

        // Create a database reference to the "orders" node
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("orders");

        // Delete the event from the database
        eventsRef.child(eventID).removeValue();

        // Delete the corresponding order from the database
        ordersRef.orderByChild("eventID").equalTo(eventID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    String orderID = orderSnapshot.getKey();
                    ordersRef.child(orderID).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });

        // Notify the adapter to update the view
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, eventsList.size());
    }


    @Override
    public int getItemCount() {
        return eventsList != null ? eventsList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView eventName;
        public TextView eventDate;
        public TextView eventTime;
        public TextView eventLocation;
        public TextView eventSlot;
        public TextView eventPrice;
        public View eventItemAdmin;
        public Button btDeleteEvent;
        public Button btEditEvent;

        public ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventTime = itemView.findViewById(R.id.eventTime);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventSlot = itemView.findViewById(R.id.btn_memberpicker);
            eventPrice = itemView.findViewById(R.id.btn_price);
            eventItemAdmin = itemView.findViewById(R.id.eventItemAdmin);
            btDeleteEvent = itemView.findViewById(R.id.btDeleteevent);
            btEditEvent = itemView.findViewById(R.id.btEditevent);
        }

    }
}
