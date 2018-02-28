package com.roguekingapps.jokesby.di.component;

import com.roguekingapps.jokesby.di.module.JokeListFragmentModule;
import com.roguekingapps.jokesby.di.PerActivity;
import com.roguekingapps.jokesby.ui.main.fragment.JokeListFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = JokeListFragmentModule.class)
public interface JokeListFragmentComponent {

    void inject(JokeListFragment jokeListFragment);
}
