package com.roguekingapps.jokesby.data.network;

import com.roguekingapps.jokesby.data.model.JokeContainer;

import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Accesses the API and loads jokes.
 */
@Singleton
public class ApiHelperImpl implements ApiHelper {

    private JokeApi jokeApi;
    private Random random;

    @Inject
    ApiHelperImpl(JokeApi jokeApi, Random random) {
        this.jokeApi = jokeApi;
        this.random = random;
    }

    @Override
    public Observable<JokeContainer> getJokeContainerObservable() {
        String[] days = getDays();
        return jokeApi.loadJokes(days[0] + "d", days[1] + "d");
    }

    private String[] getDays() {
        int randomNum = random.nextInt((2000 - 1) + 1) + 1;
        int minRandomNum = randomNum - 1;
        return new String[] {String.valueOf(minRandomNum), String.valueOf(randomNum)};
    }
}
