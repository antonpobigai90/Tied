package com.tied.android.tiedapp.ui.fragments.client;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.listeners.FragmentInterationListener;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class SelectClientDistanceFragment extends Fragment implements View.OnClickListener{

    public FragmentInterationListener mListener;
    private TextView txt_distance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_select_client_distance_view, container, false);
        initComponent(view);
        return view;
    }

    public void initComponent(View view) {

        txt_distance = (TextView) view.findViewById(R.id.distance_txt);
        txt_distance.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.txt_distance:
//
//                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInterationListener) {
            mListener = (FragmentInterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action,Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action, bundle);
        }
    }

}
