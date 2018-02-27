package com.roguekingapps.jokesby.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.database.DatabaseHelper;
import com.roguekingapps.jokesby.data.network.ApiHelper;
import com.roguekingapps.jokesby.data.network.model.Joke;
import com.roguekingapps.jokesby.data.network.model.JokeContainer;
import com.roguekingapps.jokesby.di.ApplicationContext;
import com.roguekingapps.jokesby.ui.detail.DetailPresenter;
import com.roguekingapps.jokesby.ui.main.MainPresenter;

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
    public void loadJokes(final MainPresenter presenter) {
        Consumer<JokeContainer> jokeConsumer = new Consumer<JokeContainer>() {
            @Override
            public void accept(JokeContainer jokeContainer) throws Exception {
                if (jokeContainer != null) {
                    List<Joke> jokes = jokeContainer.getJokes();
                    if (jokes != null && !jokes.isEmpty()) {
                        presenter.showJokes(jokes);
                    }
                }
            }
        };
        apiHelper.loadJokes(jokeConsumer);
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
