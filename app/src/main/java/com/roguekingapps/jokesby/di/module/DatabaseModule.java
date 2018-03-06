package com.roguekingapps.jokesby.di.module;

import android.content.Context;
import android.content.UriMatcher;

import com.roguekingapps.jokesby.data.database.JokeContract;
import com.roguekingapps.jokesby.di.DatabaseVersion;
import com.roguekingapps.jokesby.di.Favourites;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    private Context context;

    public DatabaseModule(Context context) {
        this.context = context;
    }

    @Provides
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    UriMatcher provideUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(JokeContract.AUTHORITY, JokeContract.PATH_FAVOURITES, provideFavourites());
        return uriMatcher;
    }

    @Provides
    @Favourites
    int provideFavourites() {
        return 100;
    }
    
    @Provides
    String provideDatabaseName() {
        return "jokesby.db";
    }

    @Provides
    @DatabaseVersion
    int provideDatabaseVersion() {
        return 1;
    }
}
