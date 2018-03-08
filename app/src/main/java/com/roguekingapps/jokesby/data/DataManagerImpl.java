package com.roguekingapps.jokesby.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.Html;
import android.util.Log;

import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.database.DatabaseHelper;
import com.roguekingapps.jokesby.data.database.JokeContract.FavouriteEntry;
import com.roguekingapps.jokesby.data.database.JokeContract.RatedEntry;
import com.roguekingapps.jokesby.data.network.ApiHelper;
import com.roguekingapps.jokesby.data.model.Joke;
import com.roguekingapps.jokesby.data.model.JokeContainer;
import com.roguekingapps.jokesby.di.ApplicationContext;
import com.roguekingapps.jokesby.ui.detail.DetailPresenter;
import com.roguekingapps.jokesby.ui.main.MainPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

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
                    presenter.showJokesFromApi(filteredJokes);
                }
            }
        };

        presenter.addDisposable(apiHelper.getJokeContainerObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jokeConsumer));
    }

    private List<Joke> getFilteredJokes(List<Joke> jokes) {
        List<Joke> filteredJokes = new ArrayList<>();
        for (Joke joke : jokes) {
            String body = joke.getBody();
            if (body != null && !body.contains("[removed]") && !body.contains("[deleted]")) {
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
            result = Html.fromHtml(html.replace("\n","<br />"),Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            result = Html.fromHtml(html.replace("\n","<br />")).toString();
        }
        return result;
    }


    @Override
    public void loadFromFavourites(final MainPresenter presenter) {
        DisposableObserver<Cursor> queryAllFavouritesObserver = new DisposableObserver<Cursor>() {
            @Override
            public void onNext(Cursor cursor) {
                List<Joke> jokes = new ArrayList<>();
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    String apiId = cursor.getString(cursor.getColumnIndex(FavouriteEntry.COLUMN_API_ID));
                    String title = cursor.getString(cursor.getColumnIndex(FavouriteEntry.COLUMN_TITLE));
                    String body = cursor.getString(cursor.getColumnIndex(FavouriteEntry.COLUMN_BODY));
                    String user = cursor.getString(cursor.getColumnIndex(FavouriteEntry.COLUMN_USER));
                    String url = cursor.getString(cursor.getColumnIndex(FavouriteEntry.COLUMN_URL));
                    Joke joke = new Joke(apiId, title, body, user, url);
                    jokes.add(joke);
                }
                cursor.close();
                presenter.showJokesFromFavourites(jokes);
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

        presenter.addDisposable(databaseHelper.getQueryAllFavouritesObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(queryAllFavouritesObserver));
    }

    @Override
    public void queryFavourite(final DetailPresenter presenter, String apiId) {
        DisposableObserver<Cursor> queryFavouriteObserver = new DisposableObserver<Cursor>() {
            @Override
            protected void onStart() {
                super.onStart();
                presenter.onStartLoad();
            }

            @Override
            public void onNext(Cursor cursor) {
                presenter.onPostLoad();
                presenter.onPostUpdateFavourite(cursor.getCount() >= 1);
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

        Observable<Cursor> queryObservable = databaseHelper.getQueryObservable(
                FavouriteEntry.CONTENT_URI,
                FavouriteEntry.COLUMN_API_ID,
                apiId);
        presenter.addDisposable(queryObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(queryFavouriteObserver));
    }

    @Override
    public void updateFavourite(final DetailPresenter presenter, final Joke joke) {
        Consumer<Integer> deleteFavouriteConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer rowsDeleted) throws Exception {
                if (rowsDeleted == null || rowsDeleted == 0) {
                    insertFavourite(presenter, joke);
                    return;
                }
                presenter.onPostUpdateFavourite(false);
            }
        };

        presenter.addDisposable(databaseHelper.getDeleteObservable(joke.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deleteFavouriteConsumer));
    }

    private void insertFavourite(final DetailPresenter presenter, final Joke joke) {
        DisposableObserver<Uri> insertFavouriteObserver = new DisposableObserver<Uri>() {
            @Override
            public void onNext(Uri uri) {
                presenter.onPostUpdateFavourite(uri != null);
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

        presenter.addDisposable(databaseHelper.getInsertFavouriteObservable(joke)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(insertFavouriteObserver));
    }

    @Override
    public void queryRated(final DetailPresenter presenter, String apiId) {
        DisposableObserver<Cursor> queryRatedObserver = new DisposableObserver<Cursor>() {
            @Override
            protected void onStart() {
                super.onStart();
                presenter.onStartLoad();
            }

            @Override
            public void onNext(Cursor cursor) {
                if (cursor.getCount() > 0) {
                    cursor.moveToPosition(-1);
                    cursor.moveToNext();
                    String rating = cursor.getString(cursor.getColumnIndex(RatedEntry.COLUMN_RATING));
                    presenter.checkRating(rating);
                }
                presenter.onPostLoad();
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

        Observable<Cursor> queryObservable = databaseHelper.getQueryObservable(
                RatedEntry.CONTENT_URI,
                RatedEntry.COLUMN_API_ID,
                apiId);
        presenter.addDisposable(queryObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(queryRatedObserver));
    }

    @Override
    public void updateRated(final DetailPresenter presenter, final Joke joke) {
        Consumer<Integer> rowsUpdatedConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer rowsUpdated) throws Exception {
                if (rowsUpdated == null || rowsUpdated == 0) {
                    insertRated(presenter, joke);
                    return;
                }
                presenter.checkRating();
            }
        };

        presenter.addDisposable(databaseHelper.getUpdateObservable(joke)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowsUpdatedConsumer));
    }

    private void insertRated(final DetailPresenter presenter, final Joke joke) {
        DisposableObserver<Uri> insertRatedObserver = new DisposableObserver<Uri>() {
            @Override
            public void onNext(Uri uri) {
                presenter.checkRating();
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

        presenter.addDisposable(databaseHelper.getInsertRatedObservable(joke)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(insertRatedObserver));
    }
}
