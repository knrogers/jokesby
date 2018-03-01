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
 * Handles selection events on bottom navigation items.
 */
public class BottomNavigationItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private OnNavigationItemSelectedCallback callback;

    @Inject
    BottomNavigationItemSelectedListener(
            @ActivityContext Context context,
            OnNavigationItemSelectedCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        JokeListFragment listFragment;
        switch (item.getItemId()) {
            case R.id.action_random:
                listFragment = JokeListFragment.newInstance();
                callback.updateListFragment(listFragment, context.getString(R.string.random));
                break;
        }
        return true;
    }

    public interface OnNavigationItemSelectedCallback {
        void updateListFragment(JokeListFragment listFragment, String listFragmentTag);
    }
}
