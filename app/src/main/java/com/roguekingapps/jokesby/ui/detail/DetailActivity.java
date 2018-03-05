package com.roguekingapps.jokesby.ui.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private Menu menu;
    private int drawableId = -1;

    @Inject
    DetailPresenter presenter;

    @Inject
    Typeface robotoMedium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        getActivityComponent().inject(this);

        setSupportActionBar(binding.layoutAppBar.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.joke))) {
            joke = intent.getParcelableExtra(getString(R.string.joke));
        }

        setUpViews(binding);
    }

    private void setUpViews(final ActivityDetailBinding binding) {
        if (joke != null) {
            presenter.query(joke.getId());
            binding.detailTextViewTitle.setTypeface(robotoMedium);
            binding.detailTextViewTitle.setText(joke.getTitle());

            binding.detailTextViewBody.setTypeface(robotoMedium);
            binding.detailTextViewBody.setText(joke.getBody());

            binding.detailTextViewSubmittedBy.setTypeface(robotoMedium);
            Spanned submittedBy = fromHtml(getString(R.string.submitted_by) +
                    "<a href=\"" + joke.getUrl() + "\">/u/" + joke.getUser() + "</a>");
            binding.detailTextViewSubmittedBy.setText(submittedBy);
            binding.detailTextViewSubmittedBy.setMovementMethod(LinkMovementMethod.getInstance());

            binding.detailFabShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String text = joke.getTitle()
                            + "\n\n" + joke.getBody()
                            + getString(R.string.shared_via_jokesby);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
                }
            });
        }

        binding.detailScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int titleViewHeight = binding.detailTextViewTitle.getMeasuredHeight();
                float alpha = 1f - (scrollY / (titleViewHeight * 0.75f));

                if (alpha <= 1) {
                    binding.detailFabShare.setAlpha(alpha);
                }

            }
        });
    }

    @SuppressWarnings("deprecation")
    private Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
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
        this.menu = menu;
        if (drawableId != -1) {
            updateFavouriteIcon(drawableId);
        }
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
        if (favourite) {
            updateFavouriteIcon(R.drawable.ic_favorite_black);
        } else {
            updateFavouriteIcon(R.drawable.ic_favorite_border_black);
        }
    }

    private void updateFavouriteIcon(int drawableId) {
        // If user rotates device while database is being queried, menu could be null.
        // If it is, invalidate the menu.
        if (menu == null) {
            this.drawableId = drawableId;
            invalidateOptionsMenu();
        } else {
            MenuItem menuItem = menu.findItem(R.id.action_favourite);
            menuItem.setIcon(ContextCompat.getDrawable(this, drawableId));
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
