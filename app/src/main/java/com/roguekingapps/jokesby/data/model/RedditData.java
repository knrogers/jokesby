package com.roguekingapps.jokesby.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RedditData {

    @SerializedName("children")
    private List<RedditChild> redditChildren;

    public List<RedditChild> getRedditChildren() {
        return redditChildren;
    }

    public void setRedditChildren(List<RedditChild> redditChildren) {
        this.redditChildren = redditChildren;
    }
}
