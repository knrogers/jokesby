package com.roguekingapps.jokesby.data.network;

import android.support.annotation.NonNull;

import com.roguekingapps.jokesby.data.network.model.JokeContainer;
import com.roguekingapps.jokesby.ui.main.MainPresenter;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void loadJokes(final MainPresenter presenter) {
        Call<JokeContainer> call = jokeApi.loadJokes("11d", "12d");
        call.enqueue(new Callback<JokeContainer>() {
            @Override
            public void onResponse(@NonNull Call<JokeContainer> call, @NonNull Response<JokeContainer> response) {
                if(response.isSuccessful()) {
                    presenter.showJokes();
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JokeContainer> call, @NonNull Throwable t) {

            }
        });
    }
}
