package com.roguekingapps.jokesby.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roguekingapps.jokesby.JokesbyApplication;
import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.network.model.Joke;
import com.roguekingapps.jokesby.di.component.DaggerMainActivityComponent;
import com.roguekingapps.jokesby.di.component.MainActivityComponent;
import com.roguekingapps.jokesby.di.module.MainActivityModule;
import com.roguekingapps.jokesby.ui.detail.DetailActivity;
import com.roguekingapps.jokesby.ui.main.fragment.JokeListFragment;
import com.roguekingapps.jokesby.ui.main.fragment.ListFragmentListener;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements
        MainView,
        ListFragmentListener {

    private MainActivityComponent activityComponent;
    private JokeListFragment jokeListFragment;

    @Inject
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        getActivityComponent().inject(this);

        if (jokeListFragment == null) {
            jokeListFragment = JokeListFragment.newInstance();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_container, jokeListFragment, "jokeListFragment")
                .commit();
    }

    @Override
    public void loadJokes() {
        presenter.loadJokes();
    }

    @Override
    public void showDetailActivity(Joke joke) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(getString(R.string.joke), joke);
        startActivity(intent);
    }

    @Override
    public void showJokes(List<Joke> jokes) {
        jokeListFragment.showJokes(jokes);
    }

    public MainActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerMainActivityComponent.builder()
                    .mainActivityModule(new MainActivityModule(this))
                    .applicationComponent(JokesbyApplication.get(this).getComponent())
                    .build();
        }
        return activityComponent;
    }
}
