package com.roguekingapps.jokesby.di.component;

import com.roguekingapps.jokesby.di.module.ActivityModule;
import com.roguekingapps.jokesby.ui.main.MainActivity;
import com.roguekingapps.jokesby.di.PerActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);
}
