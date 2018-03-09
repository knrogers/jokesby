package com.roguekingapps.jokesby.ui.main;

import com.roguekingapps.jokesby.data.model.Joke;
import com.roguekingapps.jokesby.ui.common.BasePresenter;

import java.util.List;

public interface MainPresenter extends BasePresenter {

    void loadFromApi();

    void loadFromFavourites();

    void loadFromRated();

    void showJokesFromApi(List<Joke> jokes);

    void showJokesFromDatabase(List<Joke> jokes);
}
