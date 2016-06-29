package com.tied.android.tiedapp.ui.fragments.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.activities.client.ClientActivity;
import com.tied.android.tiedapp.ui.listeners.FragmentInterationListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddActivityFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = AddActivityFragment.class
            .getSimpleName();

    private Bundle bundle;


    private TextView txt_cancel, txt_add_client;

    private FragmentInterationListener fragmentInterationListener;

    public AddActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_activity, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInterationListener) {
            fragmentInterationListener = (FragmentInterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action, Bundle bundle) {
        if (fragmentInterationListener != null) {
            fragmentInterationListener.OnFragmentInteractionListener(action,bundle);
        }
    }

    public void initComponent(View view) {

        txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
        txt_add_client = (TextView) view.findViewById(R.id.txt_add_client);

        bundle = getArguments();

        txt_cancel.setOnClickListener(this);
        txt_add_client.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_cancel:
                nextAction(Constants.CreateSchedule, bundle);
                break;
            case R.id.txt_add_client:
                Intent intent = new Intent(getActivity(), ClientActivity.class);
                startActivity(intent);
                break;
        }
    }
}