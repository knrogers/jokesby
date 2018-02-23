package com.roguekingapps.jokesby.ui.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.network.model.Joke;
import com.roguekingapps.jokesby.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent intent = getIntent();
        Joke joke = null;
        if (intent != null && intent.hasExtra(getString(R.string.joke))) {
            joke = intent.getParcelableExtra(getString(R.string.joke));
        }

        if (joke != null) {
            binding.textViewJokeTitle.setText(joke.getTitle());
        }
    }
}
