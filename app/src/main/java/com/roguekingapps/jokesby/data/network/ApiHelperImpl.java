package com.roguekingapps.jokesby.data.network;

import com.roguekingapps.jokesby.data.model.RedditRoot;
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

    private RedditApi redditApi;
    private PushShiftApi pushShiftApi;
    private Random random;

    @Inject
    ApiHelperImpl(RedditApi redditApi, PushShiftApi pushShiftApi, Random random) {
        this.redditApi = redditApi;
        this.pushShiftApi = pushShiftApi;
        this.random = random;
    }

    @Override
    public Observable<RedditRoot> getJokeObservableFromReddit(String after) {
        return redditApi.loadHotJokes(after);
    }

    @Override
    public Observable<JokeContainer> getJokeObservableFromPushShift() {
        String[] days = getDays();
        return pushShiftApi.loadJokes(days[0] + "d", days[1] + "d");
    }

    private String[] getDays() {
        int randomNum = random.nextInt((2000 - 1) + 1) + 1;
        int minRandomNum = randomNum - 1;
        return new String[] {String.valueOf(minRandomNum), String.valueOf(randomNum)};
    }
}
