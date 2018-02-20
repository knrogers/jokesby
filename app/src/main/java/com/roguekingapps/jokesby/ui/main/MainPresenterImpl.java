package com.roguekingapps.jokesby.ui.main;

import android.util.Log;

import com.roguekingapps.jokesby.data.network.ApiHelper;
import com.roguekingapps.jokesby.di.PerActivity;

import javax.inject.Inject;
/**
 * Communicates with the View and Model to load and display jokes.
 */
@PerActivity
public class MainPresenterImpl implements MainPresenter {

    private ApiHelper apiHelper;

    @Inject
    MainPresenterImpl(ApiHelper apiHelper) {
        this.apiHelper = apiHelper;
    }

    @Override
    public void loadJokes() {
        apiHelper.loadJokes(this);
    }

    @Override
    public void showJokes() {
        Log.i(MainPresenterImpl.class.getSimpleName(), "show jokes!");
    }
}
