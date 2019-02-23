package com.githubpr.data;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("login")
    private String userName;

    @SerializedName("avatar_url")
    private String thumbnail;

    public User(String userName, String thumbnail) {
        this.userName = userName;
        this.thumbnail = thumbnail;
    }

    public String getUserName() {
        return userName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

}
