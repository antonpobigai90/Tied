package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.auth.Login;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class PasswordFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = PasswordFragment.class
            .getSimpleName();

    private SignUpFragmentListener mListener;

    private Button continue_btn;
    private ProgressBar progressBar;
    private EditText password;

    private String usernameText, passwordText;

    public PasswordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }


    public void initComponent(View view) {

        password = (EditText) view.findViewById(R.id.password);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        continue_btn = (Button) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);
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


    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            mListener.onFragmentInteraction(Constants.Picture, bundle);
        }
    }

    public void continue_action() {

        if (validated()) {
            Bundle bundle = getArguments();

            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            User user = gson.fromJson(user_json, User.class);
            progressBar.setVisibility(View.VISIBLE);
            SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
            Call<Login> response = signUpApi.LoginSignUpUser(user.getEmail(), passwordText, Constants.Picture);
            Log.d(TAG, response.request().url().toString());
            response.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> LoginResponse) {
                    Login login = LoginResponse.body();

                    User loggedIn_user = login.getUser();
                    Log.d(TAG, login.toString());
                    if (loggedIn_user.getToken() != null) {
                        loggedIn_user.setSign_up_stage(Constants.Picture);
                        boolean saved = loggedIn_user.save(getActivity().getApplicationContext());
                        if (saved) {
                            Bundle bundle = new Bundle();
                            Gson gson = new Gson();
                            String user_json = gson.toJson(loggedIn_user);
                            bundle.putString("user", user_json);
                            nextAction(bundle);
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
                public void onFailure(Call<Login> checkEmailCall, Throwable t) {
                    Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                    Log.d(TAG + " onFailure", t.toString());
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
        }
    }

    public boolean validated() {
        passwordText = password.getText().toString();

        return !passwordText.equals("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_btn:
                continue_action();
                break;
        }
    }
}
