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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.auth.UpdateUser;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddBossNowFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = AddBossNowFragment.class
            .getSimpleName();

    private SignUpFragmentListener mListener;

    private Button continue_btn, later;
    private ProgressBar progressBar;

    private String emailText, passwordText;

    public AddBossNowFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_add_boss_now, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }


    public void initComponent(View view){

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        later = (Button) view.findViewById(R.id.via_app);
        continue_btn = (Button) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);
        later.setOnClickListener(this);
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

    public void continue_action() {
        if(validated()){
            progressBar.setVisibility(View.VISIBLE);

            Bundle bundle = getArguments();

            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            final User user = gson.fromJson(user_json, User.class);
            user.setSign_up_stage(Constants.CoWorkerCount);

            SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
            Call<UpdateUser> response = signUpApi.updateUser(user);
            response.enqueue(new Callback<UpdateUser>() {
                @Override
                public void onResponse(Call<UpdateUser> call, Response<UpdateUser> UpdateUserResponse) {
                    UpdateUser UpdateUser = UpdateUserResponse.body();
                    Log.d(TAG +" onFailure", UpdateUserResponse.body().toString());
                    if(UpdateUser.isSuccess()){
                        Bundle bundle = new Bundle();
                        boolean saved = user.save(getActivity().getApplicationContext());
                        if(saved){
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            bundle.putString(Constants.USER, json);
                            progressBar.setVisibility(View.INVISIBLE);
                            nextAction(Constants.CoWorkerCount, bundle);
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getActivity(), UpdateUser.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<UpdateUser> UpdateUserCall, Throwable t) {
                    Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                    Log.d(TAG +" onFailure", t.toString());
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }else{
            Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
        }
    }

    public boolean validated(){
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                continue_action();
                break;
            case R.id.via_app:
                progressBar.setVisibility(View.VISIBLE);
                Bundle bundle = getArguments();

                Gson gson = new Gson();
                String user_json = bundle.getString("user");
                final User user = gson.fromJson(user_json, User.class);
                user.setSign_up_stage(Constants.CoWorkerCount);
                boolean saved = user.save(getActivity().getApplicationContext());
                if(saved){
                    String json = gson.toJson(user);
                    bundle.putString(Constants.USER, json);
                    progressBar.setVisibility(View.INVISIBLE);
                    nextAction(Constants.CoWorkerCount, bundle);
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}