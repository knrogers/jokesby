package com.roguekingapps.jokesby.data.database;

import io.reactivex.functions.Consumer;

public interface DatabaseHelper {

    void deleteJoke(Consumer<Integer> rowsDeletedConsumer, String apiId);
}
