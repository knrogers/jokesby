package com.roguekingapps.jokesby.di.module;

import android.graphics.Typeface;

import com.roguekingapps.jokesby.ui.adapter.ListAdapter;
import com.roguekingapps.jokesby.ui.main.fragment.JokeListFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class JokeListFragmentModule {

    private JokeListFragment jokeListFragment;

    public JokeListFragmentModule(JokeListFragment jokeListFragment) {
        this.jokeListFragment = jokeListFragment;
    }

    @Provides
    ListAdapter provideListAdapter(Typeface robotoMedium) {
        return new ListAdapter(jokeListFragment.getContext(), jokeListFragment, jokeListFragment, robotoMedium);
    }
}
