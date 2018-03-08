package com.roguekingapps.jokesby.ui.main;

import com.roguekingapps.jokesby.data.model.Joke;

import java.util.List;

public interface MainView {

    void showJokesFromApi(List<Joke> jokes);

    void showJokesFromFavourites(List<Joke> jokes);

    void showError(String message);
}
