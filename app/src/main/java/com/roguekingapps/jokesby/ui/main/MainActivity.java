package com.roguekingapps.jokesby.ui.main;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.roguekingapps.jokesby.JokesbyApplication;
import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.di.component.ActivityComponent;
import com.roguekingapps.jokesby.di.component.DaggerActivityComponent;
import com.roguekingapps.jokesby.di.module.ActivityModule;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainView {

    private ActivityComponent activityComponent;

    @Inject
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActivityComponent().inject(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenter.loadJokes();
    }

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(JokesbyApplication.get(this).getComponent())
                    .build();
        }
        return activityComponent;
    }

    @Override
    public void showJokes() {
        Log.i(MainActivity.class.getSimpleName(), "show jokes!");
    }
}
