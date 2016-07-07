package com.tied.android.tiedapp.ui.fragments.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.schedule.DateRange;
import com.tied.android.tiedapp.objects.schedule.ScheduleDate;
import com.tied.android.tiedapp.objects.schedule.TimeRange;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.activities.ActivityFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public FragmentIterationListener mListener;

    LinearLayout week_tab, today_tab,month_tab,next_week_tab, tab_bar;


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

        tab_bar = (LinearLayout) view.findViewById(R.id.tab_bar);
        week_tab = (LinearLayout) view.findViewById(R.id.week_tab);
        today_tab = (LinearLayout) view.findViewById(R.id.today_tab);
        next_week_tab = (LinearLayout) view.findViewById(R.id.next_week_tab);
        month_tab = (LinearLayout) view.findViewById(R.id.month_tab);

        week_tab.setOnClickListener(this);
        today_tab.setOnClickListener(this);
        month_tab.setOnClickListener(this);
        next_week_tab.setOnClickListener(this);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

        mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        if (mViewPager != null) {
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.setCurrentItem(0);
            selectTab(tab_bar, 0);
        }

        onCustomSelected(mViewPager);

        Log.d(TAG, "am her 1");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.week_tab:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.today_tab:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.next_week_tab:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.month_tab:
                mViewPager.setCurrentItem(3);
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

                selectTab(tab_bar, position);
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

    public void selectTab(LinearLayout tab_bar, int position){
        int index = 0;
        for(int i = 0; i < tab_bar.getChildCount(); i++){
            if(tab_bar.getChildAt(i) instanceof LinearLayout){
                LinearLayout child = (LinearLayout) tab_bar.getChildAt(i);
                Log.d(TAG, "am here != position "+child.getChildAt(i));
                TextView title = (TextView) child.getChildAt(0);
                TextView indicator = (TextView) child.getChildAt(1);
                if(position != index){
                    indicator.setVisibility(View.GONE);
                    title.setTextColor(getResources().getColor(R.color.semi_transparent_black));
                }else{
                    indicator.setVisibility(View.VISIBLE);
                    title.setTextColor(getResources().getColor(R.color.button_bg));
                }
                index++;
            }
        }
    }


    public Pair<String,String> getWeekRange(int year, int week_no) {

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week_no);
        Date monday = cal.getTime();

        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week_no);
        Date sunday = cal.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return new Pair<String,String>(sdf.format(monday), sdf.format(sunday));
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {

            Log.d(TAG, "position : "+position);
            TimeRange timeRange = null;
            DateRange dateRange = null;
            Calendar cal = Calendar.getInstance();
            Date now = cal.getTime();
            Pair<String,String> date_range_pairs = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String  today = sdf.format(now);
            int index = 0;
            switch (position){
                case 1:
                    index = 1;
                    date_range_pairs = getWeekRange(cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR));
                    timeRange = new TimeRange("00:00","23:59");
                    dateRange = new DateRange(date_range_pairs.first,date_range_pairs.second);
                    break;
                case 2:
                    index = 2;
                    timeRange = new TimeRange("00:00","23:59");
                    dateRange = new DateRange(today,today);
                    break;
                case 3:
                    index = 3;
                    date_range_pairs = getWeekRange(cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR) + 1);
                    timeRange = new TimeRange("00:00","23:59");
                    dateRange = new DateRange(date_range_pairs.first,date_range_pairs.second);
                    break;
                case 4:
                    index = 2;
                    timeRange = new TimeRange("00:00","23:59");
                    dateRange = new DateRange("2016-07-01","2016-07-31");
                    break;
            }

            ScheduleDate scheduleDate = new ScheduleDate(timeRange, dateRange);
            Gson gson = new Gson();
            String scheduleDate_json = gson.toJson(scheduleDate);
            bundle.putString(Constants.SCHEDULE_DATE_FILTER, scheduleDate_json);
            bundle.putInt(Constants.SCHEDULE_DATA_FILTER_INDEX, index);
            Fragment fragment = ScheduleListFragment.newInstance();
            fragment.setArguments(bundle);
            return fragment;
        }
        @Override
        public int getCount() {
            return 4;
        }
    }
}
