package com.roguekingapps.jokesby.di.module;

import android.content.Context;

import com.roguekingapps.jokesby.di.ActivityContext;
import com.roguekingapps.jokesby.di.PerActivity;
import com.roguekingapps.jokesby.ui.bottomnavigation.BottomNavigationItemReselectedListener.OnNavigationItemReselectedCallback;
import com.roguekingapps.jokesby.ui.bottomnavigation.BottomNavigationItemSelectedListener.OnNavigationItemSelectedCallback;
import com.roguekingapps.jokesby.ui.main.MainActivity;
import com.roguekingapps.jokesby.ui.main.MainPresenter;
import com.roguekingapps.jokesby.ui.main.MainPresenterImpl;
import com.roguekingapps.jokesby.ui.main.MainView;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class MainActivityModule {

    private MainActivity activity;

    public MainActivityModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityContext
    Context provideActivityContext() {
        return activity;
    }

    @Provides
    MainView provideMainView() {
        return activity;
    }

    @Provides
    OnNavigationItemSelectedCallback provideOnNavigationItemSelectedCallback() {
        return activity;
    }

    @Provides
    OnNavigationItemReselectedCallback provideOnNavigationItemReselectedCallback() {
        return activity;
    }

    @Provides
    MainPresenter provideMainPresenter(MainPresenterImpl mainPresenter) {
        return mainPresenter;
    }

    @Provides
    @PerActivity
    CompositeDisposable provideMainDisposables() {
        return new CompositeDisposable();
    }
}
