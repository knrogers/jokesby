package com.roguekingapps.jokesby.ui.main.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.roguekingapps.jokesby.databinding.FragmentDisclaimerDialogBinding;

/**
 * A simple {@link DialogFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDisclaimerInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DisclaimerDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisclaimerDialogFragment extends DialogFragment {

    private OnDisclaimerInteractionListener listener;

    public DisclaimerDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DisclaimerDialogFragment.
     */
    public static DisclaimerDialogFragment newInstance() {
        return new DisclaimerDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentDisclaimerDialogBinding binding =
                FragmentDisclaimerDialogBinding.inflate(LayoutInflater.from(getContext()));
        final AlertDialog alertDialog =
                new AlertDialog.Builder(getContext())
                        .setView(binding.getRoot())
                        .create();
        binding.disclaimerContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                listener.onSelectContinue();
            }
        });

        binding.disclaimerExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectExit();
            }
        });

        return alertDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDisclaimerInteractionListener) {
            listener = (OnDisclaimerInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDisclaimerInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnDisclaimerInteractionListener {
        void onSelectContinue();
        void onSelectExit();
    }
}
