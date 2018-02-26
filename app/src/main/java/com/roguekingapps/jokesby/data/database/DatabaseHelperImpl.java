package com.roguekingapps.jokesby.data.database;

import android.content.Context;
import android.net.Uri;

import com.roguekingapps.jokesby.data.network.model.Joke;
import com.roguekingapps.jokesby.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Deletes a {@link Joke} from the favourites database.
 */
@Singleton
public class DatabaseHelperImpl implements DatabaseHelper {

    private Context context;

    @Inject
    DatabaseHelperImpl(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public void deleteJoke(Consumer<Integer> rowsDeletedConsumer, String apiId) {
        Uri uri = JokeContract.JokeEntry.CONTENT_URI
                .buildUpon()
                .appendPath(JokeContract.JokeEntry.COLUMN_API_ID)
                .appendPath(apiId)
                .build();

        Observable<Integer> observable = Observable.just(context.getContentResolver().delete(uri, null, null));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowsDeletedConsumer);
    }
}
