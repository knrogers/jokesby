package com.roguekingapps.jokesby.ui.detail;

import com.roguekingapps.jokesby.data.network.model.Joke;

public interface DetailPresenter {

    void query(String apiId);

    void update(Joke joke);

    void updateFavouriteIcon(boolean favourite);

    void showError(String message);
}
