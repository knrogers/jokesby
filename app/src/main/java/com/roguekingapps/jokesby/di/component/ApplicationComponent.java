package com.roguekingapps.jokesby.di.component;

import android.graphics.Typeface;

import com.roguekingapps.jokesby.JokesbyApplication;
import com.roguekingapps.jokesby.data.DataManager;
import com.roguekingapps.jokesby.di.module.ApplicationModule;
import com.roguekingapps.jokesby.di.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(JokesbyApplication jokesbyApplication);

    DataManager getDataManager();

    Typeface getRobotoMedium();
}
