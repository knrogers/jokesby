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

    @Inject
    MainPresenterImpl(DataManager dataManager,
                      MainView mainView,
                      CompositeDisposable disposables) {
        this.dataManager = dataManager;
        this.mainView = mainView;
        this.disposables = disposables;
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
    public void showJokesFromApi(List<Joke> jokes) {
        mainView.showJokesFromApi(jokes);
    }

    @Override
    public void showJokesFromFavourites(List<Joke> jokes) {
        mainView.showJokesFromFavourites(jokes);
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
