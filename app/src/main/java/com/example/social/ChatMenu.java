package com.example.social;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatMenu extends Activity {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatItem> chatItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_menu);

        recyclerView = findViewById(R.id.recyclerView);
        chatItemList = new ArrayList<>();

        // Populate chatItemList with actual data
        // For example:
//        chatItemList.add(new ChatItem(R.drawable.profile1, "John Doe"));
//        chatItemList.add(new ChatItem(R.drawable.profile2, "Jane Smith"));
        // Add more items as needed

        chatAdapter = new ChatAdapter(chatItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);
    }

}
