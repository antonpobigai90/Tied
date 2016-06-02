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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class IndustryFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = IndustryFragment.class
            .getSimpleName();

    private Button continue_btn;

    private SignUpFragmentListener mListener;

    private ProgressBar progressBar;

    ArrayList<String> industries = new ArrayList<String>();

    public IndustryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_industry, container, false);
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
            mListener.onFragmentInteraction(Constants.AddBoss, bundle);
        }
    }

    public void initComponent(View view) {

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        continue_btn = (Button) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_btn:
                continue_action();
                break;
        }
    }

    public boolean validated() {
        industries.add("Technology");
        industries.add("Science");
        return industries.size() > 0;
    }

    public void continue_action() {
        if (validated()) {
            progressBar.setVisibility(View.VISIBLE);

            Bundle bundle = getArguments();

            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            final User user = gson.fromJson(user_json, User.class);
            user.setTerritories(industries);
            user.setSign_up_stage(Constants.AddBoss);

            SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
            Call<UpdateUser> response = signUpApi.updateUser(user);
            response.enqueue(new Callback<UpdateUser>() {
                @Override
                public void onResponse(Call<UpdateUser> call, Response<UpdateUser> UpdateUserResponse) {
                    UpdateUser UpdateUser = UpdateUserResponse.body();
                    Log.d(TAG + " onResponse", UpdateUserResponse.body().toString());
                    if (UpdateUser.isSuccess()) {
                        Bundle bundle = new Bundle();
                        boolean saved = user.save(getActivity().getApplicationContext());
                        if (saved) {
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            bundle.putString(Constants.USER, json);
                            progressBar.setVisibility(View.INVISIBLE);
                            nextAction(bundle);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), UpdateUser.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<UpdateUser> UpdateUserCall, Throwable t) {
                    Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                    Log.d(TAG + " onFailure", t.toString());
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
        }
    }
}
