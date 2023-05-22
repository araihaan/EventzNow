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

public class TicketAdapterMember extends RecyclerView.Adapter<TicketAdapterMember.ViewHolder> {

    private List<HelperEventOrder> ordersList;
    private Context context;
    private String userId;

    public TicketAdapterMember(Context context, String userId) {
        this.context = context;
        this.userId = userId;
    }

    public void setOrdersList(List<HelperEventOrder> ordersList) {
        this.ordersList = ordersList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final HelperEventOrder order = ordersList.get(position);

        holder.eventName.setText(order.getEventname());
        holder.eventStatus.setText(order.getStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperEventOrder order = ordersList.get(holder.getAdapterPosition());
                String orderID = order.getOrderID();

                if (orderID != null) {
                    // Create an intent to start the DataEventMemberActivity
                    Intent intent = new Intent(context, DataEventMemberActivity.class);
                    intent.putExtra("orderID", orderID);
                    context.startActivity(intent);
                } else {
                    // Handle the case when orderID is null
                    // Show an error message or perform any other desired action
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return ordersList != null ? ordersList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName;
        public TextView eventStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventStatus = itemView.findViewById(R.id.eventstatus);
        }
    }
}
