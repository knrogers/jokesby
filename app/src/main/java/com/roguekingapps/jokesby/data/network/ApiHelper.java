package com.roguekingapps.jokesby.data.network;

import com.roguekingapps.jokesby.data.model.JokeContainer;

import io.reactivex.Observable;

public interface ApiHelper {

    Observable<JokeContainer> getJokeContainerObservable();
}
