package com.tied.android.tiedapp.ui.fragments.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class NotificationProfileFragment extends Fragment implements View.OnClickListener{

    public FragmentIterationListener mListener;

    private Bundle bundle;
    private ImageView img_close;

    public static Fragment newInstance (Bundle bundle) {
        Fragment fragment=new NotificationProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_notifications, container, false);
        initComponent(view);
        return view;
    }

    public void initComponent(View view) {
        img_close = (ImageView) view.findViewById(R.id.img_close);
        img_close.setOnClickListener(this);
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

    public void nextAction(int action,Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action, bundle);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_close:
                nextAction(Constants.Profile,bundle);
                break;
        }
    }
}
