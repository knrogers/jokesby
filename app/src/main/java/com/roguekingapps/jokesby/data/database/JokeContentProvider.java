package com.roguekingapps.jokesby.data.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.roguekingapps.jokesby.di.component.DaggerDatabaseComponent;
import com.roguekingapps.jokesby.di.component.DatabaseComponent;

import javax.inject.Inject;

/**
 * Manages access to the data stored in the favourites table of the database.
 */
public class JokeContentProvider extends ContentProvider {

    private static final String INVALID_CONTEXT_VALUE = "Invalid context value: context is null.";
    private DatabaseComponent databaseComponent;

    @Inject
    UriMatcher uriMatcher;


    @Override
    public boolean onCreate() {
        getDatabaseComponent();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Context context = getContext();
        if (context == null) {
            throw new NullPointerException(INVALID_CONTEXT_VALUE);
        }

        Cursor cursor = null;
        return cursor;

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Context context = getContext();
        if (context == null) {
            throw new NullPointerException(INVALID_CONTEXT_VALUE);
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    public DatabaseComponent getDatabaseComponent() {
        if (databaseComponent == null) {
            databaseComponent = DaggerDatabaseComponent.builder()
                    .build();
        }
        return databaseComponent;
    }
}