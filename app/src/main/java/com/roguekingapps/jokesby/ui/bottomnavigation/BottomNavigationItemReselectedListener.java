package com.roguekingapps.jokesby.ui.bottomnavigation;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.roguekingapps.jokesby.R;

import javax.inject.Inject;

/**
 * Handles reselection events on bottom navigation items.
 */
public class BottomNavigationItemReselectedListener implements BottomNavigationView.OnNavigationItemReselectedListener {

    private OnNavigationItemReselectedCallback callback;

    @Inject
    BottomNavigationItemReselectedListener(OnNavigationItemReselectedCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hot:
                callback.updateCurrentFragment(R.string.hot, R.string.hot_joke_fragment_tag);
                break;
            case R.id.action_random:
                callback.updateCurrentFragment(R.string.random, R.string.random_joke_fragment_tag);
                break;
            case R.id.action_favourites:
                callback.updateCurrentFragment(R.string.favourites, R.string.favourite_joke_fragment_tag);
                break;
            case R.id.action_rated:
                callback.updateCurrentFragment(R.string.rated, R.string.rated_joke_fragment_tag);
                break;
        }
    }

    public interface OnNavigationItemReselectedCallback {
        void updateCurrentFragment(int listFragmentTagId, int jokeFragmentTagId);
    }
}
