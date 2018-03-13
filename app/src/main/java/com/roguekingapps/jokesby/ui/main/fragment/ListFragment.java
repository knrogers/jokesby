package com.roguekingapps.jokesby.ui.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.roguekingapps.jokesby.JokesbyApplication;
import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.model.Joke;
import com.roguekingapps.jokesby.databinding.FragmentListJokeBinding;
import com.roguekingapps.jokesby.di.component.DaggerListFragmentComponent;
import com.roguekingapps.jokesby.di.component.ListFragmentComponent;
import com.roguekingapps.jokesby.di.module.ListFragmentModule;
import com.roguekingapps.jokesby.ui.adapter.ListAdapter;
import com.roguekingapps.jokesby.ui.adapter.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragmentListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment  implements
        ListAdapter.JokeOnClickHandler,
        OnLoadMoreListener {

    private static final String JOKE_FRAGMENT_TAG_ID = "jokeFragmentTagId";

    private FragmentListJokeBinding binding;
    private ListFragmentListener listener;
    private ListFragmentComponent fragmentComponent;
    private RecyclerView.SmoothScroller smoothScroller;
    private String jokeFragmentTag;
    private boolean initialLoad = true;

    @Inject
    ListAdapter adapter;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @param jokeFragmentTagId id of JokeFragment tag
     * @return A new instance of fragment ListFragment.
     */
    public static ListFragment newInstance(int jokeFragmentTagId) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(JOKE_FRAGMENT_TAG_ID, jokeFragmentTagId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListJokeBinding.inflate(inflater, container, false);
        getFragmentComponent().inject(this);
        binding.listRecyclerView.setAdapter(adapter);
        Context context = getContext();
        if (context != null) {
            smoothScroller = new LinearSmoothScroller(context) {
                @Override protected int getVerticalSnapPreference() {
                    return LinearSmoothScroller.SNAP_TO_START;
                }
            };
            final LinearLayoutManager linearLayoutManager =
                    (LinearLayoutManager) binding.listRecyclerView.getLayoutManager();
            if (context.getString(R.string.hot).equals(getTag())
                    || context.getString(R.string.random).equals(getTag())) {
                binding.listRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (initialLoad) {
                            initialLoad = false;
                        }
                        adapter.loadMore(linearLayoutManager);
                    }
                });
            }
        }

        Bundle args = getArguments();
        int jokeFragmentTagId = getJokeFragmentTagId(args);
        jokeFragmentTag = getString(jokeFragmentTagId);
        JokeFragment jokeFragment = listener.getJokeFragment(jokeFragmentTag);

        if (jokeFragment == null) {
            showToast(getString(R.string.jokes_not_loaded));
            binding.listRecyclerView.setVisibility(View.GONE);
            binding.listEmptyView.setVisibility(View.GONE);
            binding.listProgressBar.setVisibility(View.GONE);
            binding.listEmptyView.setVisibility(View.VISIBLE);
            binding.listEmptyView.setText(getString(R.string.jokes_not_loaded));
            return binding.getRoot();
        }

        if (jokeFragment.getJokes() == null || jokeFragment.getJokes().isEmpty()) {
            listener.loadJokes();
        } else {
            binding.listRecyclerView.setVisibility(View.VISIBLE);
            adapter.setJokes(jokeFragment.getJokes());
            adapter.notifyDataSetChanged();
        }

        return binding.getRoot();
    }

    private int getJokeFragmentTagId(Bundle args) {
        int jokeFragmentTagId = -1;
        if (args != null) {
            if (args.containsKey(JOKE_FRAGMENT_TAG_ID)) {
                jokeFragmentTagId = args.getInt(JOKE_FRAGMENT_TAG_ID);
            }
        }
        return jokeFragmentTagId;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(getString(R.string.initial_load), initialLoad);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            initialLoad = savedInstanceState.getBoolean(getString(R.string.initial_load));
        }
    }

    public void onStartLoad() {
        if (initialLoad) {
            binding.listRecyclerView.setVisibility(View.GONE);
            binding.listEmptyView.setVisibility(View.GONE);
            binding.listProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void onPostLoad() {
        binding.listProgressBar.setVisibility(View.GONE);
    }

    public void showJokesFromApi(final List<Joke> jokes) {
        listener.setJokes(jokes, jokeFragmentTag);
        if (jokes == null) {
            showToast(getString(R.string.jokes_not_loaded));
            binding.listEmptyView.setVisibility(View.VISIBLE);
            binding.listEmptyView.setText(getString(R.string.jokes_not_loaded));
        } else {
            binding.listRecyclerView.setVisibility(View.VISIBLE);
            if (adapter.getJokes() == null) {
                adapter.setJokes(jokes);
            } else {
                //   remove progress item
                adapter.removeLast();
                adapter.addJokes(jokes);
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void showJokesFromDatabase(final List<Joke> jokes) {
        if (jokes.isEmpty()) {
            String emptyText = getEmptyText();
            if (emptyText != null) {
                binding.listEmptyView.setVisibility(View.VISIBLE);
                binding.listEmptyView.setText(emptyText);
            }
        } else {
            binding.listRecyclerView.setVisibility(View.VISIBLE);
            adapter.setJokes(jokes);
            adapter.notifyDataSetChanged();
        }
    }

    @Nullable
    private String getEmptyText() {
        String emptyText = null;
        if (getTag() != null) {
            if (getTag().equals(getString(R.string.favourites))) {
                emptyText = getString(R.string.add_favourites);
            } else if (getTag().equals(getString(R.string.rated))) {
                emptyText = getString(R.string.rate_jokes);
            }
        }
        return emptyText;
    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(Joke joke) {
        listener.showDetailActivity(joke);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListFragmentListener) {
            listener = (ListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onLoadMore() {
        //add null so that the adapter will check view type and show progress bar
        //at the end of the list.
        List<Joke> jokes = new ArrayList<>();
        jokes.add(null);
        adapter.addJokes(jokes);
        binding.listRecyclerView.post(new Runnable() {
            public void run() {
                adapter.notifyItemInserted(adapter.getJokes().size() - 1);
                listener.loadJokes();
            }
        });
    }

    public void resetScrollPosition() {
        smoothScroller.setTargetPosition(0);
        LinearLayoutManager layoutManager =
                (LinearLayoutManager) binding.listRecyclerView.getLayoutManager();
        layoutManager.startSmoothScroll(smoothScroller);
    }

    public int getScrollOffset() {
        return binding.listRecyclerView.computeVerticalScrollOffset();
    }

    public ListFragmentComponent getFragmentComponent() {
        if (fragmentComponent == null) {
            fragmentComponent = DaggerListFragmentComponent.builder()
                    .listFragmentModule(new ListFragmentModule(this))
                    .applicationComponent(JokesbyApplication.get(getContext()).getComponent())
                    .build();
        }
        return fragmentComponent;
    }
}
