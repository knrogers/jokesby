package com.roguekingapps.jokesby.data.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.database.JokeContract.FavouriteEntry;
import com.roguekingapps.jokesby.di.Favourites;
import com.roguekingapps.jokesby.di.component.DaggerDatabaseComponent;
import com.roguekingapps.jokesby.di.component.DatabaseComponent;
import com.roguekingapps.jokesby.di.module.DatabaseModule;

import javax.inject.Inject;

/**
 * Manages access to the data stored in the favourites table of the database.
 */
public class JokeContentProvider extends ContentProvider {

    private static final String INVALID_CONTEXT_VALUE = "Invalid context value: context is null.";
    private DatabaseComponent databaseComponent;

    @Inject
    UriMatcher uriMatcher;

    @Inject
    FavouritesOpenHelper favouritesOpenHelper;

    @Inject
    @Favourites
    int favourites;

    @Override
    public boolean onCreate() {
        getDatabaseComponent().inject(this);
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

        int match = uriMatcher.match(uri);
        Cursor cursor;
        if (match == favourites) {
            cursor = favouritesOpenHelper.getReadableDatabase().query(
                    FavouriteEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
        } else {
            throw new UnsupportedOperationException(context.getString(R.string.unknown_uri) + uri);
        }
        if (cursor != null && cursor.getCount() > 0) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Context context = getContext();
        if (context == null) {
            throw new NullPointerException(INVALID_CONTEXT_VALUE);
        }

        int match = uriMatcher.match(uri);
        Uri responseUri;
        if (match == favourites) {
            long id = favouritesOpenHelper.getWritableDatabase()
                    .insert(FavouriteEntry.TABLE_NAME, null, contentValues);

            if (id > 0) {
                responseUri = ContentUris.withAppendedId(FavouriteEntry.CONTENT_URI, id);
            } else {
                throw new SQLException(context.getString(R.string.insert_row_failed) + uri);
            }
        } else {
            throw new UnsupportedOperationException(context.getString(R.string.unknown_uri) + uri);
        }
        context.getContentResolver().notifyChange(responseUri, null);

        return responseUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Context context = getContext();
        if (context == null) {
            throw new NullPointerException(INVALID_CONTEXT_VALUE);
        }

        int match = uriMatcher.match(uri);
        int rowsDeleted;
        if (match == favourites) {
            rowsDeleted = favouritesOpenHelper.getReadableDatabase().delete(
                    FavouriteEntry.TABLE_NAME,
                    selection + context.getString(R.string.parameter_placeholder),
                    selectionArgs);
        } else {
            throw new UnsupportedOperationException(context.getString(R.string.unknown_uri) + uri);
        }
        return rowsDeleted;
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
                    .databaseModule(new DatabaseModule(getContext()))
                    .build();
        }
        return databaseComponent;
    }
}