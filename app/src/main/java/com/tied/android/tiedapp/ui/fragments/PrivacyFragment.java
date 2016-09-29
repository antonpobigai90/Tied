package com.tied.android.tiedapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;

/**
 * Created by hitendra on 9/9/2016.
 */
public class PrivacyFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout rlSales, rlDailyActivities, rlClients, rlTeritorry, rlLine, rlIndustry;
    public FragmentIterationListener mListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_privacy, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        rlSales = (RelativeLayout) rootView.findViewById(R.id.rlSales);
        rlSales.setOnClickListener(this);
        rlDailyActivities = (RelativeLayout) rootView.findViewById(R.id.rlDailyActivities);
        rlDailyActivities.setOnClickListener(this);
        rlClients = (RelativeLayout) rootView.findViewById(R.id.rlClients);
        rlClients.setOnClickListener(this);
        rlTeritorry = (RelativeLayout) rootView.findViewById(R.id.rlTeritorry);
        rlTeritorry.setOnClickListener(this);
        rlLine = (RelativeLayout) rootView.findViewById(R.id.rlLine);
        rlLine.setOnClickListener(this);
        rlIndustry = (RelativeLayout) rootView.findViewById(R.id.rlIndustry);
        rlIndustry.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        nextAction(Constants.PRIVACY_SALES, new Bundle());
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
}
