package com.roguekingapps.jokesby.ui.detail;

import com.roguekingapps.jokesby.data.DataManager;
import com.roguekingapps.jokesby.data.model.Joke;
import com.roguekingapps.jokesby.di.PerActivity;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Communicates with the View and Model to delete jokes from or insert jokes into the database.
 */
@PerActivity
public class DetailPresenterImpl implements DetailPresenter {

    private DataManager dataManager;
    private DetailView detailView;
    private CompositeDisposable disposables;

    @Inject
    DetailPresenterImpl(DataManager dataManager,
                        DetailView detailView,
                        CompositeDisposable disposables) {
        this.dataManager = dataManager;
        this.detailView = detailView;
        this.disposables = disposables;
    }

    @Override
    public void query(String apiId) {
        dataManager.query(this, apiId);
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

    @Override
    public void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    @Override
    public void clearDisposables() {
        disposables.clear();
    }
}
