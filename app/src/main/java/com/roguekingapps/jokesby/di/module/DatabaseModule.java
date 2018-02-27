package com.roguekingapps.jokesby.di.module;

import android.content.Context;
import android.content.UriMatcher;

import com.roguekingapps.jokesby.data.database.JokeContract;
import com.roguekingapps.jokesby.di.Favourites;
import com.roguekingapps.jokesby.di.FavouritesWithApiId;

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

        String apiIdPath = JokeContract.PATH_FAVOURITES + "/" + JokeContract.JokeEntry.COLUMN_API_ID + "/#";
        uriMatcher.addURI(JokeContract.AUTHORITY, apiIdPath, provideFavouritesWithApiId());

        return uriMatcher;
    }

    @Provides
    @Favourites
    int provideFavourites() {
        return 100;
    }

    @Provides
    @FavouritesWithApiId
    int provideFavouritesWithApiId() {
        return 101;
    }
}
