package com.roguekingapps.jokesby.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.network.model.Joke;
import com.roguekingapps.jokesby.databinding.LayoutJokeListItemBinding;
import com.roguekingapps.jokesby.di.PerActivity;

import java.util.List;

/**
 * Creates and binds the {@link ViewHolder} objects using a {@link List} of {@link Joke}
 * objects.
 */
@PerActivity
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Context context;
    private List<Joke> jokes;
    private JokeOnClickHandler clickHandler;

    public ListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutJokeListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.layout_joke_list_item,
                parent,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind();
        Joke joke = jokes.get(position);
        holder.binding.jokeTitle.setText(joke.getTitle());
    }

    @Override
    public int getItemCount() {
        if (jokes == null) {
            return 0;
        }
        return jokes.size();
    }

    public void setJokes(List<Joke> jokes) {
        this.jokes = jokes;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LayoutJokeListItemBinding binding;

        ViewHolder(LayoutJokeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            View view = this.binding.getRoot();
            view.setOnClickListener(this);
        }

        void bind() {
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface JokeOnClickHandler {
        void onClick(Joke joke);
    }
}
