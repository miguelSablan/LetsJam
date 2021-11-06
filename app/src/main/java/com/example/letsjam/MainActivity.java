package com.example.letsjam;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    //DatabaseReference reference;
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
        //reference = FirebaseDatabase.getInstance().getReference().getRoot();

        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);
        btnCreate = findViewById(R.id.btnCreate);

        items = new ArrayList<>();

        ChatAdapter.onClickListener onClickListener = new ChatAdapter.onClickListener() {
            @Override
            public void onItemClicked(int position) {
                //Toast.makeText(getApplicationContext(), "Room created: " + chatRoom, Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "Clicked at " + position);
                // create new activity
                Intent i = new Intent(MainActivity.this, ChatActivity.class);
                // pass room
                //i.putExtra(KEY_ITEM_TEXT, items.get(position));
                //i.putExtra(KEY_ITEM_POSITION, position);
                //i.putExtra("RoomName", etItem.getText().toString());
                // display activity
                startActivity(i);
            }


        };

        adapter = new ChatAdapter(items, onClickListener);
        rvItems.setAdapter(adapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MainActivity", "Button clicked in Main Activity!");
                String chatRoom = etItem.getText().toString();

                // Add item to the model
                items.add(chatRoom);
                // Notify adapter
                adapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");

                //HashMap<String, Object> hashMap = new HashMap<>();
                //hashMap.put(etItem.getText().toString(), "");
                //reference.updateChildren(hashMap);
                Toast.makeText(getApplicationContext(), "Room created: " + chatRoom, Toast.LENGTH_SHORT).show();
            }
        });

        btnLogOut = findViewById(R.id.btnLogout);

        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
    }
}