package com.tied.android.tiedapp.ui.fragments.signins;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.auth.LoginUser;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.signups.SignInActivity;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

            SignUpApi signUpApi = ((SignInActivity) getActivity()).service;
            Call<LoginUser> response = signUpApi.LoginUser(emailText, passwordText);
            Log.d(TAG, response.request().url().toString());
            response.enqueue(new Callback<LoginUser>() {
                @Override
                public void onResponse(Call<LoginUser> call, Response<LoginUser> LoginResponse) {
                    LoginUser LoginUser = LoginResponse.body();

                    Log.d(TAG, LoginUser.toString());
                    User loggedIn_user = LoginUser.getUser();
                    loggedIn_user.setToken(LoginUser.getToken());
                    if (loggedIn_user.getToken() != null) {
                        boolean saved = loggedIn_user.save(getActivity().getApplicationContext());
                        if (saved) {
                            Bundle bundle = new Bundle();
                            Gson gson = new Gson();
                            String user_json = gson.toJson(loggedIn_user);
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra(Constants.USER, user_json);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "user not save", Toast.LENGTH_LONG).show();
                        }
                        Log.d(TAG, loggedIn_user.toString());
                    } else {
                        Toast.makeText(getActivity(), "user not created", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<LoginUser> checkEmailCall, Throwable t) {
                    Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                    Log.d(TAG + " onFailure", t.toString());
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }else{
            Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
        }
    }

    public boolean validated(){

        emailText = email.getText().toString();
        passwordText = password.getText().toString();
        return emailText.contains("@");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in:
                continue_action();
                break;
            case R.id.forgot_password:
                mListener.onFragmentInteraction(Constants.Reset, null);
                break;
            case R.id.sign_up:
                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}