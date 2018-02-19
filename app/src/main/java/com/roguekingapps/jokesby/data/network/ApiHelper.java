package com.roguekingapps.jokesby.data.network;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses the API and loads jokes.
 */
@Singleton
public class ApiHelper implements BaseApiHelper {

    private Gson gson;

    @Inject
    ApiHelper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void loadJokes() {
    }
}
