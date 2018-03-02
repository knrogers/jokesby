package com.roguekingapps.jokesby.data;

import com.roguekingapps.jokesby.data.network.model.Joke;
import com.roguekingapps.jokesby.ui.detail.DetailPresenter;
import com.roguekingapps.jokesby.ui.main.MainPresenter;

public interface DataManager {

    void loadFromApi(MainPresenter mainPresenter);

    void loadFromFavourites(MainPresenter mainPresenter);

    void query(DetailPresenter detailPresenter, String apiId);

    void updateJoke(DetailPresenter detailPresenter, Joke joke);
}
