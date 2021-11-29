package com.example.letsjam;

public class Songs {
    private String songID, videoPicture, pushKey;

    public Songs(){}


    public Songs(String songID, String videoPicture, String pushKey) {
        this.songID = songID;
        this.videoPicture = videoPicture;
        this.pushKey = pushKey;
    }

    public String getSongID() {
        return songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }

    public String getVideoPicture() {
        return videoPicture;
    }

    public void setVideoPicture(String videoPicture) {
        this.videoPicture = videoPicture;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }
}
