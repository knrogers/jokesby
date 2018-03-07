package com.roguekingapps.jokesby.ui.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.roguekingapps.jokesby.di.component.DaggerJokeListFragmentComponent;
import com.roguekingapps.jokesby.di.component.JokeListFragmentComponent;
import com.roguekingapps.jokesby.di.module.JokeListFragmentModule;
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
 * Use the {@link JokeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JokeListFragment extends Fragment  implements
        ListAdapter.JokeOnClickHandler,
        OnLoadMoreListener {

    private FragmentListJokeBinding binding;
    private ListFragmentListener listener;
    private JokeListFragmentComponent fragmentComponent;
    private RecyclerView.SmoothScroller smoothScroller;

    @Inject
    ListAdapter adapter;

    public JokeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment JokeListFragment.
     */
    public static JokeListFragment newInstance() {
        return new JokeListFragment();
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
            if (context.getString(R.string.random).equals(getTag())) {
                binding.listRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        adapter.loadMore(linearLayoutManager);
                    }
                });
            }
        }
        onPreLoad();
        listener.loadJokes();
        return binding.getRoot();
    }

    private void onPreLoad() {
        binding.listRecyclerView.setVisibility(View.GONE);
        binding.listEmptyView.setVisibility(View.GONE);
        binding.listProgressBar.setVisibility(View.VISIBLE);
    }

    public void showJokes(final List<Joke> jokes) {
        binding.listProgressBar.setVisibility(View.GONE);
        if (jokes == null) {
            showToast(getString(R.string.jokes_not_loaded));
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

    public JokeListFragmentComponent getFragmentComponent() {
        if (fragmentComponent == null) {
            fragmentComponent = DaggerJokeListFragmentComponent.builder()
                    .jokeListFragmentModule(new JokeListFragmentModule(this))
                    .applicationComponent(JokesbyApplication.get(getContext()).getComponent())
                    .build();
        }
        return fragmentComponent;
    }
}
