package com.roguekingapps.jokesby.data.network;

import com.roguekingapps.jokesby.data.model.JokeContainer;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JokeApi {

    @GET("reddit/search/submission/?subreddit=jokes&stickied=false&size=500&score=>50&sort=desc")
    Observable<JokeContainer> loadJokes(@Query("before") String daysBefore, @Query("after") String daysAfter);
}
