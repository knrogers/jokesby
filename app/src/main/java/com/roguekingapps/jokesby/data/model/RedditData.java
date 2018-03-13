package com.roguekingapps.jokesby.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RedditData {

    @SerializedName("children")
    private List<RedditChild> redditChildren;

    private String after;

    public List<RedditChild> getRedditChildren() {
        return redditChildren;
    }

    public void setRedditChildren(List<RedditChild> redditChildren) {
        this.redditChildren = redditChildren;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }
}
