package com.roguekingapps.jokesby.ui.main.fragment;

import com.roguekingapps.jokesby.data.model.Joke;

public interface ListFragmentListener {

    void loadJokes();

    void showDetailActivity(Joke joke);
}
