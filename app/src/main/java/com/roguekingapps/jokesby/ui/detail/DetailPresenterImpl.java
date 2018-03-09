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
    public void onStartLoad() {
        detailView.onStartLoad();
    }

    @Override
    public void onPostLoad() {
        detailView.onPostLoad();
    }

    @Override
    public void queryFavourite(String apiId) {
        dataManager.queryFavourite(this, apiId);
    }

    @Override
    public void updateFavourite(Joke joke) {
        dataManager.updateFavourite(this, joke);
    }

    @Override
    public void queryRated(String apiId) {
        dataManager.queryRated(this, apiId);
    }

    @Override
    public void updateRated(Joke joke) {
        dataManager.updateRated(this, joke);
    }

    @Override
    public void deleteRated(String apiId) {
        dataManager.deleteRated(this, apiId);
    }

    @Override
    public void onPostUpdateRating() {
        detailView.onPostUpdateRating();
    }

    @Override
    public void checkRating(String rating) {
        detailView.checkRating(rating);
    }

    @Override
    public void onPostUpdateFavourite(boolean favourite) {
        detailView.onPostUpdateFavourite(favourite);
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
