package com.roguekingapps.jokesby.data;

import com.roguekingapps.jokesby.data.model.Joke;
import com.roguekingapps.jokesby.ui.detail.DetailPresenter;
import com.roguekingapps.jokesby.ui.main.MainPresenter;

public interface DataManager {

    void loadHot(MainPresenter mainPresenter, String after);

    void loadRandom(MainPresenter mainPresenter);

    void loadFavourites(MainPresenter mainPresenter);

    void loadRated(MainPresenter mainPresenter);

    void queryFavourite(DetailPresenter detailPresenter, String apiId);

    void updateFavourite(DetailPresenter detailPresenter, Joke joke);

    void queryRated(DetailPresenter detailPresenter, String apiId);

    void updateRated(DetailPresenter detailPresenter, Joke joke);

    void deleteRated(DetailPresenter detailPresenter, String apiId);
}
