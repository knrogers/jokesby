package com.roguekingapps.jokesby.data.network;

import com.roguekingapps.jokesby.data.model.JokeContainer;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

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
    public Observable<JokeContainer> getJokeContainerObservable() {
        return jokeApi.loadJokes("11d", "12d");
    }
}
