package com.example.eventznow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MembersJoinedAdapter extends RecyclerView.Adapter<MembersJoinedAdapter.ViewHolder> {

    private List<User> userList;
    private Context context;

    public MembersJoinedAdapter(Context context) {
        this.context = context;
        this.userList = new ArrayList<>();
    }

    public void addUser(User user) {
        userList.add(user);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final User user = userList.get(position);

        holder.memberName.setText(user.getUsername());
        holder.memberEmail.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView memberName;
        public TextView memberEmail;

        public ViewHolder(View itemView) {
            super(itemView);
            memberName = itemView.findViewById(R.id.memberName);
            memberEmail = itemView.findViewById(R.id.memberEmail);
        }
    }
}
