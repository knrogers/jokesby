package com.roguekingapps.jokesby.data.network;

import com.roguekingapps.jokesby.data.network.model.JokeContainer;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

/**
 * Accesses the API and loads jokes.
 */
@Singleton
public class ApiHelper implements BaseApiHelper {

    private ApiResponse apiResponse;
    private JokeApi jokeApi;

    @Inject
    ApiHelper(ApiResponse apiResponse, JokeApi jokeApi) {
        this.apiResponse = apiResponse;
        this.jokeApi = jokeApi;
    }

    @Override
    public void loadJokes() {
        Call<JokeContainer> call = jokeApi.loadJokes("11d", "12d");
        call.enqueue(apiResponse);
    }
}
