package com.roguekingapps.jokesby.data;

import com.roguekingapps.jokesby.ui.detail.DetailPresenter;
import com.roguekingapps.jokesby.ui.main.MainPresenter;

public interface DataManager {

    void loadJokes(MainPresenter mainPresenter);
    void updateJoke(DetailPresenter detailPresenter, String apiId);
}
