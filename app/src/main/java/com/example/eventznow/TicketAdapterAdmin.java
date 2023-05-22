package com.example.eventznow;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TicketAdapterAdmin extends RecyclerView.Adapter<TicketAdapterAdmin.ViewHolder> {

    private List<HelperClassEvents> eventsList;
    private Context context;
    private String userId;

    public TicketAdapterAdmin(Context context, String userId) {
        this.context = context;
        this.userId = userId;
    }

    public void setEventsList(List<HelperClassEvents> eventsList) {
        this.eventsList = eventsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_item_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final HelperClassEvents event = eventsList.get(position);
        holder.eventName.setText(event.getEventname());
        holder.eventCreator.setText(event.getCreator());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperClassEvents event = eventsList.get(holder.getAdapterPosition());
                String eventID = event.getEventID();

                if (eventID != null) {
                    // Create an intent to start the DataEventAdminActivity
                    Intent intent = new Intent(context, DataEventAdminActivity.class);
                    intent.putExtra("eventID", eventID);
                    context.startActivity(intent);
                } else {
                    // Handle the case when eventID is null
                    // Show an error message or perform any other desired action
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsList != null ? eventsList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName;
        public TextView eventCreator;

        public ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventCreator = itemView.findViewById(R.id.creatoremail);
        }
    }
}

