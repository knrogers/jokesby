package com.roguekingapps.jokesby.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.roguekingapps.jokesby.ui.main.fragment.DisclaimerDialogFragment;
import com.roguekingapps.jokesby.ui.main.fragment.DisclaimerDialogFragment.OnDisclaimerInteractionListener;
import com.roguekingapps.jokesby.ui.main.fragment.JokeFragment;
import com.roguekingapps.jokesby.ui.main.fragment.ListFragment;
import com.roguekingapps.jokesby.ui.main.fragment.ListFragmentListener;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements
        MainView,
        ListFragmentListener,
        OnNavigationItemSelectedCallback,
        OnNavigationItemReselectedCallback,
        OnDisclaimerInteractionListener {

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

        if (getConsentGiven()) {
            showListFragment();
        } else {
            showDisclaimerFragment();
        }
    }

    private boolean getConsentGiven() {
        SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE);
        String consentGivenKey = getString(R.string.consent_given);
        return sharedPreferences.contains(consentGivenKey)
                && sharedPreferences.getBoolean(consentGivenKey, false);
    }

    private String getListFragmentTag(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            return savedInstanceState.getString
                    (getString(R.string.list_fragment_tag), getString(R.string.hot));
        }
        return getString(R.string.hot);
    }

    private void showListFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ListFragment selectedFragment =
                (ListFragment) fragmentManager.findFragmentByTag(listFragmentTag);
        if (selectedFragment == null) {
            selectedFragment = ListFragment.newInstance(R.string.hot_joke_fragment_tag);
        }
        fragmentManager.beginTransaction()
                .replace(R.id.list_container, selectedFragment, listFragmentTag)
                .commit();
    }

    private void showDisclaimerFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DisclaimerDialogFragment disclaimerFragment = DisclaimerDialogFragment.newInstance();
        disclaimerFragment.setCancelable(false);
        fragmentManager.beginTransaction()
                .add(disclaimerFragment, getString(R.string.content_disclaimer))
                .commit();
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
    public void onStartLoad() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ListFragment selectedFragment =
                (ListFragment) fragmentManager.findFragmentByTag(listFragmentTag);
        if (selectedFragment != null) {
            selectedFragment.onStartLoad();
        }
    }

    @Override
    public void onPostLoad() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ListFragment selectedFragment =
                (ListFragment) fragmentManager.findFragmentByTag(listFragmentTag);
        if (selectedFragment != null) {
            selectedFragment.onPostLoad();
        }
    }

    @Override
    public JokeFragment getJokeFragment(String jokeFragmentTag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Check to see if we have retained the worker fragment.
        JokeFragment jokeFragment = (JokeFragment)
                fragmentManager.findFragmentByTag(jokeFragmentTag);
        // If not retained (or first time running), we need to create it.
        if (jokeFragment == null) {
            jokeFragment = JokeFragment.newInstance();
            // Tell it who it is working with.
            fragmentManager.beginTransaction()
                    .add(jokeFragment, jokeFragmentTag)
                    .commit();
        }
        return jokeFragment;
    }

    @Override
    public void loadJokes() {
        if (listFragmentTag.equals(getString(R.string.hot))) {
            presenter.loadHot();
        } else if (listFragmentTag.equals(getString(R.string.random))) {
            presenter.loadRandom();
        } else if (listFragmentTag.equals(getString(R.string.favourites))) {
            presenter.loadFavourites();
        } else if (listFragmentTag.equals(getString(R.string.rated))) {
            presenter.loadRated();
        }
    }

    @Override
    public void setJokes(List<Joke> jokes, String jokeFragmentTag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        JokeFragment jokeFragment =
                (JokeFragment) fragmentManager.findFragmentByTag(jokeFragmentTag);
        if (jokeFragment != null) {
            jokeFragment.setJokes(jokes);
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
        // If user updated favourites or rating in detail screen.
        if (requestCode == getResources().getInteger(R.integer.request_code)
                && resultCode == Activity.RESULT_OK) {
            // Reload master list to refresh view.
            if (listFragmentTag.equals(getString(R.string.favourites))) {
                presenter.loadFavourites();
            } else if (listFragmentTag.equals(getString(R.string.rated))) {
                presenter.loadRated();
            }
        }
    }

    @Override
    public void showJokesFromApi(List<Joke> jokes) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ListFragment selectedFragment =
                (ListFragment) fragmentManager.findFragmentByTag(listFragmentTag);
        if (selectedFragment != null) {
            selectedFragment.showJokesFromApi(jokes);
        }
    }

    @Override
    public void showJokesFromDatabase(List<Joke> jokes) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ListFragment selectedFragment =
                (ListFragment) fragmentManager.findFragmentByTag(listFragmentTag);
        if (selectedFragment != null) {
            selectedFragment.showJokesFromDatabase(jokes);
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateListFragment(int listFragmentTagId, int jokeFragmentTagId) {
        listFragmentTag = getString(listFragmentTagId);
        ListFragment listFragment = ListFragment.newInstance(jokeFragmentTagId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.list_container, listFragment, listFragmentTag)
                .commit();
    }

    @Override
    public void updateCurrentFragment(int listFragmentTagId, int jokeFragmentTagId) {
        String listFragmentTag = getString(listFragmentTagId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        ListFragment selectedFragment =
                (ListFragment) fragmentManager.findFragmentByTag(listFragmentTag);
        if (selectedFragment.getScrollOffset() > 0) {
            selectedFragment.resetScrollPosition();
        } else {
            presenter.setAfter(null);
            this.listFragmentTag = listFragmentTag;
            JokeFragment jokeFragment = (JokeFragment) fragmentManager
                    .findFragmentByTag(getString(jokeFragmentTagId));
            if (jokeFragment != null) {
                jokeFragment.setJokes(null);
            }
            ListFragment listFragment = ListFragment.newInstance(jokeFragmentTagId);
            fragmentManager.beginTransaction()
                    .replace(R.id.list_container, listFragment, listFragmentTag)
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

    @Override
    public void onSelectContinue() {
        SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE);
        sharedPreferences.edit()
                .putBoolean(getString(R.string.consent_given), true)
                .apply();
        showListFragment();
    }

    @Override
    public void onSelectExit() {
        finish();
    }
}
