package com.roguekingapps.jokesby.ui.main;

import com.roguekingapps.jokesby.data.model.Joke;

import java.util.List;

public interface MainView {

    void showJokes(List<Joke> jokes);

    void showError(String message);
}
