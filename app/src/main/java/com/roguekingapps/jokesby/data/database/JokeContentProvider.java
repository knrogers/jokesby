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
import com.roguekingapps.jokesby.data.database.JokeContract.RatedEntry;
import com.roguekingapps.jokesby.di.Favourites;
import com.roguekingapps.jokesby.di.InvalidContextString;
import com.roguekingapps.jokesby.di.Rated;
import com.roguekingapps.jokesby.di.component.DaggerDatabaseComponent;
import com.roguekingapps.jokesby.di.component.DatabaseComponent;
import com.roguekingapps.jokesby.di.module.DatabaseModule;

import javax.inject.Inject;

/**
 * Manages access to the data stored in the jokesby database.
 */
public class JokeContentProvider extends ContentProvider {

    private DatabaseComponent databaseComponent;

    @Inject
    @InvalidContextString
    String invalidContextValue;

    @Inject
    UriMatcher uriMatcher;

    @Inject
    DatabaseOpenHelper databaseOpenHelper;

    @Inject
    @Favourites
    int favourites;

    @Inject
    @Rated
    int rated;

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
            throw new NullPointerException(invalidContextValue);
        }

        int match = uriMatcher.match(uri);
        Cursor cursor;
        if (match == favourites) {
            cursor = databaseOpenHelper.getReadableDatabase().query(
                    FavouriteEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
        } else if (match == rated) {
            cursor = databaseOpenHelper.getReadableDatabase().query(
                    RatedEntry.TABLE_NAME,
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
            throw new NullPointerException(invalidContextValue);
        }

        int match = uriMatcher.match(uri);
        Uri responseUri;
        if (match == favourites) {
            responseUri = getResponseUri(context, uri, contentValues,
                    FavouriteEntry.TABLE_NAME, FavouriteEntry.CONTENT_URI);
        } else if (match == rated) {
            responseUri = getResponseUri(context, uri, contentValues,
                    RatedEntry.TABLE_NAME, RatedEntry.CONTENT_URI);
        } else {
            throw new UnsupportedOperationException(context.getString(R.string.unknown_uri) + uri);
        }
        context.getContentResolver().notifyChange(responseUri, null);

        return responseUri;
    }

    private Uri getResponseUri(Context context, @NonNull Uri insertUri,
                               @Nullable ContentValues contentValues,
                               String tableName, Uri contentUri) {
        Uri responseUri;
        long id = databaseOpenHelper.getWritableDatabase()
                .insert(tableName, null, contentValues);

        if (id > 0) {
            responseUri = ContentUris.withAppendedId(contentUri, id);
        } else {
            throw new SQLException(context.getString(R.string.insert_row_failed) + insertUri);
        }
        return responseUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Context context = getContext();
        if (context == null) {
            throw new NullPointerException(invalidContextValue);
        }

        int match = uriMatcher.match(uri);
        int rowsDeleted;
        if (match == favourites) {
            rowsDeleted = databaseOpenHelper.getReadableDatabase().delete(
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