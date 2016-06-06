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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.auth.LoginUser;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.schedule.ScheduleActivity;
import com.tied.android.tiedapp.ui.activities.signups.SignInActivity;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;
import com.tied.android.tiedapp.util.DialogUtils;
import com.tied.android.tiedapp.util.Utility;

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

    ImageView signin, signup;
    Context context;
    private EditText email, password;

    LinearLayout alert_valid;

    private String emailText, passwordText;

    private TextView forgot_password;

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

        context = getActivity();

        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        forgot_password = (TextView) view.findViewById(R.id.forgot_password);

        signin = (ImageView) view.findViewById(R.id.signin);
        signup = (ImageView) view.findViewById(R.id.signup);

        signin.setOnClickListener(this);
        signup.setOnClickListener(this);
        forgot_password.setOnClickListener(this);

        alert_valid = (LinearLayout) view.findViewById(R.id.alert_valid);
        alert_valid.setVisibility(View.GONE);
    }

    public void continue_action(){
        DialogUtils.displayProgress(getActivity());
        SignUpApi signUpApi = ((SignInActivity) getActivity()).service;
        Call<LoginUser> response = signUpApi.LoginUser(emailText, passwordText);
        Log.d(TAG, response.request().url().toString());
        response.enqueue(new Callback<LoginUser>() {
            @Override
            public void onResponse(Call<LoginUser> call, Response<LoginUser> LoginResponse) {
                LoginUser LoginUser = LoginResponse.body();
                Log.d(TAG, LoginUser.toString());
                if (LoginUser.isSuccess()) {
                    User loggedIn_user = LoginUser.getUser();
                    loggedIn_user.setToken(LoginUser.getToken());
                    boolean saved = loggedIn_user.save(getActivity().getApplicationContext());
                    if (saved) {
                        Bundle bundle = new Bundle();
                        Gson gson = new Gson();
                        String user_json = gson.toJson(loggedIn_user);
                        Intent intent = new Intent(getActivity(), ScheduleActivity.class);
                        intent.putExtra(Constants.USER, user_json);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "user not save", Toast.LENGTH_LONG).show();
                    }
                    Log.d(TAG, loggedIn_user.toString());
                } else {
                    DialogUtils.closeProgress();
                    Toast.makeText(getActivity(), LoginUser.getMessage(), Toast.LENGTH_LONG).show();
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<LoginUser> checkEmailCall, Throwable t) {
                Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    public boolean validated(){

        emailText = email.getText().toString();
        passwordText = password.getText().toString();
        return emailText.contains("@");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signin:
                if (!Utility.isEmailValid(email.getText().toString())) {
                    alert_valid.setVisibility(View.VISIBLE);
                    Utility.moveViewToScreenCenter( alert_valid, Utility.getResourceString(context, R.string.alert_valide_email));
                } else if (password.getText().length() == 0) {
                    alert_valid.setVisibility(View.VISIBLE);
                    Utility.moveViewToScreenCenter( alert_valid, Utility.getResourceString(context, R.string.alert_valide_password));
                } else {
                    continue_action();
                }

                break;
            case R.id.forgot_password:
                mListener.onFragmentInteraction(Constants.Reset, null);
                break;
            case R.id.signup:
                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}