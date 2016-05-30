package com.tied.android.tiedapp.ui.fragments.signins;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class DoneResetFragment extends Fragment{

    public static final String TAG = DoneResetFragment.class
            .getSimpleName();

    private SignUpFragmentListener mListener;

    private Button sign_in, register;
    private ProgressBar progressBar;
    private EditText email, password;

    private String emailText, passwordText;

    public DoneResetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in_reset_done, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignUpFragmentListener) {
            mListener = (SignUpFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action, Bundle bundle) {
        if (mListener != null) {
            mListener.onFragmentInteraction(action,bundle);
        }
    }
}