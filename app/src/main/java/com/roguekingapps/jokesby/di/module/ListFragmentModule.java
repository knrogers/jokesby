package com.roguekingapps.jokesby.di.module;

import android.graphics.Typeface;

import com.roguekingapps.jokesby.ui.adapter.ListAdapter;
import com.roguekingapps.jokesby.ui.main.fragment.ListFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class ListFragmentModule {

    private ListFragment listFragment;

    public ListFragmentModule(ListFragment listFragment) {
        this.listFragment = listFragment;
    }

    @Provides
    ListAdapter provideListAdapter(Typeface robotoMedium) {
        return new ListAdapter(listFragment.getContext(), listFragment, listFragment, robotoMedium);
    }
}
