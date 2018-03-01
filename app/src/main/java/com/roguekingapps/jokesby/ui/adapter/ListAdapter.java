package com.roguekingapps.jokesby.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roguekingapps.jokesby.data.network.model.Joke;
import com.roguekingapps.jokesby.databinding.LayoutJokeListItemBinding;
import com.roguekingapps.jokesby.databinding.LayoutProgressBarBinding;
import com.roguekingapps.jokesby.di.PerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates and binds the {@link JokeViewHolder} objects using a {@link List} of {@link Joke}
 * objects.
 */
@PerActivity
public class ListAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_LOADING = 0;

    private OnLoadMoreListener onLoadMoreListener;
    private boolean loading;
    private Context context;
    private List<Joke> jokes;
    private JokeOnClickHandler clickHandler;
    private Typeface robotoMedium;

    public ListAdapter(
            Context context,
            JokeOnClickHandler clickHandler,
            OnLoadMoreListener onLoadMoreListener) {
        this.context = context;
        this.clickHandler = clickHandler;
        this.onLoadMoreListener = onLoadMoreListener;
        robotoMedium = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Medium.ttf");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            LayoutJokeListItemBinding binding = LayoutJokeListItemBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false);
            return new JokeViewHolder(binding);
        } else if (viewType == VIEW_LOADING) {
            LayoutProgressBarBinding binding = LayoutProgressBarBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false);
            return new LoadingViewHolder(binding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof JokeViewHolder) {
            Joke joke = jokes.get(position);
            JokeViewHolder jokeViewHolder = (JokeViewHolder) holder;
            jokeViewHolder.bind();
            jokeViewHolder.layoutJokeListItemBinding.jokeTitle.setTypeface(robotoMedium);
            jokeViewHolder.layoutJokeListItemBinding.jokeTitle.setText(joke.getTitle());
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.bind();
            loadingViewHolder.layoutProgressBarBinding.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return jokes.get(position) == null ? VIEW_LOADING : VIEW_ITEM;
    }

    @Override
    public int getItemCount() {
        if (jokes == null) {
            return 0;
        }
        return jokes.size();
    }

    public void removeLast() {
        if (jokes == null) {
            return;
        }

        if (jokes.get(jokes.size() - 1) == null) {
            jokes.remove(jokes.size() - 1);
            notifyItemRemoved(jokes.size());
        }
    }

    public List<Joke> getJokes() {
        return jokes;
    }

    public void setJokes(List<Joke> jokes) {
        if (this.jokes == null) {
            this.jokes = new ArrayList<>();
        }
        this.jokes.addAll(jokes);
    }

    public void loadMore(LinearLayoutManager linearLayoutManager) {
        int totalItemCount = linearLayoutManager.getItemCount();
        int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
        int visibleThreshold = 5;
        if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            if (onLoadMoreListener != null) {
                onLoadMoreListener.onLoadMore();
            }
            loading = true;
        }
    }

    class JokeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LayoutJokeListItemBinding layoutJokeListItemBinding;

        JokeViewHolder(LayoutJokeListItemBinding layoutJokeListItemBinding) {
            super(layoutJokeListItemBinding.getRoot());
            this.layoutJokeListItemBinding = layoutJokeListItemBinding;
            View view = this.layoutJokeListItemBinding.getRoot();
            view.setOnClickListener(this);
        }

        void bind() {
            layoutJokeListItemBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            clickHandler.onClick(jokes.get(getAdapterPosition()));
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        private LayoutProgressBarBinding layoutProgressBarBinding;

        LoadingViewHolder(LayoutProgressBarBinding layoutProgressBarBinding) {
            super(layoutProgressBarBinding.getRoot());
            this.layoutProgressBarBinding = layoutProgressBarBinding;
        }

        void bind() {
            layoutProgressBarBinding.executePendingBindings();
        }
    }

    public interface JokeOnClickHandler {
        void onClick(Joke joke);
    }
}
