package com.roguekingapps.jokesby.data.network;

import com.roguekingapps.jokesby.data.network.model.JokeContainer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JokeApi {

    @GET("reddit/search/submission/?subreddit=jokes&stickied=false&size=500&score=>50&sort=desc")
    Call<JokeContainer> loadJokes(@Query("before") String daysBefore, @Query("after") String daysAfter);
}
