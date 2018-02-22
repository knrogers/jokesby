package com.roguekingapps.jokesby.di.module;

import com.roguekingapps.jokesby.ui.adapter.ListAdapter;
import com.roguekingapps.jokesby.ui.main.MainActivity;
import com.roguekingapps.jokesby.ui.main.MainPresenter;
import com.roguekingapps.jokesby.ui.main.MainPresenterImpl;
import com.roguekingapps.jokesby.ui.main.MainView;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private MainActivity activity;

    public ActivityModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    MainView provideMainView() {
        return activity;
    }

    @Provides
    ListAdapter provideListAdapter() {
        return new ListAdapter(activity);
    }

    @Provides
    MainPresenter provideMainPresenter(MainPresenterImpl mainPresenter) {
        return mainPresenter;
    }
}
