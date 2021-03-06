package com.roguekingapps.jokesby.di.component;

import com.roguekingapps.jokesby.di.module.MainActivityModule;
import com.roguekingapps.jokesby.ui.main.MainActivity;
import com.roguekingapps.jokesby.di.PerActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = MainActivityModule.class)
public interface MainActivityComponent {

    void inject(MainActivity activity);
}
