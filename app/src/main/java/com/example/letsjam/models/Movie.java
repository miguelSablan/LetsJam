package com.example.letsjam.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    String videoId;
    String title;

    public Movie(JSONObject jsonObject) throws JSONException {
        videoId = jsonObject.getJSONObject("id").getString("videoId");
        System.out.println(getVideoId());
        title = jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");
        System.out.println(getTitle());
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for(int i = 0; i < movieJsonArray.length(); i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

}
