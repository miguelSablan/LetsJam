package com.example.letsjam.adapters;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.letsjam.ChatActivity;
import com.example.letsjam.MainActivity;
import com.example.letsjam.R;
import com.example.letsjam.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get the movie at th passed in position
        Movie movie = movies.get(position);
        //Bind the movie date into the VH
        holder.bind(movie);

        //ClickListener for when user clicks on a video
        holder.itemView.setOnClickListener(view -> {
            Toast.makeText(context, movie.getVideoId() + " " + movie.getTitle(), Toast.LENGTH_SHORT).show();

            //Intent brings back to lounge room
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("videoId", movie.getVideoId());
            intent.putExtra("videoPicture", movie.getTitle());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        TextView videoID;
        ImageView ivPoster;
        String imageUrl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            videoID = itemView.findViewById(R.id.videoID);
            ivPoster = itemView.findViewById(R.id.ivPoster);
        }

        public void bind(Movie movie) {
            imageUrl = movie.getTitle();
            //tvTitle.setText(movie.getTitle());
            videoID.setText(movie.getVideoId());
            //To upload image into the recycler view
            Glide.with(context).load(imageUrl).override(2000,200).into(ivPoster);
        }
    }
}
