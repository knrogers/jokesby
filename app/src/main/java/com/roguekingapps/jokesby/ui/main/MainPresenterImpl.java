package com.roguekingapps.jokesby.ui.main;

import com.roguekingapps.jokesby.data.DataManager;
import com.roguekingapps.jokesby.data.network.model.Joke;
import com.roguekingapps.jokesby.di.PerActivity;

import java.util.List;

import javax.inject.Inject;

/**
 * Communicates with the View and Model to load and display jokes.
 */
@PerActivity
public class MainPresenterImpl implements MainPresenter {

    private DataManager dataManager;
    private MainView mainView;

    @Inject
    MainPresenterImpl(DataManager dataManager, MainView mainView) {
        this.dataManager = dataManager;
        this.mainView = mainView;
    }

    @Override
    public void loadFromApi() {
        dataManager.loadFromApi(this);
    }

    @Override
    public void loadFromFavourites() {
        dataManager.loadFromFavourites(this);
    }

    @Override
    public void showJokes(List<Joke> jokes) {
        mainView.showJokes(jokes);
    }

    @Override
    public void showError(String message) {
        mainView.showError(message);
    }
}
