package com.tied.android.tiedapp.ui.fragments.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.Count;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.activities.client.SelectClientActivity;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.DialogUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateScheduleFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = CreateScheduleFragment.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    private TextView txt_create_schedule;

    private FragmentIterationListener fragmentIterationListener;

    public CreateScheduleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentIterationListener) {
            fragmentIterationListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action, Bundle bundle) {
        if (fragmentIterationListener != null) {
            fragmentIterationListener.OnFragmentInteractionListener(action,bundle);
        }
    }

    public void initComponent(View view) {
        txt_create_schedule = (TextView) view.findViewById(R.id.txt_create_schedule);
        txt_create_schedule.setOnClickListener(this);

        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            user = gson.fromJson(user_json, User.class);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_create_schedule:
                if(!Client.isClientCreated(getActivity().getApplicationContext())){
                    checkIfClientExist(user);
                }else{
                    Intent intent = new Intent(getActivity(), SelectClientActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                break;
        }
    }

    public void checkIfClientExist(User user){
        DialogUtils.displayProgress(getActivity());
        ClientApi clientApi = MainApplication.getInstance().getRetrofit().create(ClientApi.class);
        Call<Count> response = clientApi.getClientsCount(user.getToken());
        response.enqueue(new Callback<Count>() {
            @Override
            public void onResponse(Call<Count> call, Response<Count> countResponse) {
                if (getActivity() == null) return;
                DialogUtils.closeProgress();
                Count count = countResponse.body();
                Log.d(TAG + "Count", count.toString());
                if (count != null && count.isAuthFailed()){
                    User.LogOut(getActivity());
                } else if (count != null && count.isSuccess()) {
                    if(count.getCount() > 0){
                        Intent intent = new Intent(getActivity(), SelectClientActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        nextAction(Constants.ClientAdd, bundle);
                    }
                } else {
                    nextAction(Constants.ClientAdd, bundle);
                }
            }
            @Override
            public void onFailure(Call<Count> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }
}