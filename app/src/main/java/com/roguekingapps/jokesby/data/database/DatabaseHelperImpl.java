package com.roguekingapps.jokesby.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.database.JokeContract.JokeEntry;
import com.roguekingapps.jokesby.data.network.model.Joke;
import com.roguekingapps.jokesby.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
    public void query(Observer<Cursor> queryFavouriteObserver, final String apiId) {
        Observable<Cursor> observable = Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> emitter) throws Exception {
                Cursor cursor = context.getContentResolver().query(
                        JokeEntry.CONTENT_URI, null,
                        JokeEntry.COLUMN_API_ID + context.getString(R.string.parameter_placeholder),
                        new String[]{apiId}, null);
                if (cursor == null) {
                    emitter.onError(new Exception("Cursor returned from query was null."));
                    return;
                }
                emitter.onNext(cursor);
                cursor.close();
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(queryFavouriteObserver);
    }

    @Override
    public void queryAll(Observer<Cursor> queryAllFavouritesObserver) {
        Observable<Cursor> observable = Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> emitter) throws Exception {
                Cursor cursor = context.getContentResolver()
                        .query(JokeEntry.CONTENT_URI, null, null, null, null);
                if (cursor == null) {
                    emitter.onError(new Exception("Cursor returned from query was null."));
                    return;
                }
                emitter.onNext(cursor);
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(queryAllFavouritesObserver);
    }

    @Override
    public void deleteJoke(Consumer<Integer> deleteFavouriteConsumer, String apiId) {
        Observable<Integer> observable =
                Observable.just(context.getContentResolver().delete(
                        JokeEntry.CONTENT_URI,
                        JokeEntry.COLUMN_API_ID,
                        new String[]{apiId}));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deleteFavouriteConsumer);
    }

    @Override
    public void insertJoke(Observer<Uri> insertFavouriteObserver, Joke joke) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(JokeEntry.COLUMN_API_ID, joke.getId());
        contentValues.put(JokeEntry.COLUMN_TITLE, joke.getTitle());
        contentValues.put(JokeEntry.COLUMN_BODY, joke.getBody());
        contentValues.put(JokeEntry.COLUMN_USER, joke.getUser());
        contentValues.put(JokeEntry.COLUMN_URL, joke.getUrl());

        Observable<Uri> observable = Observable.create(new ObservableOnSubscribe<Uri>() {
            @Override
            public void subscribe(ObservableEmitter<Uri> emitter) throws Exception {
                Uri uri = context.getContentResolver().insert(JokeEntry.CONTENT_URI, contentValues);
                if (uri == null) {
                    emitter.onError(new Exception("Uri returned from insert was null."));
                    return;
                }
                emitter.onNext(uri);
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(insertFavouriteObserver);
    }
}
