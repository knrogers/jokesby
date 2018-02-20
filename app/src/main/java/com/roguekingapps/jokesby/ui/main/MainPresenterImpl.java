package com.roguekingapps.jokesby.ui.main;

import com.roguekingapps.jokesby.data.network.ApiHelper;
import com.roguekingapps.jokesby.di.PerActivity;

import javax.inject.Inject;

/**
 * Communicates with the View and Model to load and display jokes.
 */
@PerActivity
public class MainPresenterImpl implements MainPresenter {

    private ApiHelper apiHelper;
    private MainView mainView;

    @Inject
    MainPresenterImpl(ApiHelper apiHelper, MainView mainView) {
        this.apiHelper = apiHelper;
        this.mainView = mainView;
    }

    @Override
    public void loadJokes() {
        apiHelper.loadJokes(this);
    }

    @Override
    public void showJokes() {
        mainView.showJokes();
    }
}
