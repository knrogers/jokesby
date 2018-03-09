package com.roguekingapps.jokesby.ui.detail;

import com.roguekingapps.jokesby.ui.common.BaseView;

public interface DetailView extends BaseView {

    void onPostUpdateFavourite(boolean favourite);

    void onPostUpdateRating();

    void checkRating(String rating);

    void showError(String message);
}
