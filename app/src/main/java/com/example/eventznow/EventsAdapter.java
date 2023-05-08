package com.example.eventznow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<HelperClassEvents> eventsList;
    private Context context;

    public EventsAdapter(Context context) {
        this.context = context;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HelperClassEvents event = eventsList.get(position);

        holder.eventName.setText(event.getEventname());
        holder.eventDate.setText(event.getDate());
        holder.eventTime.setText(event.getTime());
        holder.eventLocation.setText(event.getLocation());
        holder.eventSlot.setText(event.getSlot());
        holder.eventPrice.setText(event.getPrice());
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

        public ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventTime = itemView.findViewById(R.id.eventTime);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventSlot = itemView.findViewById(R.id.btn_memberpicker);
            eventPrice = itemView.findViewById(R.id.btn_price);
        }
    }
}
