package com.example.letsjam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.InputQueue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.asynchttpclient.callback.TextHttpResponseHandler;
import com.example.letsjam.adapters.MovieAdapter;
import com.example.letsjam.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class SearchActivity extends AppCompatActivity {

    private String YoutubeUrl1 = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=";
    private String YoutubeUrl2 = "&type=video&maxResults=50&key=AIzaSyBNC0ZjzuuV8jYUD9cS9r1BCs4uC4YGWWg";
    public static final String TAG = "SearchActivity";
    private String newYoutubeUrl;
    private EditText inputBtn;
    private Button searchBtn;
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();
        inputBtn = findViewById(R.id.input_query);
        searchBtn = findViewById(R.id.btn_search);


        //Create the adapter
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);
        //Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        //Set a Layout Manager on the reycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movies.clear();

                String searchString = inputBtn.getText().toString();
                System.out.println(searchString);
                //Need to fix if there are any spaces in search input
                newYoutubeUrl = YoutubeUrl1 + searchString + YoutubeUrl2;
                System.out.println(newYoutubeUrl);

                AsyncHttpClient client = new AsyncHttpClient();
                client.get(newYoutubeUrl, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d(TAG, "onSuccess");
                        JSONObject jsonObject = json.jsonObject;
                        try {
                            JSONArray items = jsonObject.getJSONArray("items");
                            Log.i(TAG, "Results " + items.toString());
                            movies.addAll(Movie.fromJsonArray(items));
                            movieAdapter.notifyDataSetChanged();
                            System.out.println(movies.size());
                        } catch (JSONException e) {
                            Log.e(TAG, "Hit json exception", e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.d(TAG, "onFailure");
                    }
                });
            }
        });
    }
}