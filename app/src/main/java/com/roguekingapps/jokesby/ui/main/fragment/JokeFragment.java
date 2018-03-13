package com.roguekingapps.jokesby.ui.main.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.roguekingapps.jokesby.data.model.Joke;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JokeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JokeFragment extends Fragment {

    private List<Joke> jokes;

    public JokeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment JokeFragment.
     */
    public static JokeFragment newInstance() {
        return new JokeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public List<Joke> getJokes() {
        return jokes;
    }

    public void setJokes(List<Joke> jokes) {
        this.jokes = jokes;
    }
}
