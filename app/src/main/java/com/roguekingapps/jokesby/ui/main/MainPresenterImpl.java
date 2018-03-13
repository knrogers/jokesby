package com.roguekingapps.jokesby.ui.main;

import com.roguekingapps.jokesby.data.DataManager;
import com.roguekingapps.jokesby.data.model.Joke;
import com.roguekingapps.jokesby.di.PerActivity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Communicates with the View and Model to load and display jokes.
 */
@PerActivity
public class MainPresenterImpl implements MainPresenter {

    private DataManager dataManager;
    private MainView mainView;
    private CompositeDisposable disposables;
    private String after;

    @Inject
    MainPresenterImpl(DataManager dataManager,
                      MainView mainView,
                      CompositeDisposable disposables) {
        this.dataManager = dataManager;
        this.mainView = mainView;
        this.disposables = disposables;
    }

    @Override
    public void onStartLoad() {
        mainView.onStartLoad();
    }

    @Override
    public void onPostLoad() {
        mainView.onPostLoad();
    }

    @Override
    public void setAfter(String after) {
        this.after = after;
    }

    @Override
    public void loadHot() {
        dataManager.loadHot(this, after);
    }

    @Override
    public void loadRandom() {
        dataManager.loadRandom(this);
    }

    @Override
    public void loadFavourites() {
        dataManager.loadFavourites(this);
    }

    @Override
    public void loadRated() {
        dataManager.loadRated(this);
    }

    @Override
    public void showJokesFromApi(List<Joke> jokes) {
        mainView.showJokesFromApi(jokes);
    }

    @Override
    public void showJokesFromDatabase(List<Joke> jokes) {
        mainView.showJokesFromDatabase(jokes);
    }

    @Override
    public void showError(String message) {
        mainView.showError(message);
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
