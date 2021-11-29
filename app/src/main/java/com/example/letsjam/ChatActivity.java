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

import android.content.Intent;
import android.nfc.Tag;
import android.widget.ImageButton;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letsjam.adapters.MovieAdapter;
import com.example.letsjam.adapters.recyclerAdapter;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private ArrayList<Songs> musicList;
    private RecyclerView recyclerView;
    private ImageButton searchBtn;
    private String videoId = "";
    private String videoPicture;
    private String pushKey;
    private recyclerAdapter adapter;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;
    private YouTubePlayerView ytPlayerView;
    private YouTubePlayer ytPlayer;

    String api_key = "AIzaSyBNC0ZjzuuV8jYUD9cS9r1BCs4uC4YGWWg";

    private MyPlayerStateChangeListener myPlayerStateChangeListener;
    private MyPlaybackEventListener myPlayBackEventListener;

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

        currentRoom = getIntent().getExtras().get("ChatRoom").toString();

        // Get current room name that's stored from Firebase
        root = db.getReference().child(currentRoom);
        System.out.println(root);
        //getSupportActionBar().setTitle(currentRoom);

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

        recyclerView = findViewById(R.id.musicPlaylist);
        searchBtn = findViewById(R.id.searchBtn);
        ytPlayerView = findViewById(R.id.ytPlayer);

        musicList = new ArrayList<>();

        // Search Button
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearchActivity();
            }
        });

        myPlayerStateChangeListener = new MyPlayerStateChangeListener();
        myPlayBackEventListener = new MyPlaybackEventListener();

        ytPlayerView.initialize(api_key, this);

        /*
        // Updating the queue
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                musicList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Songs songs = dataSnapshot.getValue(Songs.class);
                    musicList.add(songs);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "There was a network issue.", Toast.LENGTH_SHORT).show();
            }
        });
        */
        setAdapter();
        //getIncomingIntent();
    }

    private void setAdapter() {
        adapter = new recyclerAdapter(musicList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    /*private void setMusicInfo(String videoId, String videoPicture) {
        musicList.add(new Music(videoId + " " + videoPicture));

    }*/

    // Opens Search Screen
    public void openSearchActivity(){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    /*
    //Gets info from intent
    private void getIncomingIntent(){
        if(getIntent().hasExtra("videoId") && getIntent().hasExtra("videoPicture")){
            videoId = getIntent().getStringExtra("videoId");
            videoPicture =  getIntent().getStringExtra("videoPicture");

            HashMap<String, String> songMap = new HashMap<>();
            songMap.put("songID", videoId);
            songMap.put("videoPicture", videoPicture);
            //Gets the push ID that fireBase assigns
            pushKey = root.push().getKey();
            songMap.put("pushKey", pushKey);
            System.out.println("The key is " + pushKey);
            root.child(pushKey).setValue(songMap);


            adapter.notifyDataSetChanged();
            System.out.println("The size is: " + musicList.size());
        }
    }
     */

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
        ytPlayer = player;

        Toast.makeText(getApplicationContext(),
                "YouTubePlayer.onInitializationSuccess()",
                Toast.LENGTH_LONG).show();

        ytPlayer.setPlayerStateChangeListener(myPlayerStateChangeListener);
        ytPlayer.setPlaybackEventListener(myPlayBackEventListener);

        if (!b) {
            //This might be making app crash at somepoints when not enough songs
            if (musicList.size() <= 0) {
                player.loadVideo("ublf6qfpuuo");
            } else {
                player.loadVideo(musicList.get(0).getSongID());
                ytPlayer.play();
            }
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(getApplicationContext(), "Video player Failed", Toast.LENGTH_SHORT).show();
    }

    private final class MyPlayerStateChangeListener implements PlayerStateChangeListener {
        String playerState = "UNINITIALIZED";

        @Override
        public void onLoading() {
            playerState = "LOADING";
            //updateText();
            Log.i("PlayerStateListener", playerState);
        }

        @Override
        public void onLoaded(String videoId) {
            playerState = String.format("LOADED %s", videoId);
            //updateText();
            Log.i("PlayerStateListener", playerState);
            ytPlayer.play();
        }

        @Override
        public void onAdStarted() {
            playerState = "AD_STARTED";
            //updateText();
            Log.i("PlayerStateListener", playerState);
        }

        @Override
        public void onVideoStarted() {
            playerState = "VIDEO_STARTED";
            //updateText();
            Log.i("PlayerStateListener", playerState);


        }

        @Override
        public void onVideoEnded() {
            playerState = "VIDEO_ENDED";

            //updateText();
            Log.i("PlayerStateListener", playerState);

            // Remove the video that just ended
            //musicList.remove(0);

            // Play the new first video in the list

            System.out.println("After video end, gonna now play:" + musicList.get(1).getSongID());
            if (musicList.size() < 0) {
                ytPlayer.loadVideo("ublf6qfpuuo");
            }
            else {
                ytPlayer.cueVideo(musicList.get(0).getSongID());
                ytPlayer.play();
                //root.child(musicList.get(0).getPushKey()).removeValue();
                adapter.notifyDataSetChanged();
            }

        }


        @Override
        public void onError(YouTubePlayer.ErrorReason reason) {
            playerState = "ERROR (" + reason + ")";
            if (reason == YouTubePlayer.ErrorReason.UNEXPECTED_SERVICE_DISCONNECTION) {
                // When this error occurs the player is released and can no longer be used.
            }
            Log.i("PlayerStateListener", playerState);
        }

    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        private void updateLog(String prompt){
            //log +=  "MyPlaybackEventListener" + "\n-" +
            //        prompt + "\n\n=====";
            //textVideoLog.setText(log);
        };

        @Override
        public void onBuffering(boolean arg0) {
            updateLog("onBuffering(): " + String.valueOf(arg0));
        }

        @Override
        public void onPaused() {
            updateLog("onPaused()");
        }

        @Override
        public void onPlaying() {
            updateLog("onPlaying()");
        }

        @Override
        public void onSeekTo(int arg0) {
            updateLog("onSeekTo(): " + String.valueOf(arg0));
        }

        @Override
        public void onStopped() {
            updateLog("onStopped()");
        }

    }

    public void getNextSong() {
        musicList.get(0).getSongID();
        System.out.println(musicList.get(0).getSongID());

    }

    public String getVideoNumber() {
        return videoId;
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

