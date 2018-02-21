package com.roguekingapps.jokesby.data.network;

import com.roguekingapps.jokesby.data.network.model.JokeContainer;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Accesses the API and loads jokes.
 */
@Singleton
public class ApiHelperImpl implements ApiHelper {

    private JokeApi jokeApi;

    @Inject
    ApiHelperImpl(JokeApi jokeApi) {
        this.jokeApi = jokeApi;
    }

    @Override
    public void loadJokes(Consumer<JokeContainer> jokeConsumer) {
        Observable<JokeContainer> observable = jokeApi.loadJokes("11d", "12d");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jokeConsumer);
    }
}
