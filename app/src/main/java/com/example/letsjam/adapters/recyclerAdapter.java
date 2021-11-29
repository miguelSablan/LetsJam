package com.example.letsjam.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.letsjam.Songs;
import com.example.letsjam.R;
import com.example.letsjam.Songs;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    Context context;
    private ArrayList<Songs> musicList;

    public recyclerAdapter(ArrayList<Songs> musicList) {
        this.musicList = musicList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView songID;
        private ImageView videoPicture;

        public MyViewHolder(final View view) {
            super(view);

            videoPicture = itemView.findViewById(R.id.youtubeImage);
            songID = itemView.findViewById(R.id.videoD);

        }
    }


        @NonNull
        @Override
        public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);

            MyViewHolder viewHolder = new MyViewHolder(itemView);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {

            Songs songs = musicList.get(position);

            holder.songID.setText(songs.getSongID());

            String videoPictureUrl = songs.getVideoPicture();
            Glide.with(holder.itemView.getContext()).load(videoPictureUrl).into(holder.videoPicture);


        }

        @Override
        public int getItemCount() {
            return musicList.size();
        }
    }
