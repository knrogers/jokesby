package com.roguekingapps.jokesby.ui.main;

import com.roguekingapps.jokesby.data.network.model.Joke;

import java.util.List;

public interface MainPresenter {

    void loadJokes();
    void showJokes(List<Joke> jokes);
}
