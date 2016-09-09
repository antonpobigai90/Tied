package com.tied.android.tiedapp.ui.fragments.profile;

/**
 * Created by Emmanuel on 6/17/2016.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.activities.ProfileActivity;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.DemoData;

import me.relex.circleindicator.CircleIndicator;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = ProfileFragment.class
            .getSimpleName();

    public ImageView avatar;
    private LinearLayout back_layout;


    private Button btnNotifications, btnEditPersonalInfo, btnChangePassword, btnIndustries;

    public FragmentIterationListener mListener;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager vpProfile;
    private PagerAdapter mPagerAdapter;
    private CircleIndicator circleIndicator;
    private Bundle bundle;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new ProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_new, container, false);
        initComponent(view);
        Log.d(TAG, "AM HERE AGAIN");
        return view;
    }

    public void initComponent(View view) {
        // Set up the ViewPager with the sections adapter.

        vpProfile = (ViewPager) view.findViewById(R.id.vpProfile);
        circleIndicator = (CircleIndicator) view.findViewById(R.id.circleIndicator);
        vpProfile.setAdapter(mPagerAdapter);
        circleIndicator.setViewPager(vpProfile);

        btnNotifications = (Button) view.findViewById(R.id.btnNotifications);
        btnNotifications.setOnClickListener(this);
        btnEditPersonalInfo = (Button) view.findViewById(R.id.btnEditPersonalInfo);
        btnEditPersonalInfo.setOnClickListener(this);

        btnChangePassword = (Button) view.findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(this);

        btnIndustries = (Button) view.findViewById(R.id.btnIndustries);
        btnIndustries.setOnClickListener(this);

        back_layout = (LinearLayout) view.findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);


        bundle = getArguments();

        Log.d(TAG, "am her 1");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "am her 2");
        if (mPagerAdapter == null) {
            mPagerAdapter = new PagerAdapter(getFragmentManager());
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEditPersonalInfo:
                nextAction(Constants.EditProfile, bundle);
                break;
            case R.id.back_layout:
                nextAction(Constants.CreateSchedule, bundle);
                break;
            case R.id.btnNotifications:
                nextAction(Constants.Notification, bundle);
                break;
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Log.d(TAG, "position : " + position);
            switch (position) {
                case 0:
                    fragment = new AvatarProfileFragment();
                    ((ProfileActivity) getActivity()).profileFragment = fragment;
                    break;
                case 1:
                    fragment = new GoalProfileFragment();
                    break;
                case 2:
                    fragment = new SalesProfileFragment();
                    break;
            }
            assert fragment != null;
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return DemoData.profile_layout.length;
        }
    }
}