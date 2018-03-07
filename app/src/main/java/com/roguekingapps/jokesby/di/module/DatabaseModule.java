package com.roguekingapps.jokesby.di.module;

import android.content.Context;
import android.content.UriMatcher;

import com.roguekingapps.jokesby.data.database.JokeContract;
import com.roguekingapps.jokesby.di.DatabaseName;
import com.roguekingapps.jokesby.di.DatabaseVersion;
import com.roguekingapps.jokesby.di.Favourites;
import com.roguekingapps.jokesby.di.InvalidContextString;
import com.roguekingapps.jokesby.di.Rated;

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
    @DatabaseName
    String provideDatabaseName() {
        return "jokesby.db";
    }

    @Provides
    @DatabaseVersion
    int provideDatabaseVersion() {
        return 1;
    }

    @Provides
    @InvalidContextString
    String provideInvalidContextString() {
        return "Invalid context value: context is null.";
    }

    @Provides
    @Singleton
    UriMatcher provideUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(JokeContract.AUTHORITY, JokeContract.PATH_FAVOURITES, provideFavourites());
        uriMatcher.addURI(JokeContract.AUTHORITY, JokeContract.PATH_RATED, provideRated());
        return uriMatcher;
    }

    @Provides
    @Favourites
    int provideFavourites() {
        return 100;
    }

    @Provides
    @Rated
    int provideRated() {
        return 200;
    }
}
