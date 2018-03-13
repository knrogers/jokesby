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
import com.roguekingapps.jokesby.data.model.RedditChild;
import com.roguekingapps.jokesby.data.model.RedditData;
import com.roguekingapps.jokesby.data.model.RedditRoot;
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
    public void loadHot(final MainPresenter presenter, String after) {
        DisposableObserver<RedditRoot> jokeConsumer = new DisposableObserver<RedditRoot>() {
            @Override
            protected void onStart() {
                super.onStart();
                presenter.onStartLoad();
            }

            @Override
            public void onNext(RedditRoot redditRoot) {
                if (redditRoot != null && redditRoot.getRedditData() != null) {
                    RedditData redditData = redditRoot.getRedditData();
                    String after = redditData.getAfter();
                    presenter.setAfter(after);

                    List<RedditChild> redditChildren = redditData.getRedditChildren();
                    if (redditChildren != null) {
                        List<Joke> jokes = new ArrayList<>();
                        for (RedditChild redditChild : redditChildren) {
                            jokes.add(redditChild.getJoke());
                        }
                        List<Joke> filteredJokes = getFilteredJokes(jokes);
                        presenter.onPostLoad();
                        presenter.showJokesFromApi(filteredJokes);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage(), e);
                presenter.onPostLoad();
                presenter.showError(context.getResources().getString(R.string.error_query_api));
            }

            @Override
            public void onComplete() {

            }
        };

        presenter.addDisposable(apiHelper.getJokeObservableFromReddit(after)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(jokeConsumer));
    }

    @Override
    public void loadRandom(final MainPresenter presenter) {
        DisposableObserver<JokeContainer> jokeConsumer = new DisposableObserver<JokeContainer>() {
            @Override
            protected void onStart() {
                super.onStart();
                presenter.onStartLoad();
            }

            @Override
            public void onNext(JokeContainer jokeContainer) {
                if (jokeContainer != null) {
                    List<Joke> jokes = jokeContainer.getJokes();
                    List<Joke> filteredJokes = getFilteredJokes(jokes);
                    presenter.onPostLoad();
                    presenter.showJokesFromApi(filteredJokes);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage(), e);
                presenter.onPostLoad();
                presenter.showError(context.getResources().getString(R.string.error_query_api));
            }

            @Override
            public void onComplete() {

            }
        };

        presenter.addDisposable(apiHelper.getJokeObservableFromPushShift()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(jokeConsumer));
    }

    private List<Joke> getFilteredJokes(List<Joke> jokes) {
        List<Joke> filteredJokes = new ArrayList<>();
        for (Joke joke : jokes) {
            String body = joke.getBody();
            if (!joke.isStickied() && body != null
                    && !body.contains("[removed]") && !body.contains("[deleted]")) {
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
    public void loadFavourites(final MainPresenter presenter) {
        DisposableObserver<Cursor> queryAllFavouritesObserver = new DisposableObserver<Cursor>() {
            @Override
            protected void onStart() {
                super.onStart();
                presenter.onStartLoad();
            }

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
                presenter.onPostLoad();
                presenter.showJokesFromDatabase(jokes);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage(), e);
                presenter.onPostLoad();
                presenter.showError(context.getResources().getString(R.string.error_query_favourite));
            }

            @Override
            public void onComplete() {

            }
        };

        presenter.addDisposable(databaseHelper.getQueryAllObservable(FavouriteEntry.CONTENT_URI)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(queryAllFavouritesObserver));
    }

    @Override
    public void loadRated(final MainPresenter presenter) {
        DisposableObserver<Cursor> queryAllRatedObserver = new DisposableObserver<Cursor>() {
            @Override
            protected void onStart() {
                super.onStart();
                presenter.onStartLoad();
            }

            @Override
            public void onNext(Cursor cursor) {
                List<Joke> jokes = new ArrayList<>();
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    String apiId = cursor.getString(cursor.getColumnIndex(RatedEntry.COLUMN_API_ID));
                    String title = cursor.getString(cursor.getColumnIndex(RatedEntry.COLUMN_TITLE));
                    String body = cursor.getString(cursor.getColumnIndex(RatedEntry.COLUMN_BODY));
                    String user = cursor.getString(cursor.getColumnIndex(RatedEntry.COLUMN_USER));
                    String url = cursor.getString(cursor.getColumnIndex(RatedEntry.COLUMN_URL));
                    String rating = cursor.getString(cursor.getColumnIndex(RatedEntry.COLUMN_RATING));
                    Joke joke = new Joke(apiId, title, body, user, url);
                    joke.setRating(rating);
                    jokes.add(joke);
                }
                cursor.close();
                presenter.onPostLoad();
                presenter.showJokesFromDatabase(jokes);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage(), e);
                presenter.onPostLoad();
                presenter.showError(context.getResources().getString(R.string.error_query_favourite));
            }

            @Override
            public void onComplete() {

            }
        };

        presenter.addDisposable(databaseHelper.getQueryAllObservable(RatedEntry.CONTENT_URI)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(queryAllRatedObserver));
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

        Observable<Integer> deleteObservable = databaseHelper.getDeleteObservable(
                FavouriteEntry.CONTENT_URI,
                FavouriteEntry.COLUMN_API_ID,
                joke.getId());
        presenter.addDisposable(deleteObservable
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
                presenter.onPostUpdateRating();
            }
        };

        presenter.addDisposable(databaseHelper.getUpdateObservable(joke)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowsUpdatedConsumer));
    }

    @Override
    public void deleteRated(final DetailPresenter presenter, String apiId) {
        Consumer<Integer> rowsUpdatedConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer rowsDeleted) throws Exception {
                presenter.onPostUpdateRating();
            }
        };

        Observable<Integer> deleteObservable = databaseHelper.getDeleteObservable(
                RatedEntry.CONTENT_URI,
                RatedEntry.COLUMN_API_ID,
                apiId);
        presenter.addDisposable(deleteObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowsUpdatedConsumer));
    }

    private void insertRated(final DetailPresenter presenter, final Joke joke) {
        DisposableObserver<Uri> insertRatedObserver = new DisposableObserver<Uri>() {
            @Override
            public void onNext(Uri uri) {
                presenter.onPostUpdateRating();
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
