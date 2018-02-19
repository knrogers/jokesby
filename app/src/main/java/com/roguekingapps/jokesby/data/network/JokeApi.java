package com.roguekingapps.jokesby.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JokeApi {

    @GET("reddit/search/submission/?subreddit=jokes&stickied=false&size=500&score=>1000&sort=desc")
    Call<JokeContainer> loadJokes(@Query("before") String daysBefore, @Query("after") String daysAfter);
}
