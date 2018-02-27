package com.roguekingapps.jokesby.ui.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.roguekingapps.jokesby.JokesbyApplication;
import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.network.model.Joke;
import com.roguekingapps.jokesby.databinding.ActivityDetailBinding;
import com.roguekingapps.jokesby.di.component.DaggerDetailActivityComponent;
import com.roguekingapps.jokesby.di.component.DetailActivityComponent;
import com.roguekingapps.jokesby.di.module.DetailActivityModule;

import javax.inject.Inject;

public class DetailActivity extends AppCompatActivity implements DetailView {

    private DetailActivityComponent activityComponent;
    private Joke joke;

    @Inject
    DetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        getActivityComponent().inject(this);

        setSupportActionBar(binding.detailToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.joke))) {
            joke = intent.getParcelableExtra(getString(R.string.joke));
        }

        if (joke != null) {
            binding.detailTextViewTitle.setText(joke.getTitle());
        }
    }

    public DetailActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerDetailActivityComponent.builder()
                    .detailActivityModule(new DetailActivityModule(this))
                    .applicationComponent(JokesbyApplication.get(this).getComponent())
                    .build();
        }
        return activityComponent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_favourite:
                if (joke != null) {
                    presenter.update(joke);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateFavouriteIcon(boolean favourite) {
        Log.i(DetailActivity.class.getSimpleName(), "update favourite icon");
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
