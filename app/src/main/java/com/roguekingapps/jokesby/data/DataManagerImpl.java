package com.roguekingapps.jokesby.data;

import com.roguekingapps.jokesby.data.database.DatabaseHelper;
import com.roguekingapps.jokesby.data.network.ApiHelper;
import com.roguekingapps.jokesby.data.network.model.Joke;
import com.roguekingapps.jokesby.data.network.model.JokeContainer;
import com.roguekingapps.jokesby.ui.detail.DetailPresenter;
import com.roguekingapps.jokesby.ui.main.MainPresenter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Consumer;

/**
 * Communicates between the data helper classes, i.e {@link ApiHelper}, and the presenters.
 */
@Singleton
public class DataManagerImpl implements DataManager {

    private ApiHelper apiHelper;
    private DatabaseHelper databaseHelper;

    @Inject
    DataManagerImpl(ApiHelper apiHelper, DatabaseHelper databaseHelper) {
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
    public void updateJoke(final DetailPresenter presenter, String apiId) {
        Consumer<Integer> deleteFavouriteConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer rowsDeleted) throws Exception {
                boolean favourite = false;
                if (rowsDeleted == null || rowsDeleted == 0) {
                    return;
                }
                presenter.updateFavouriteIcon(favourite);
            }
        };
        databaseHelper.deleteJoke(deleteFavouriteConsumer, apiId);
    }
}
