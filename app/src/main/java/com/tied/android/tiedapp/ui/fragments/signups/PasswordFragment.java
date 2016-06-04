package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.auth.SignUpLogin;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;
import com.tied.android.tiedapp.util.Utility;

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

//    private Button continue_btn;
    private LinearLayout back_btn;
    private RelativeLayout continue_btn;
    LinearLayout alert_valid_password;
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
        back_btn = (LinearLayout) view.findViewById(R.id.back_layout);
        continue_btn = (RelativeLayout)view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);

        alert_valid_password = (LinearLayout) view.findViewById(R.id.alert_valid);
        alert_valid_password.setVisibility(View.GONE);
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
            mListener.onFragmentInteraction(action, bundle);
        }
    }

    public void continue_action() {

        Bundle bundle = getArguments();

        Gson gson = new Gson();
        String user_json = bundle.getString("user");
        User user = gson.fromJson(user_json, User.class);
        SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
        Call<SignUpLogin> response = signUpApi.LoginSignUpUser(user.getEmail(), passwordText, Constants.Picture);
        Log.d(TAG, response.request().url().toString());
        response.enqueue(new Callback<SignUpLogin>() {
            @Override
            public void onResponse(Call<SignUpLogin> call, Response<SignUpLogin> LoginResponse) {
                SignUpLogin signUpLogin = LoginResponse.body();

                User loggedIn_user = signUpLogin.getUser();
                Log.d(TAG, signUpLogin.toString());
                if (loggedIn_user.getToken() != null) {
                    loggedIn_user.setSign_up_stage(Constants.Picture);
                    boolean saved = loggedIn_user.save(getActivity().getApplicationContext());
                    if (saved) {
                        Bundle bundle = new Bundle();
                        Gson gson = new Gson();
                        String user_json = gson.toJson(loggedIn_user);
                        bundle.putString("user", user_json);
                        nextAction(Constants.Picture, bundle);
                    } else {
                        Toast.makeText(getActivity(), "user not save", Toast.LENGTH_LONG).show();
                    }
                    Log.d(TAG, loggedIn_user.toString());
                } else {
                    Toast.makeText(getActivity(), "user not created", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpLogin> checkEmailCall, Throwable t) {
                Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                Log.d(TAG + " onFailure", t.toString());
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_btn:
                passwordText = password.getText().toString();
                if (passwordText.length() == 0) {
                    alert_valid_password.setVisibility(View.VISIBLE);
                    Utility.moveViewToScreenCenter( alert_valid_password, Utility.getResourceString(getActivity(), R.string.alert_valide_password));
                } else {
                    continue_action();
                }
                break;
            case R.id.back_layout:
                Bundle bundle = getArguments();
                nextAction(Constants.EmailSignUp,bundle);
                break;
        }
    }
}
