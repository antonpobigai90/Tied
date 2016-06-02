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
public class TerritoryFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = TerritoryFragment.class
            .getSimpleName();

    private Button continue_btn, add;

    private SignUpFragmentListener mListener;

    private ProgressBar progressBar;

    ArrayList<String> territories = new ArrayList<String>();

    public TerritoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_territory, container, false);
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
            mListener.onFragmentInteraction(Constants.SalesRep, bundle);
        }
    }

    public void initComponent(View view) {

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        add = (Button) view.findViewById(R.id.add);
        continue_btn = (Button) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                continue_action();
                break;
        }
    }

    public boolean validated(){
        territories.add("Texas");
        territories.add("Dalax");
        return territories.size() > 0;
    }

    public void continue_action(){
        if(validated()){
            progressBar.setVisibility(View.VISIBLE);

            Bundle bundle = getArguments();

            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            final User user = gson.fromJson(user_json, User.class);
            user.setTerritories(territories);
            user.setSign_up_stage(Constants.SalesRep);

            SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
            Call<UpdateUser> response = signUpApi.updateUser(user);
            response.enqueue(new Callback<UpdateUser>() {
                @Override
                public void onResponse(Call<UpdateUser> call, Response<UpdateUser> UpdateUserResponse) {
                    UpdateUser UpdateUser = UpdateUserResponse.body();
                    Log.d(TAG +" onResponse", UpdateUserResponse.body().toString());
                    if(UpdateUser.isSuccess()){
                        Bundle bundle = new Bundle();
                        boolean saved = user.save(getActivity().getApplicationContext());
                        if(saved){
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            bundle.putString(Constants.USER, json);
                            progressBar.setVisibility(View.INVISIBLE);
                            nextAction(bundle);
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
}
