package com.roguekingapps.jokesby.ui.main;

import com.roguekingapps.jokesby.data.model.Joke;
import com.roguekingapps.jokesby.ui.common.BaseView;

import java.util.List;

public interface MainView extends BaseView {

    void showJokesFromApi(List<Joke> jokes);

    void showJokesFromDatabase(List<Joke> jokes);

    void showError(String message);
}
