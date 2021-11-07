package com.example.letsjam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

public class ChatActivity extends AppCompatActivity {
    EditText etMessage;
    Button btnSend;
    DatabaseReference reference;

    private String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        // Get room name that's stored from Firebase
        roomName = getIntent().getExtras().get("ChatRoom").toString();
        getSupportActionBar().setTitle(roomName);
        //getSupportActionBar().setTitle("Room Name");

    }
}

