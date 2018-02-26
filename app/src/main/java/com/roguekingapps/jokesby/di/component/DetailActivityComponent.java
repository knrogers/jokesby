package com.roguekingapps.jokesby.di.component;

import com.roguekingapps.jokesby.di.PerActivity;
import com.roguekingapps.jokesby.di.module.DetailActivityModule;
import com.roguekingapps.jokesby.ui.detail.DetailActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = DetailActivityModule.class)
public interface DetailActivityComponent {

    void inject(DetailActivity activity);
}
