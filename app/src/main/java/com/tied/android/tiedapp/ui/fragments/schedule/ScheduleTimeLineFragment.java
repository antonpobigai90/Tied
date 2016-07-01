package com.tied.android.tiedapp.ui.fragments.schedule;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.activities.ActivityFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentInterationListener;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ScheduleTimeLineFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = ActivityFragment.class
            .getSimpleName();

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private Bundle bundle;
    private User user;

    public FragmentInterationListener mListener;

    TextView txt_emp,txt_you, indicator_emp,indicator_you;
    LinearLayout emp_tab, you_tab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_timeline,null);

        initComponent(view);
        Log.d(TAG, "AM HERE AGAIN");
        return view;
    }

    public void initComponent(View view) {
        // Set up the ViewPager with the sections adapter.

        bundle = getArguments();

        txt_emp = (TextView) view.findViewById(R.id.txt_emp);
        txt_you = (TextView) view.findViewById(R.id.txt_you);
        indicator_emp = (TextView) view.findViewById(R.id.indicator_emp);
        indicator_you = (TextView) view.findViewById(R.id.indicator_you);
        emp_tab = (LinearLayout) view.findViewById(R.id.emp_tab);
        you_tab = (LinearLayout) view.findViewById(R.id.you_tab);

        emp_tab.setOnClickListener(this);
        you_tab.setOnClickListener(this);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

        mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        if (mViewPager != null) {
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.setCurrentItem(0);
            indicator_you.setVisibility(View.VISIBLE);
            txt_you.setTextColor(getResources().getColor(R.color.button_bg));
        }

        onCustomSelected(mViewPager);

        Log.d(TAG, "am her 1");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.emp_tab:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.you_tab:
                mViewPager.setCurrentItem(0);
                break;
        }
    }

    public void onCustomSelected(ViewPager vpPager){
        // Attaching the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(getActivity(),"Selected page position: " + position, Toast.LENGTH_SHORT).show();

                switch(position){
                    case 0:
                        indicator_you.setVisibility(View.VISIBLE);
                        indicator_emp.setVisibility(View.GONE);

                        txt_you.setTextColor(getResources().getColor(R.color.button_bg));
                        txt_emp.setTextColor(getResources().getColor(R.color.semi_transparent_black));
                        break;
                    case 1:
                        indicator_emp.setVisibility(View.VISIBLE);
                        indicator_you.setVisibility(View.GONE);

                        txt_you.setTextColor(getResources().getColor(R.color.semi_transparent_black));
                        txt_emp.setTextColor(getResources().getColor(R.color.button_bg));
                        break;

                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "am her 2");
        if(mPagerAdapter == null){
            mPagerAdapter = new PagerAdapter(getFragmentManager());
        }
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



    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Log.d(TAG, "position : "+position);

            fragment = ScheduleListFragment.newInstance(position);

            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
