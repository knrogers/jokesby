package com.roguekingapps.jokesby.ui.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.roguekingapps.jokesby.JokesbyApplication;
import com.roguekingapps.jokesby.R;
import com.roguekingapps.jokesby.data.network.model.Joke;
import com.roguekingapps.jokesby.databinding.FragmentListJokeBinding;
import com.roguekingapps.jokesby.di.component.DaggerJokeListFragmentComponent;
import com.roguekingapps.jokesby.di.component.JokeListFragmentComponent;
import com.roguekingapps.jokesby.di.module.JokeListFragmentModule;
import com.roguekingapps.jokesby.ui.adapter.ListAdapter;
import com.roguekingapps.jokesby.ui.main.MainView;

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
        MainView,
        ListAdapter.JokeOnClickHandler {

    private ListFragmentListener listener;
    private JokeListFragmentComponent fragmentComponent;

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
        FragmentListJokeBinding binding = FragmentListJokeBinding.inflate(inflater, container, false);
        getFragmentComponent().inject(this);
        binding.listRecyclerView.setAdapter(adapter);
        listener.loadJokes();
        return binding.getRoot();
    }

    @Override
    public void showJokes(List<Joke> jokes) {
        if (jokes == null) {
            showToast(getString(R.string.jokes_not_loaded));
        } else {
            adapter.setJokes(jokes);
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
