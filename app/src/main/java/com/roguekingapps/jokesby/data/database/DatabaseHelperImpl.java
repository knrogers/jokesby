package com.roguekingapps.jokesby.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.database.JokeContract.FavouriteEntry;
import com.roguekingapps.jokesby.data.database.JokeContract.RatedEntry;
import com.roguekingapps.jokesby.data.model.Joke;
import com.roguekingapps.jokesby.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Accesses the favourites database to process CRUD operations.
 */
@Singleton
public class DatabaseHelperImpl implements DatabaseHelper {

    private Context context;

    @Inject
    DatabaseHelperImpl(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public Observable<Cursor> getQueryObservable(
            final Uri contentUri,
            final String columnApiId,
            final String apiId) {
        return Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> emitter) throws Exception {
                Cursor cursor = context.getContentResolver().query(
                        contentUri, null,
                        columnApiId + context.getString(R.string.parameter_placeholder),
                        new String[]{apiId}, null);
                if (cursor == null) {
                    emitter.onError(new Exception("Cursor returned from query was null."));
                    return;
                }
                emitter.onNext(cursor);
            }
        });
    }

    @Override
    public Observable<Cursor> getQueryAllFavouritesObservable() {
        return Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> emitter) throws Exception {
                Cursor cursor = context.getContentResolver()
                        .query(FavouriteEntry.CONTENT_URI, null, null, null, null);
                if (cursor == null) {
                    emitter.onError(new Exception("Cursor returned from query was null."));
                    return;
                }
                emitter.onNext(cursor);
            }
        });
    }

    @Override
    public Observable<Integer> getDeleteObservable(String apiId) {
        return Observable.just(context.getContentResolver().delete(
                        FavouriteEntry.CONTENT_URI,
                        FavouriteEntry.COLUMN_API_ID,
                        new String[]{apiId}));
    }

    @Override
    public Observable<Integer> getUpdateObservable(Joke joke) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RatedEntry.COLUMN_RATING, joke.getRating());
        return Observable.just(context.getContentResolver().update(
                RatedEntry.CONTENT_URI,
                contentValues,
                RatedEntry.COLUMN_API_ID,
                new String[]{joke.getId()}));
    }

    @Override
    public Observable<Uri> getInsertFavouriteObservable(Joke joke) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(FavouriteEntry.COLUMN_API_ID, joke.getId());
        contentValues.put(FavouriteEntry.COLUMN_TITLE, joke.getTitle());
        contentValues.put(FavouriteEntry.COLUMN_BODY, joke.getBody());
        contentValues.put(FavouriteEntry.COLUMN_USER, joke.getUser());
        contentValues.put(FavouriteEntry.COLUMN_URL, joke.getUrl());

        return Observable.create(new ObservableOnSubscribe<Uri>() {
            @Override
            public void subscribe(ObservableEmitter<Uri> emitter) throws Exception {
                Uri uri = context.getContentResolver().insert(FavouriteEntry.CONTENT_URI, contentValues);
                if (uri == null) {
                    emitter.onError(new Exception("Uri returned from insert was null."));
                    return;
                }
                emitter.onNext(uri);
            }
        });
    }

    @Override
    public Observable<Uri> getInsertRatedObservable(Joke joke) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(RatedEntry.COLUMN_API_ID, joke.getId());
        contentValues.put(RatedEntry.COLUMN_TITLE, joke.getTitle());
        contentValues.put(RatedEntry.COLUMN_BODY, joke.getBody());
        contentValues.put(RatedEntry.COLUMN_USER, joke.getUser());
        contentValues.put(RatedEntry.COLUMN_URL, joke.getUrl());
        contentValues.put(RatedEntry.COLUMN_RATING, joke.getRating());

        return Observable.create(new ObservableOnSubscribe<Uri>() {
            @Override
            public void subscribe(ObservableEmitter<Uri> emitter) throws Exception {
                Uri uri = context.getContentResolver().insert(RatedEntry.CONTENT_URI, contentValues);
                if (uri == null) {
                    emitter.onError(new Exception("Uri returned from insert was null."));
                    return;
                }
                emitter.onNext(uri);
            }
        });
    }
}
