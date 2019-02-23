package com.meesho.githubpr.data;

import com.google.gson.annotations.SerializedName;

public class DataModel implements BaseData {

    @SerializedName("number")
    private String number;

    @SerializedName("title")
    private String title;

    @SerializedName("user")
    private User user;

    @SerializedName("state")
    private String state;

    public DataModel(String number, String title, User user, String state) {
        this.number = number;
        this.title = title;
        this.user = user;
        this.state = state;
    }

    public String getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getState() {
        return state;
    }

    public User getUser() {
        return user;
    }
}
