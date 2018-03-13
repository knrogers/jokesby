package com.roguekingapps.jokesby.di.component;

import com.roguekingapps.jokesby.di.module.ListFragmentModule;
import com.roguekingapps.jokesby.di.PerActivity;
import com.roguekingapps.jokesby.ui.main.fragment.ListFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ListFragmentModule.class)
public interface ListFragmentComponent {

    void inject(ListFragment listFragment);
}
