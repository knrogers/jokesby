package com.roguekingapps.jokesby.data.database;

import android.net.Uri;

import com.roguekingapps.jokesby.data.network.model.Joke;

import io.reactivex.Observer;
import io.reactivex.functions.Consumer;

public interface DatabaseHelper {

    void deleteJoke(Consumer<Integer> deleteFavouriteConsumer, String apiId);

    void insertJoke(Observer<Uri> insertFavouriteObserver, Joke joke);
}
