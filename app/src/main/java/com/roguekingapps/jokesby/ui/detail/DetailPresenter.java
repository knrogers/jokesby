package com.roguekingapps.jokesby.ui.detail;

import com.roguekingapps.jokesby.data.model.Joke;
import com.roguekingapps.jokesby.ui.common.BasePresenter;

public interface DetailPresenter extends BasePresenter {

    void onStartLoad();

    void onPostLoad();

    void queryFavourite(String apiId);

    void updateFavourite(Joke joke);

    void queryRated(String apiId);

    void updateRated(Joke joke);

    void checkRating();

    void checkRating(String rating);

    void onPostUpdateFavourite(boolean favourite);
}
