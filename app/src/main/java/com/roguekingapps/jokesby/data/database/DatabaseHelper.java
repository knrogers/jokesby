package com.roguekingapps.jokesby.data.database;

import android.database.Cursor;
import android.net.Uri;

import com.roguekingapps.jokesby.data.network.model.Joke;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;

public interface DatabaseHelper {

    Observable<Cursor> getQueryObservable(String apiId);

    Observable<Cursor> getQueryAllFavouritesObservable();

    Observable<Integer> getDeleteObservable(String apiId);

    Observable<Uri> getInsertObservable(Joke joke);
}
