package com.roguekingapps.jokesby.ui.bottomnavigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.di.ActivityContext;
import com.roguekingapps.jokesby.ui.main.fragment.JokeListFragment;

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
        JokeListFragment listFragment;
        listFragment = JokeListFragment.newInstance();
        switch (item.getItemId()) {
            case R.id.action_random:
                callback.updateCurrentFragment(
                        listFragment,
                        context.getString(R.string.random));
                break;
            case R.id.action_favourites:
                callback.updateCurrentFragment(
                        listFragment,
                        context.getString(R.string.favourites));
                break;
            case R.id.action_rated:
                callback.updateCurrentFragment(
                        listFragment,
                        context.getString(R.string.rated));
                break;
        }
    }

    public interface OnNavigationItemReselectedCallback {
        void updateCurrentFragment(JokeListFragment listFragment, String listFragmentTag);
    }
}
