package com.roguekingapps.jokesby.data.network;

import android.support.annotation.NonNull;

import com.roguekingapps.jokesby.data.network.model.JokeContainer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Receives and processes callback from API response.
 */
public class ApiResponse implements Callback<JokeContainer> {

    @Override
    public void onResponse(@NonNull Call<JokeContainer> call, @NonNull Response<JokeContainer> response) {
        if(response.isSuccessful()) {

        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(@NonNull Call<JokeContainer> call, @NonNull Throwable t) {

    }
}
