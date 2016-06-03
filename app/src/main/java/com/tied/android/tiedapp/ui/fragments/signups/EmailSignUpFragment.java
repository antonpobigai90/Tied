package com.tied.android.tiedapp.ui.fragments.signups;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.auth.CheckEmail;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.activities.signups.WalkThroughActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class EmailSignUpFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = EmailSignUpFragment.class
            .getSimpleName();

    private SignUpFragmentListener mListener;

    private RelativeLayout continue_btn;
    private ImageView btn_close;

    private ProgressBar progressBar;
    private EditText email;

    private String emailText = "";

    public EmailSignUpFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_email, container, false);
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

    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            mListener.onFragmentInteraction(Constants.Password,bundle);
        }
    }

    public void initComponent(View view){

        email = (EditText) view.findViewById(R.id.email);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        btn_close = (ImageView) view.findViewById(R.id.img_close);
        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);
        btn_close.setOnClickListener(this);

        Bundle bundle = getArguments();
        if(bundle != null){
            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            User user = gson.fromJson(user_json, User.class);
            email.setText(user.getEmail());
        }
    }

    public void continue_action(){
        if(validated()){
            progressBar.setVisibility(View.VISIBLE);
            SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
            Call<CheckEmail> response = signUpApi.checkEmail(emailText);
            Log.d(TAG, response.request().url().toString());
            response.enqueue(new Callback<CheckEmail>() {
                @Override
                public void onResponse(Call<CheckEmail> call, Response<CheckEmail> checkEmailResponse) {
                    CheckEmail checkEmail = checkEmailResponse.body();

                    if(checkEmail.isSuccess()){
                        Bundle bundle = new Bundle();
                        User user = new User();
                        user.setEmail(emailText);
                        Gson gson = new Gson();
                        String user_json = gson.toJson(user);
                        bundle.putString("user", user_json);
                        nextAction(bundle);
                    }else{
                        Toast.makeText(getActivity(), checkEmail.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<CheckEmail> checkEmailCall, Throwable t) {
                    Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                    Log.d(TAG +" onFailure", t.toString());
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }else{
            Toast.makeText(getActivity(), "Invalid email", Toast.LENGTH_LONG).show();
        }

    }

    public boolean validated(){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        emailText = email.getText().toString();
        return emailText.matches(emailPattern);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                continue_action();
                break;
            case R.id.img_close:
                Intent intent = new Intent(getActivity(), WalkThroughActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}