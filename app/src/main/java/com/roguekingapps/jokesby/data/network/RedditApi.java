package com.roguekingapps.jokesby.data.network;

import com.roguekingapps.jokesby.data.model.RedditRoot;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RedditApi {

    @GET("r/Jokes/hot.json?limit=50&count=50")
    Observable<RedditRoot> loadHotJokes(@Query("after") String after);
}
