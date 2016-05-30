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
import android.widget.TextView;
import android.widget.Toast;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class SignInFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = SignInFragment.class
            .getSimpleName();

    private SignUpFragmentListener mListener;

    private Button sign_in;
    private ProgressBar progressBar;
    private EditText email, password;

    private String emailText, passwordText;

    private TextView forgot_password, sign_up;

    public SignInFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
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
        password = (EditText) view.findViewById(R.id.password);
        forgot_password = (TextView) view.findViewById(R.id.forgot_password);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        sign_in = (Button) view.findViewById(R.id.sign_in);
        sign_up = (TextView) view.findViewById(R.id.sign_up);

        sign_in.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
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
            case R.id.sign_in:
                continue_action();
                break;
            case R.id.sign_up:
                nextAction(Constants.EmailSignUp, null);
                break;
            case R.id.forgot_password:
                nextAction(Constants.Reset, null);
                break;
        }
    }
}