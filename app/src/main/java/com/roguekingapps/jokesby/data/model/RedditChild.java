package com.roguekingapps.jokesby.data.model;

import com.google.gson.annotations.SerializedName;

public class RedditChild {

    @SerializedName("data")
    private Joke joke;

    public Joke getJoke() {
        return joke;
    }

    public void setJoke(Joke joke) {
        this.joke = joke;
    }
}
