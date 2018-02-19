package com.roguekingapps.jokesby.di.component;

import com.roguekingapps.jokesby.JokesbyApplication;
import com.roguekingapps.jokesby.data.network.ApiHelper;
import com.roguekingapps.jokesby.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(JokesbyApplication jokesbyApplication);

    ApiHelper getApiHelper();
}
