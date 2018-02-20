package com.roguekingapps.jokesby.data.network.model;

import com.google.gson.annotations.SerializedName;

public class Joke {

    private String id;
    private String title;

    @SerializedName("selftext")
    private String body;

    @SerializedName("author")
    private String user;

    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
