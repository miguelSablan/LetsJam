package com.example.letsjam;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;

public class ChatActivity extends AppCompatActivity {
    EditText messageInput;
    Button btnSend;

    ScrollView displayMessages;
    TextView textMessage;

    DatabaseReference roomReference, GroupMessageKeyRef;
    private String currentRoom, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageInput = findViewById(R.id.messageInput);
        btnSend = findViewById(R.id.btnSend);

        textMessage = findViewById(R.id.textMessage);
        displayMessages = findViewById(R.id.displayMessages);

        // Get current room name that's stored from Firebase
        currentRoom = getIntent().getExtras().get("ChatRoom").toString();
        getSupportActionBar().setTitle(currentRoom);

        // Get user name
        userName = getIntent().getExtras().get("Name").toString();

        // ChatRooms -> Room
        roomReference = FirebaseDatabase.getInstance().getReference().child("ChatRooms").child(currentRoom);
        Log.i("Chat Activity!", "Room ref: " +  roomReference);

        // Tap button to send message
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //textView.setText(userName + ": " + etMessage.getText().toString());
                SaveMessageInfoToDatabase();

                messageInput.setText("");

                displayMessages.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        roomReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DisplayMessages(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DisplayMessages(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DisplayMessages(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    private void DisplayMessages(DataSnapshot snapshot) {
        Iterator iterator = snapshot.getChildren().iterator();

        while(iterator.hasNext()) {
            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();

            textMessage.append(chatName + ": " + chatMessage + "\n");

            displayMessages.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

    private void SaveMessageInfoToDatabase() {
        String message = messageInput.getText().toString();
        String messageKey =  roomReference.push().getKey();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Need to write a message first!", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> groupMessage = new HashMap<>();
            roomReference.updateChildren(groupMessage);

            GroupMessageKeyRef = roomReference.child(messageKey);

            HashMap<String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("name", userName);
            messageInfoMap.put("message", message);

            GroupMessageKeyRef.updateChildren(messageInfoMap);
        }
    }
}

