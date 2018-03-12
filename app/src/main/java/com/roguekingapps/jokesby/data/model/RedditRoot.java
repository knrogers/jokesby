package com.roguekingapps.jokesby.data.model;

import com.google.gson.annotations.SerializedName;

public class RedditRoot {

    @SerializedName("data")
    private RedditData redditData;

    public RedditData getRedditData() {
        return redditData;
    }

    public void setRedditData(RedditData redditData) {
        this.redditData = redditData;
    }
}
