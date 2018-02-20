package com.roguekingapps.jokesby.di.module;

import com.roguekingapps.jokesby.ui.main.MainPresenter;
import com.roguekingapps.jokesby.ui.main.MainPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    @Provides
    MainPresenter providesMainPresenter(MainPresenterImpl mainPresenter) {
        return mainPresenter;
    }
}
