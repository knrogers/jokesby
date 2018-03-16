package com.roguekingapps.jokesby.ui.bottomnavigation;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.roguekingapps.jokesby.R;

import javax.inject.Inject;

/**
 * Handles selection events on bottom navigation items.
 */
public class BottomNavigationItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

    private OnNavigationItemSelectedCallback callback;

    @Inject
    BottomNavigationItemSelectedListener(OnNavigationItemSelectedCallback callback) {
        this.callback = callback;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hot:
                callback.updateListFragment(R.string.hot, R.string.hot_joke_fragment_tag);
                break;
            case R.id.action_random:
                callback.updateListFragment(R.string.random, R.string.random_joke_fragment_tag);
                break;
            case R.id.action_favourites:
                callback.updateListFragment(R.string.favourites, R.string.favourite_joke_fragment_tag);
                break;
            case R.id.action_rated:
                callback.updateListFragment(R.string.rated, R.string.rated_joke_fragment_tag);
                break;
        }
        return true;
    }

    public interface OnNavigationItemSelectedCallback {
        void updateListFragment(int listFragmentTagId, int jokeFragmentTagId);
    }
}
