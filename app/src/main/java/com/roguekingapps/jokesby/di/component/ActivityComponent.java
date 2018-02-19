package com.roguekingapps.jokesby.di.component;

import com.roguekingapps.jokesby.ui.MainActivity;
import com.roguekingapps.jokesby.di.PerActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class)
public interface ActivityComponent {

    void inject(MainActivity activity);
}
