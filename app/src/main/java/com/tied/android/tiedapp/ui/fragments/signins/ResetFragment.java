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
import android.widget.Toast;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class ResetFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = ResetFragment.class
            .getSimpleName();

    private SignUpFragmentListener mListener;

    private Button reset;
    private ProgressBar progressBar;
    private EditText email;

    private String emailText;

    public ResetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in_reset, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
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

    public void initComponent(View view){

        email = (EditText) view.findViewById(R.id.email);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        reset = (Button) view.findViewById(R.id.reset);
        reset.setOnClickListener(this);
    }

    public void continue_action(){
        if(validated()){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
        }

    }

    public boolean validated(){

        emailText = email.getText().toString();
        return emailText.contains("@");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reset:
                nextAction(Constants.DoneReset, null);
                break;
        }
    }
}