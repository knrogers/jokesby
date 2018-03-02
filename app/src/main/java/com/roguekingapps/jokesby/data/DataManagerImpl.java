package com.roguekingapps.jokesby.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.Html;
import android.util.Log;

import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.database.DatabaseHelper;
import com.roguekingapps.jokesby.data.database.JokeContract.JokeEntry;
import com.roguekingapps.jokesby.data.network.ApiHelper;
import com.roguekingapps.jokesby.data.network.model.Joke;
import com.roguekingapps.jokesby.data.network.model.JokeContainer;
import com.roguekingapps.jokesby.di.ApplicationContext;
import com.roguekingapps.jokesby.ui.detail.DetailPresenter;
import com.roguekingapps.jokesby.ui.main.MainPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Communicates between the data helper classes, i.e {@link ApiHelper}, and the presenters.
 */
@Singleton
public class DataManagerImpl implements DataManager {

    private static final String TAG = DataManagerImpl.class.getSimpleName();

    private Context context;
    private ApiHelper apiHelper;
    private DatabaseHelper databaseHelper;

    @Inject
    DataManagerImpl(@ApplicationContext Context context,
                    ApiHelper apiHelper,
                    DatabaseHelper databaseHelper) {
        this.context = context;
        this.apiHelper = apiHelper;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void loadFromApi(final MainPresenter presenter) {
        Consumer<JokeContainer> jokeConsumer = new Consumer<JokeContainer>() {
            @Override
            public void accept(JokeContainer jokeContainer) throws Exception {
                if (jokeContainer != null) {
                    List<Joke> jokes = jokeContainer.getJokes();
                    List<Joke> filteredJokes = getFilteredJokes(jokes);
                    presenter.showJokes(filteredJokes);
                }
            }
        };
        apiHelper.loadJokes(jokeConsumer);
    }

    @Override
    public void loadFromFavourites(final MainPresenter presenter) {
        Observer<Cursor> queryAllFavouritesObserver = new Observer<Cursor>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Cursor cursor) {
                List<Joke> jokes = new ArrayList<>();
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    String apiId = cursor.getString(cursor.getColumnIndex(JokeEntry.COLUMN_API_ID));
                    String title = cursor.getString(cursor.getColumnIndex(JokeEntry.COLUMN_TITLE));
                    String body = cursor.getString(cursor.getColumnIndex(JokeEntry.COLUMN_BODY));
                    String user = cursor.getString(cursor.getColumnIndex(JokeEntry.COLUMN_USER));
                    String url = cursor.getString(cursor.getColumnIndex(JokeEntry.COLUMN_URL));
                    Joke joke = new Joke(apiId, title, body, user, url);
                    jokes.add(joke);
                }
                cursor.close();
                presenter.showJokes(jokes);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage(), e);
                presenter.showError(context.getResources().getString(R.string.error_query_favourite));
            }

            @Override
            public void onComplete() {

            }
        };
        databaseHelper.queryAll(queryAllFavouritesObserver);
    }

    private List<Joke> getFilteredJokes(List<Joke> jokes) {
        List<Joke> filteredJokes = new ArrayList<>();
        for (Joke joke : jokes) {
            String body = joke.getBody();
            if (!body.contains("[removed]") && !body.contains("[deleted]")) {
                joke.setTitle(fromHtml(joke.getTitle()));
                joke.setBody(fromHtml(body));
                filteredJokes.add(joke);
            }
        }
        return filteredJokes;
    }

    @SuppressWarnings("deprecation")
    private String fromHtml(String html){
        String result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            result = Html.fromHtml(html).toString();
        }
        return result;
    }

    @Override
    public void query(final DetailPresenter presenter, String apiId) {
        Observer<Cursor> queryFavouritesObserver = new Observer<Cursor>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Cursor cursor) {
                presenter.updateFavouriteIcon(cursor.getCount() >= 1);
                cursor.close();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage(), e);
                presenter.showError(context.getResources().getString(R.string.error_query_favourite));
            }

            @Override
            public void onComplete() {

            }
        };
        databaseHelper.query(queryFavouritesObserver, apiId);
    }

    @Override
    public void updateJoke(final DetailPresenter presenter, final Joke joke) {
        Consumer<Integer> deleteFavouriteConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer rowsDeleted) throws Exception {
                if (rowsDeleted == null || rowsDeleted == 0) {
                    insertJoke(presenter, joke);
                    return;
                }
                presenter.updateFavouriteIcon(false);
            }
        };
        databaseHelper.deleteJoke(deleteFavouriteConsumer, joke.getId());
    }

    private void insertJoke(final DetailPresenter presenter, final Joke joke) {
        Observer<Uri> insertFavouriteObserver = new Observer<Uri>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Uri uri) {
                presenter.updateFavouriteIcon(uri != null);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage(), e);
                presenter.showError(context.getResources().getString(R.string.error_add_favourite));
            }

            @Override
            public void onComplete() {

            }
        };
        databaseHelper.insertJoke(insertFavouriteObserver, joke);
    }
}
