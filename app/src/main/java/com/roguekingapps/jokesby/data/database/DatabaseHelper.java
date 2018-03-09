package com.roguekingapps.jokesby.data.database;

import android.database.Cursor;
import android.net.Uri;

import com.roguekingapps.jokesby.data.model.Joke;

import io.reactivex.Observable;

public interface DatabaseHelper {

    Observable<Cursor> getQueryObservable(Uri contentUri, String columnApiId, String apiId);

    Observable<Cursor> getQueryAllObservable(Uri contentUri);

    Observable<Integer> getDeleteObservable(Uri contentUri, String columnApiId, String apiId);

    Observable<Integer> getUpdateObservable(Joke joke);

    Observable<Uri> getInsertFavouriteObservable(Joke joke);

    Observable<Uri> getInsertRatedObservable(Joke joke);
}
