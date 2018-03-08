package com.roguekingapps.jokesby.ui.detail;

public interface DetailView {

    void onStartLoad();

    void onPostLoad();

    void onPostUpdateFavourite(boolean favourite);

    void checkRating();

    void checkRating(String rating);

    void showError(String message);
}
