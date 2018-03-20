package com.roguekingapps.jokesby.ui.detail;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.roguekingapps.jokesby.JokesbyApplication;
import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.model.Joke;
import com.roguekingapps.jokesby.databinding.ActivityDetailBinding;
import com.roguekingapps.jokesby.di.component.DaggerDetailActivityComponent;
import com.roguekingapps.jokesby.di.component.DetailActivityComponent;
import com.roguekingapps.jokesby.di.module.DetailActivityModule;
import com.roguekingapps.jokesby.ui.main.fragment.JokeFragment;
import com.roguekingapps.jokesby.ui.url.UrlSpanNoUnderLine;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DetailActivity extends AppCompatActivity implements DetailView {

    private DetailActivityComponent activityComponent;
    private ActivityDetailBinding binding;
    private Joke joke;
    private AppCompatImageButton ratingButton;
    private boolean fromFavouriteList;
    private int result = Activity.RESULT_CANCELED;

    @Inject
    DetailPresenter presenter;

    @Inject
    Typeface robotoMedium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        getActivityComponent().inject(this);

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        setSupportActionBar(binding.detailAppBar.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        JokeFragment jokeFragment = getJokeFragment();
        if (jokeFragment == null) {
            setUpJokeFragment();
        }

        Intent intent = getIntent();
        if (intent != null) {
            if (joke == null && intent.hasExtra(getString(R.string.joke))) {
                joke = intent.getParcelableExtra(getString(R.string.joke));
                addJokeTo(jokeFragment);
            }
            fromFavouriteList = intent.hasExtra(getString(R.string.favourite));
        }

        if (joke != null) {
            updateFavourite();
            setUpRatingButtons();
            updateRating();
            updateTextViews();
            setFabOnClickListener();
        }
        setScrollViewListener();
    }

    private JokeFragment getJokeFragment() {
        String jokeFragmentTag = getString(R.string.detail_joke_fragment_tag);
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (JokeFragment) fragmentManager
                .findFragmentByTag(jokeFragmentTag);
    }

    private void setUpJokeFragment() {
        JokeFragment jokeFragment = JokeFragment.newInstance();

        String jokeFragmentTag = getString(R.string.detail_joke_fragment_tag);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(jokeFragment, jokeFragmentTag)
                .commit();
    }

    private void addJokeTo(JokeFragment jokeFragment) {
        if (jokeFragment != null &&
                (jokeFragment.getJokes() == null || jokeFragment.getJokes().isEmpty())) {
            List<Joke> jokes = new ArrayList<>();
            jokes.add(joke);
            jokeFragment.setJokes(jokes);
        }
    }

    private void updateFavourite() {
        if (!fromFavouriteList) {
            presenter.queryFavourite(joke.getId());
        } else {
            onPostUpdateFavourite(true);
        }
    }

    private void updateRating() {
        if (joke.getRating() == null) {
            presenter.queryRated(joke.getId());
        } else {
            checkRating(joke.getRating());
        }
    }

    private void updateTextViews() {
        binding.detailTextViewTitle.setTypeface(robotoMedium);
        binding.detailTextViewTitle.setText(joke.getTitle());

        binding.detailTextViewBody.setTypeface(robotoMedium);
        binding.detailTextViewBody.setText(joke.getBody());

        binding.detailTextViewSubmittedBy.setTypeface(robotoMedium);
        Spannable submittedBy = fromHtml(getString(R.string.submitted_by) +
                "<a href=\"" + joke.getUrl() + "\">/u/" + joke.getUser() + "</a>");
        binding.detailTextViewSubmittedBy.setText(submittedBy);
        binding.detailTextViewSubmittedBy.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setFabOnClickListener() {
        binding.detailFabFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (joke != null) {
                    presenter.updateFavourite(joke);
                }
            }
        });
    }

    private void setUpRatingButtons() {
        LinearLayout ratingLayout = binding.detailRatingBar.ratingBarLayout;
        ColorStateList gray =
                ContextCompat.getColorStateList(this, R.color.gray_semi_transparent_88);
        for (int i = 0; i < ratingLayout.getChildCount(); i++) {
            if (ratingLayout.getChildAt(i) instanceof Space) {
                continue;
            }
            AppCompatImageButton button = (AppCompatImageButton) ratingLayout.getChildAt(i);
            ViewCompat.setBackgroundTintList(button, gray);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatImageButton clickedButton = (AppCompatImageButton) view;
                    updateRatingButton(clickedButton);
                }
            });
        }
    }

    private void updateRatingButton(AppCompatImageButton buttonToUpdate) {
        LinearLayout ratingLayout = binding.detailRatingBar.ratingBarLayout;
        ColorStateList gray =
                ContextCompat.getColorStateList(this, R.color.gray_semi_transparent_88);
        ColorStateList blue =
                ContextCompat.getColorStateList(DetailActivity.this, R.color.accent);
        if (ratingButton != null && ratingButton == buttonToUpdate) {
            ratingButton = null;
            joke.setRating(null);
            ViewCompat.setBackgroundTintList(buttonToUpdate, gray);
            presenter.deleteRated(joke.getId());
        } else {
            ratingButton = buttonToUpdate;
            joke.setRating(ratingButton.getTag().toString());
            for (int i = 0; i < ratingLayout.getChildCount(); i++) {
                if (ratingLayout.getChildAt(i) instanceof Space) {
                    continue;
                }
                AppCompatImageButton button = (AppCompatImageButton) ratingLayout.getChildAt(i);
                if (button == buttonToUpdate) {
                    ViewCompat.setBackgroundTintList(button, blue);
                } else {
                    ViewCompat.setBackgroundTintList(button, gray);
                }
            }
            presenter.updateRated(joke);
        }
    }


    private void setScrollViewListener() {
        binding.detailScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int titleViewHeight = binding.detailTextViewTitle.getMeasuredHeight();
                float alpha = 1f - (scrollY / (titleViewHeight * 0.75f));

                if (alpha <= 1 && alpha >= 0) {
                    binding.detailFabFavourite.setAlpha(alpha);
                    if (binding.detailFabFavourite.getVisibility() != View.VISIBLE
                            && scrollY <= (titleViewHeight * 0.75f)) {
                        binding.detailFabFavourite.setVisibility(View.VISIBLE);
                        binding.detailFabFavourite.setClickable(true);
                        binding.detailFabFavourite.setFocusable(true);
                    }
                } else if (alpha < 0) {
                    binding.detailFabFavourite.setAlpha(-1);
                    if (binding.detailFabFavourite.getVisibility() != View.GONE
                            && scrollY >= (titleViewHeight * 0.75f)) {
                        binding.detailFabFavourite.setVisibility(View.GONE);
                        binding.detailFabFavourite.setClickable(false);
                        binding.detailFabFavourite.setFocusable(false);
                    }
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    private Spannable fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }

        Spannable spannable = new SpannableString(result);
        URLSpan[] spans = spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = spannable.getSpanStart(span);
            int end = spannable.getSpanEnd(span);
            spannable.removeSpan(span);
            span = new UrlSpanNoUnderLine(span.getURL());
            spannable.setSpan(span, start, end, 0);
        }
        return spannable;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.result_code), result);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(getString(R.string.result_code))) {
            result = savedInstanceState.getInt(getString(R.string.result_code));
        }
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
            case R.id.action_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String text = joke.getTitle()
                        + "\n\n" + joke.getBody()
                        + getString(R.string.shared_via_jokesby);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartLoad() {
        if (binding.detailScrollView.getVisibility() != View.GONE) {
            binding.detailScrollView.setVisibility(View.GONE);
        }

        if (binding.detailFabFavourite.getVisibility() != View.GONE) {
            binding.detailFabFavourite.setVisibility(View.GONE);
        }

        if (binding.detailProgressBar.getVisibility() != View.VISIBLE) {
            binding.detailProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPostLoad() {
        if (binding.detailScrollView.getVisibility() != View.VISIBLE) {
            binding.detailScrollView.setVisibility(View.VISIBLE);
        }

        if (binding.detailFabFavourite.getVisibility() != View.VISIBLE) {
            binding.detailFabFavourite.setVisibility(View.VISIBLE);
        }

        if (binding.detailProgressBar.getVisibility() != View.GONE) {
            binding.detailProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPostUpdateFavourite(boolean favourite) {
        result = Activity.RESULT_OK;
        if (favourite) {
            binding.detailFabFavourite.setImageResource(R.drawable.ic_favourite_black);
        } else {
            binding.detailFabFavourite.setImageResource(R.drawable.ic_favourite_border_black);
        }
    }

    @Override
    public void onPostUpdateRating() {
        result = Activity.RESULT_OK;
    }

    @Override
    public void checkRating(String rating) {
        LinearLayout ratingLayout = binding.detailRatingBar.ratingBarLayout;
        ColorStateList blue =
                ContextCompat.getColorStateList(DetailActivity.this, R.color.accent);
        for (int i = 0; i < ratingLayout.getChildCount(); i++) {
            if (ratingLayout.getChildAt(i) instanceof Space) {
                continue;
            }
            AppCompatImageButton ratingButton = (AppCompatImageButton) ratingLayout.getChildAt(i);
            if (rating.equals(ratingButton.getTag().toString())) {
                ViewCompat.setBackgroundTintList(ratingButton, blue);
                this.ratingButton = ratingButton;
                break;
            }
        }
    }

    @Override
    public void finish() {
        setResult(result);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.clearDisposables();
    }

    @Override
    public void showError(String message) {
        if (binding.detailProgressBar.getVisibility() == View.VISIBLE) {
            binding.detailProgressBar.setVisibility(View.GONE);
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
}
