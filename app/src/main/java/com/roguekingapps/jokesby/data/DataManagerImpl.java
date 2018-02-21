package com.roguekingapps.jokesby.data;

import com.roguekingapps.jokesby.data.network.ApiHelper;
import com.roguekingapps.jokesby.data.network.model.Joke;
import com.roguekingapps.jokesby.data.network.model.JokeContainer;
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

    @Inject
    DataManagerImpl(ApiHelper apiHelper) {
        this.apiHelper = apiHelper;
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
}
