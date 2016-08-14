package com.tied.android.tiedapp.ui.fragments.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.AllScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.NextWeekScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.ThisMonthScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.ThisWeekScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.TodayScheduleFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ScheduleTimeLineFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = ScheduleTimeLineFragment.class
            .getSimpleName();

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private Bundle bundle;
    private User user;

    public FragmentIterationListener mListener;

    LinearLayout all_tab, today_tab,this_week_tab,next_week_tab, tab_bar, this_month, alert_edit_msg;
    HorizontalScrollView tab_scroll;
    List<Fragment> fragmentList = new ArrayList<Fragment>();


    public static Fragment newInstance (Bundle bundle) {
        Fragment fragment=new ScheduleTimeLineFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        fragmentList.add(new TodayScheduleFragment());
        fragmentList.add(new AllScheduleFragment());
        fragmentList.add(new ThisWeekScheduleFragment());
        fragmentList.add( new NextWeekScheduleFragment());
        fragmentList.add( new ThisMonthScheduleFragment());
    }

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
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tab_bar = (LinearLayout) view.findViewById(R.id.tab_bar);
        all_tab = (LinearLayout) view.findViewById(R.id.all_tab);
        today_tab = (LinearLayout) view.findViewById(R.id.today_tab);
        this_week_tab = (LinearLayout) view.findViewById(R.id.this_week_tab);
        next_week_tab = (LinearLayout) view.findViewById(R.id.next_week_tab);
        this_month = (LinearLayout) view.findViewById(R.id.this_month);
        tab_scroll = (HorizontalScrollView) view.findViewById(R.id.tab_scroll);

        alert_edit_msg = (LinearLayout) getActivity().findViewById(R.id.alert_edit_msg);
       // moveViewToScreenCenter( alert_edit_msg, "Your edit was successful");

        all_tab.setOnClickListener(this);
        today_tab.setOnClickListener(this);
        this_week_tab.setOnClickListener(this);
        next_week_tab.setOnClickListener(this);
        this_month.setOnClickListener(this);






        if (mViewPager != null) {
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.setCurrentItem(0);
            selectTab(tab_bar, 0);
        }
        onCustomSelected(mViewPager);
        Logger.write("It is created again");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_tab:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.today_tab:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.this_week_tab:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.next_week_tab:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.this_month:
                mViewPager.setCurrentItem(4);
                break;
        }
    }

    public static void moveViewToScreenCenter(final View view, String message )
    {
        view.setVisibility(View.VISIBLE);
        TextView txt_alert = (TextView) view.findViewById(R.id.info_msg);
        txt_alert.setText(message);

        TranslateAnimation anim = new TranslateAnimation( 0, 0 , 0, -130);
        anim.setFillAfter(true);
        anim.setDuration(2500);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.GONE);
//                view.setAnimation(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);

    }

    public void onCustomSelected(ViewPager vpPager){
        // Attaching the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
              //  Toast.makeText(getActivity(),"Selected page position: " + position, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "position real: "+position);
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

        /*if(mPagerAdapter == null){
            mPagerAdapter = new PagerAdapter(getFragmentManager());
        }
        if (context instanceof FragmentIterationListener) {
            mListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    public void nextAction(int action,Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action, bundle);
        }
    }

    public void selectTab(LinearLayout tab_bar, final int position){
        int index = 0;
        for(int i = 0; i < tab_bar.getChildCount(); i++){
            if(tab_bar.getChildAt(i) instanceof LinearLayout){
                LinearLayout child = (LinearLayout) tab_bar.getChildAt(i);
                final TextView title = (TextView) child.getChildAt(0);
                TextView indicator = (TextView) child.getChildAt(1);
                if(position != index){
                    indicator.setVisibility(View.GONE);
                    title.setTextColor(getResources().getColor(R.color.semi_transparent_black));
                }else{
                    indicator.setVisibility(View.VISIBLE);
                    title.setTextColor(getResources().getColor(R.color.button_bg));
                    ViewTreeObserver vto = tab_scroll.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
                        @Override
                        public void onGlobalLayout() {
                            tab_scroll.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            if(position == 4 || position == 3){
                                tab_scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                            }
                            else if(position == 0){
                                tab_scroll.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                            }
                            else{
                                tab_scroll.scrollTo((int) title.getScaleX(), 0);
                            }
                        }
                    });
                }
                index++;
            }
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

                fragment=fragmentList.get(position);


            fragment.setArguments(bundle);
            return fragment;
        }
        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
    private static int mCurCheckPosition=0;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }
        // Logger.write("femiiiiiiiiiiiiiiii");
    }
}
