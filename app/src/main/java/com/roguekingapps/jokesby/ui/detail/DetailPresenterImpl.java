package com.roguekingapps.jokesby.ui.detail;

import com.roguekingapps.jokesby.data.DataManager;
import com.roguekingapps.jokesby.data.network.model.Joke;
import com.roguekingapps.jokesby.di.PerActivity;

import javax.inject.Inject;

/**
 * Communicates with the View and Model to delete jokes from or insert jokes into the database.
 */
@PerActivity
public class DetailPresenterImpl implements DetailPresenter {

    private DataManager dataManager;
    private DetailView detailView;

    @Inject
    DetailPresenterImpl(DataManager dataManager, DetailView detailView) {
        this.dataManager = dataManager;
        this.detailView = detailView;

    }

    @Override
    public void update(Joke joke) {
        dataManager.updateJoke(this, joke);
    }

    @Override
    public void updateFavouriteIcon(boolean favourite) {
        detailView.updateFavouriteIcon(favourite);
    }

    @Override
    public void showError(String message) {
        detailView.showError(message);
    }
}
