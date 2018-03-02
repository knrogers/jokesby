package com.roguekingapps.jokesby.ui.main;

import com.roguekingapps.jokesby.data.network.model.Joke;

import java.util.List;

public interface MainPresenter {

    void loadFromApi();

    void loadFromFavourites();

    void showJokes(List<Joke> jokes);

    void showError(String message);
}
