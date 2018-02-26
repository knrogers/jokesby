package com.roguekingapps.jokesby.di.component;

import com.roguekingapps.jokesby.data.database.JokeContentProvider;
import com.roguekingapps.jokesby.di.module.DatabaseModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = DatabaseModule.class)
public interface DatabaseComponent {

    void inject(JokeContentProvider contentProvider);
}
