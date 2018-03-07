package com.roguekingapps.jokesby.ui.detail;

import com.roguekingapps.jokesby.data.model.Joke;
import com.roguekingapps.jokesby.ui.common.BasePresenter;

public interface DetailPresenter extends BasePresenter {

    void query(String apiId);

    void update(Joke joke);

    void updateFavouriteIcon(boolean favourite);
}
