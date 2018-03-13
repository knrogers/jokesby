package com.roguekingapps.jokesby.ui.main.fragment;

import com.roguekingapps.jokesby.data.model.Joke;

import java.util.List;

public interface ListFragmentListener {

    JokeFragment getJokeFragment(String jokeFragmentTag);

    void loadJokes();

    void setJokes(List<Joke> jokes, String jokeFragmentTag);

    void showDetailActivity(Joke joke);
}
