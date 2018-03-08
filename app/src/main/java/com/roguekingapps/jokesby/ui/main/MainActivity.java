package com.roguekingapps.jokesby.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.roguekingapps.jokesby.JokesbyApplication;
import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.model.Joke;
import com.roguekingapps.jokesby.databinding.ActivityMainBinding;
import com.roguekingapps.jokesby.di.component.DaggerMainActivityComponent;
import com.roguekingapps.jokesby.di.component.MainActivityComponent;
import com.roguekingapps.jokesby.di.module.MainActivityModule;
import com.roguekingapps.jokesby.ui.bottomnavigation.BottomNavigationItemReselectedListener;
import com.roguekingapps.jokesby.ui.bottomnavigation.BottomNavigationItemReselectedListener.OnNavigationItemReselectedCallback;
import com.roguekingapps.jokesby.ui.bottomnavigation.BottomNavigationItemSelectedListener;
import com.roguekingapps.jokesby.ui.bottomnavigation.BottomNavigationItemSelectedListener.OnNavigationItemSelectedCallback;
import com.roguekingapps.jokesby.ui.detail.DetailActivity;
import com.roguekingapps.jokesby.ui.main.fragment.JokeListFragment;
import com.roguekingapps.jokesby.ui.main.fragment.ListFragmentListener;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements
        MainView,
        ListFragmentListener,
        OnNavigationItemSelectedCallback,
        OnNavigationItemReselectedCallback {

    private MainActivityComponent activityComponent;
    private String listFragmentTag;

    @Inject
    BottomNavigationItemSelectedListener bottomNavigationItemSelectedListener;

    @Inject
    BottomNavigationItemReselectedListener bottomNavigationItemReselectedListener;

    @Inject
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getActivityComponent().inject(this);

        binding.bottomNavigation
                .setOnNavigationItemSelectedListener(bottomNavigationItemSelectedListener);
        binding.bottomNavigation
                .setOnNavigationItemReselectedListener(bottomNavigationItemReselectedListener);
        listFragmentTag = getListFragmentTag(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        JokeListFragment selectedFragment =
                (JokeListFragment) fragmentManager.findFragmentByTag(listFragmentTag);
        if (selectedFragment == null) {
            selectedFragment = JokeListFragment.newInstance();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.list_container, selectedFragment, listFragmentTag)
                .commit();
    }

    private String getListFragmentTag(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            return savedInstanceState.getString
                    (getString(R.string.list_fragment_tag), getString(R.string.random));
        }
        return getString(R.string.random);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.list_fragment_tag), listFragmentTag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.clearDisposables();
    }

    @Override
    public void loadJokes() {
        if (listFragmentTag.equals(getString(R.string.random))) {
            presenter.loadFromApi();
        } else if (listFragmentTag.equals(getString(R.string.favourites))) {
            presenter.loadFromFavourites();
        }
    }

    @Override
    public void showDetailActivity(Joke joke) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(getString(R.string.joke), joke);
        if (listFragmentTag.equals(getString(R.string.favourites))) {
            intent.putExtra(getString(R.string.favourite), true);
        }
        startActivityForResult(intent, getResources().getInteger(R.integer.request_code));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If user clicked on favourites icon in detail screen.
        if (requestCode == getResources().getInteger(R.integer.request_code)
                && resultCode == Activity.RESULT_OK) {
            // Reload master list to refresh view.
            if (listFragmentTag.equals(getString(R.string.favourites))) {
                presenter.loadFromFavourites();
            }
        }
    }

    @Override
    public void showJokesFromApi(List<Joke> jokes) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        JokeListFragment selectedFragment =
                (JokeListFragment) fragmentManager.findFragmentByTag(listFragmentTag);
        if (selectedFragment != null) {
            selectedFragment.showJokesFromApi(jokes);
        }
    }

    @Override
    public void showJokesFromFavourites(List<Joke> jokes) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        JokeListFragment selectedFragment =
                (JokeListFragment) fragmentManager.findFragmentByTag(listFragmentTag);
        if (selectedFragment != null) {
            selectedFragment.showJokesFromFavourites(jokes);
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateListFragment(JokeListFragment jokeListFragment, String listFragmentTag) {
        this.listFragmentTag = listFragmentTag;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.list_container, jokeListFragment, listFragmentTag)
                .commit();
    }

    @Override
    public void updateCurrentFragment(JokeListFragment jokeListFragment, String listFragmentTag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        JokeListFragment selectedFragment =
                (JokeListFragment) fragmentManager.findFragmentByTag(listFragmentTag);
        if (selectedFragment.getScrollOffset() > 0) {
            selectedFragment.resetScrollPosition();
        } else {
            this.listFragmentTag = listFragmentTag;
            fragmentManager.beginTransaction()
                    .replace(R.id.list_container, jokeListFragment, listFragmentTag)
                    .commit();
        }
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
