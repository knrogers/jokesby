package com.roguekingapps.jokesby.di.module;

import com.roguekingapps.jokesby.di.PerActivity;
import com.roguekingapps.jokesby.ui.detail.DetailActivity;
import com.roguekingapps.jokesby.ui.detail.DetailPresenter;
import com.roguekingapps.jokesby.ui.detail.DetailPresenterImpl;
import com.roguekingapps.jokesby.ui.detail.DetailView;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class DetailActivityModule {

    private DetailActivity activity;

    public DetailActivityModule(DetailActivity activity) {
        this.activity = activity;
    }

    @Provides
    DetailView provideDetailView() {
        return activity;
    }

    @Provides
    DetailPresenter provideDetailPresenter(DetailPresenterImpl detailPresenter) {
        return detailPresenter;
    }

    @Provides
    @PerActivity
    CompositeDisposable provideDetailDisposables() {
        return new CompositeDisposable();
    }
}
