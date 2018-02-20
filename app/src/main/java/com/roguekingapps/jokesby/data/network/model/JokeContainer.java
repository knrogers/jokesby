package com.roguekingapps.jokesby.data.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JokeContainer {

    @SerializedName("data")
    private List<Joke> jokes;

    public List<Joke> getJokes() {
        return jokes;
    }

    public void setJokes(List<Joke> jokes) {
        this.jokes = jokes;
    }
}
