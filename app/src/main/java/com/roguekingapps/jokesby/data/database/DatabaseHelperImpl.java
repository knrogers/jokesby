package com.roguekingapps.jokesby.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.database.JokeContract.FavouriteEntry;
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
    public Observable<Cursor> getQueryObservable(final String apiId) {
        return Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> emitter) throws Exception {
                Cursor cursor = context.getContentResolver().query(
                        FavouriteEntry.CONTENT_URI, null,
                        FavouriteEntry.COLUMN_API_ID + context.getString(R.string.parameter_placeholder),
                        new String[]{apiId}, null);
                if (cursor == null) {
                    emitter.onError(new Exception("Cursor returned from query was null."));
                    return;
                }
                emitter.onNext(cursor);
                cursor.close();
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
    public Observable<Uri> getInsertObservable(Joke joke) {
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
}
