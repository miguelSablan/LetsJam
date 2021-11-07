package com.example.letsjam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    DatabaseReference reference;
    EditText etItem;
    RecyclerView rvItems;

    List<String> items;

    Button btnCreate;
    ChatAdapter adapter;

    Button btnLogOut;
    FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().getRoot();

        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);
        btnCreate = findViewById(R.id.btnCreate);

        items = new ArrayList<>();

        // Storing/Loading items in list
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> mySet = new HashSet<String>();
                Iterator i = snapshot.getChildren().iterator();
                while (i.hasNext()) {
                    mySet.add(((DataSnapshot)i.next()).getKey());
                }
                items.clear();
                items.addAll(mySet);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "There was a network issue.", Toast.LENGTH_SHORT).show();
            }
        });

        // Tapping on a room
        ChatAdapter.onClickListener onClickListener = new ChatAdapter.onClickListener() {
            @Override
            public void onItemClicked(int position) {
                // The name of the room tapped
                String roomName = items.get(position);
                // create new activity
                Intent i = new Intent(MainActivity.this, ChatActivity.class);
                // pass room data
                i.putExtra("ChatRoom", roomName);
                // display activity
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Joining: " + roomName, Toast.LENGTH_SHORT).show();
            }
        };

        adapter = new ChatAdapter(items, onClickListener);
        rvItems.setAdapter(adapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // Tapping the Create Room Button
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // The Chat Room Name
                String chatRoom = etItem.getText().toString();
                // Add room to the list
                items.add(chatRoom);
                // Notify adapter
                adapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(chatRoom, "ChatRoom");
                reference.updateChildren(hashMap);

                Toast.makeText(getApplicationContext(), "Room created: " + chatRoom, Toast.LENGTH_SHORT).show();

                //reference.child(chatRoom).removeValue();
                //Toast.makeText(getApplicationContext(), "Removed: " + chatRoom, Toast.LENGTH_SHORT).show();
            }
        });

        btnLogOut = findViewById(R.id.btnLogout);

        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
    }
}