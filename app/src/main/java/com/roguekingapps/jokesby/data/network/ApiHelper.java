package com.roguekingapps.jokesby.data.network;

import com.roguekingapps.jokesby.data.network.model.JokeContainer;

import io.reactivex.functions.Consumer;

public interface ApiHelper {

    void loadJokes(Consumer<JokeContainer> jokeConsumer);
}
