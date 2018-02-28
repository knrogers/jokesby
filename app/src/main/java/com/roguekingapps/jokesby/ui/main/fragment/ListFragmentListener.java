package com.roguekingapps.jokesby.ui.main.fragment;

import com.roguekingapps.jokesby.data.network.model.Joke;

public interface ListFragmentListener {

    void loadJokes();

    void showDetailActivity(Joke joke);
}
