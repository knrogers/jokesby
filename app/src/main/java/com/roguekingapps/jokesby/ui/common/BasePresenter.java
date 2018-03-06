package com.roguekingapps.jokesby.ui.common;

import io.reactivex.disposables.Disposable;

public interface BasePresenter {

    void showError(String message);

    void addDisposable(Disposable disposable);

    void clearDisposables();
}
