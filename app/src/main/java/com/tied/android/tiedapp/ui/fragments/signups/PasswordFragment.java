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
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.SignUpLogin;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
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

    private FragmentIterationListener mListener;

//    private Button continue_btn;
    private LinearLayout back_btn;
    private RelativeLayout continue_btn;
    LinearLayout alert_valid_password;
    private EditText password;

    private String passwordText;
    private Bundle bundle;

    public static Fragment newInstance (Bundle bundle) {
        Fragment fragment=new PasswordFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

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

        bundle = getArguments();

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
        if (context instanceof FragmentIterationListener) {
            mListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action, Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action, bundle);
        }
    }

    public void continue_action() {
        DialogUtils.displayProgress(getActivity());
        Gson gson = new Gson();
        final String user_json = bundle.getString(Constants.USER_DATA);
        final User user = gson.fromJson(user_json, User.class);
        Log.d(TAG, user.toString());
        Call<SignUpLogin> response = MainApplication.createService(SignUpApi.class)
                .LoginSignUpUser(user.getEmail(), passwordText, Constants.Picture);
        Log.d(TAG, response.request().url().toString());
        response.enqueue(new Callback<SignUpLogin>() {
            @Override
            public void onResponse(Call<SignUpLogin> call, Response<SignUpLogin> LoginResponse) {
                if (getActivity() == null) return;
                SignUpLogin signUpLogin = LoginResponse.body();
                User loggedIn_user = signUpLogin.getUser();
                Log.d(TAG, signUpLogin.toString());
                if (loggedIn_user.getToken() != null) {
                    loggedIn_user.setSign_up_stage(Constants.Picture);
                    loggedIn_user.setPassword(passwordText);
                    loggedIn_user.setAvatar(user.getAvatar());
                    loggedIn_user.setFirst_name(user.getFirst_name());
                    loggedIn_user.setLast_name(user.getLast_name());
                    boolean saved = loggedIn_user.save(getActivity().getApplicationContext());
                    if (saved) {
                        Bundle bundle = new Bundle();
                        Gson gson = new Gson();
                        String user_json = gson.toJson(loggedIn_user);
                        bundle.putString(Constants.USER_DATA, user_json);
                        nextAction(Constants.Picture, bundle);
                    } else {
                        Toast.makeText(getActivity(), "user not save", Toast.LENGTH_LONG).show();
                    }
                    DialogUtils.closeProgress();
                } else {
                    DialogUtils.closeProgress();
                    Toast.makeText(getActivity(), "user not created", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpLogin> checkEmailCall, Throwable t) {
                DialogUtils.closeProgress();
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
                nextAction(Constants.EmailSignUp,bundle);
                break;
        }
    }
}
