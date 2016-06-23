package com.tied.android.tiedapp.ui.fragments.profile;

/**
 * Created by Emmanuel on 6/17/2016.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.activities.ProfileActivity;
import com.tied.android.tiedapp.ui.listeners.ProfileFragmentListener;
import com.tied.android.tiedapp.util.DemoData;

import me.relex.circleindicator.CircleIndicator;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = ProfileFragment.class
            .getSimpleName();


    public ImageView avatar, img_edit;

    public ProfileFragmentListener mListener;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private Bundle bundle;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initComponent(view);
        Log.d(TAG, "AM HERE AGAIN");
        return view;
    }

    public void initComponent(View view) {

        mPagerAdapter = new PagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mPagerAdapter);
        mPagerAdapter.notifyDataSetChanged();
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);

        img_edit = (ImageView) view.findViewById(R.id.img_edit);
        img_edit.setOnClickListener(this);

        bundle = ((ProfileActivity) getActivity()).bundle;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileFragmentListener) {
            mListener = (ProfileFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(Constants.EditProfile, bundle);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_edit:
                nextAction(bundle);
                break;
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Log.d(TAG, "position : "+position);
            switch (position){
                case 0:
                    fragment = new AvatarProfileFragment();
                    ((ProfileActivity) getActivity()).profileFragment =  fragment;
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