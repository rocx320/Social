package com.example.social.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<ChatItem> chatItemList;

    public ChatAdapter(List<ChatItem> chatItemList) {
        this.chatItemList = chatItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_msg, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatItem chatItem = chatItemList.get(position);
        holder.profileImageView.setImageResource(chatItem.getProfileImageRes());
        holder.profileNameTextView.setText(chatItem.getProfileName());
    }

    @Override
    public int getItemCount() {
        return chatItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView profileNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImage_chatMenu);
            profileNameTextView = itemView.findViewById(R.id.showUsername_chatMenu);
        }
    }
}
