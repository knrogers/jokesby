package com.roguekingapps.jokesby.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.roguekingapps.jokesby.JokesbyApplication;
import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.network.model.Joke;
import com.roguekingapps.jokesby.databinding.ActivityMainBinding;
import com.roguekingapps.jokesby.di.component.ActivityComponent;
import com.roguekingapps.jokesby.di.component.DaggerActivityComponent;
import com.roguekingapps.jokesby.di.module.ActivityModule;
import com.roguekingapps.jokesby.ui.adapter.ListAdapter;
import com.roguekingapps.jokesby.ui.detail.DetailActivity;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainView, ListAdapter.JokeOnClickHandler {

    private ActivityComponent activityComponent;

    @Inject
    ListAdapter adapter;

    @Inject
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getActivityComponent().inject(this);

        binding.mainRecyclerView.setAdapter(adapter);
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
    public void showJokes(List<Joke> jokes) {
        if (jokes == null) {
            showToast(getString(R.string.jokes_not_loaded));
        } else {
            adapter.setJokes(jokes);
            adapter.notifyDataSetChanged();
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(Joke joke) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(getString(R.string.joke), joke);
        startActivity(intent);
    }
}
