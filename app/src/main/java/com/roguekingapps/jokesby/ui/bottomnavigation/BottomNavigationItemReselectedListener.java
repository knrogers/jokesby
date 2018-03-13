package com.roguekingapps.jokesby.ui.bottomnavigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.di.ActivityContext;
import com.roguekingapps.jokesby.ui.main.fragment.ListFragment;

import javax.inject.Inject;

/**
 * Handles reselection events on bottom navigation items.
 */
public class BottomNavigationItemReselectedListener implements BottomNavigationView.OnNavigationItemReselectedListener {

    private Context context;
    private OnNavigationItemReselectedCallback callback;

    @Inject
    BottomNavigationItemReselectedListener(
            @ActivityContext Context context,
            OnNavigationItemReselectedCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        ListFragment listFragment;
        switch (item.getItemId()) {
            case R.id.action_hot:
                listFragment = ListFragment.newInstance(R.string.hot_joke_fragment_tag);
                callback.updateCurrentFragment(
                        listFragment,
                        context.getString(R.string.hot),
                        R.string.hot_joke_fragment_tag);
                break;
            case R.id.action_random:
                listFragment = ListFragment.newInstance(R.string.random_joke_fragment_tag);
                callback.updateCurrentFragment(
                        listFragment,
                        context.getString(R.string.random),
                        R.string.random_joke_fragment_tag);
                break;
            case R.id.action_favourites:
                listFragment = ListFragment.newInstance(R.string.favourite_joke_fragment_tag);
                callback.updateCurrentFragment(
                        listFragment,
                        context.getString(R.string.favourites),
                        R.string.favourite_joke_fragment_tag);
                break;
            case R.id.action_rated:
                listFragment = ListFragment.newInstance(R.string.rated_joke_fragment_tag);
                callback.updateCurrentFragment(
                        listFragment,
                        context.getString(R.string.rated),
                        R.string.rated_joke_fragment_tag);
                break;
        }
    }

    public interface OnNavigationItemReselectedCallback {
        void updateCurrentFragment(ListFragment listFragment, String listFragmentTag, int jokeFragmentTagId);
    }
}
