package com.tied.android.tiedapp.ui.activities.lines;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.lines.ClientFragment;
import com.tied.android.tiedapp.ui.fragments.lines.GeneralFragment;
import com.tied.android.tiedapp.ui.fragments.lines.GoalFragment;
import com.tied.android.tiedapp.ui.fragments.lines.RevenueFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;

/**
 * Created by Daniel on 5/3/2016.
 */
public class AddLinesActivity extends AppCompatActivity implements FragmentIterationListener, View.OnClickListener{

    public static final String TAG = AddLinesActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private Bundle bundle;
    private User user;

    LinearLayout general_tab, revenue_tab, goal_tab, client_tab,tab_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);

        initComponent();
    }

    private void initComponent() {

        general_tab = (LinearLayout) findViewById(R.id.general_tab);
        revenue_tab = (LinearLayout) findViewById(R.id.revenue_tab);
        goal_tab = (LinearLayout) findViewById(R.id.goal_tab);
        client_tab = (LinearLayout) findViewById(R.id.client_tab);
        tab_bar = (LinearLayout) findViewById(R.id.tab_bar);

        revenue_tab.setOnClickListener(this);
        general_tab.setOnClickListener(this);
        client_tab.setOnClickListener(this);
        goal_tab.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        if (mViewPager != null) {
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.setCurrentItem(0);
            selectTab(tab_bar, 0);
        }

        onCustomSelected(mViewPager);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.general_tab:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.revenue_tab:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.goal_tab:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.client_tab:
                mViewPager.setCurrentItem(3);
                break;
        }
    }

    protected void onCustomSelected(ViewPager vpPager){
        // Attaching the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
//                Toast.makeText(AddLinesActivity.this,"Selected page position: " + position, Toast.LENGTH_SHORT).show();
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
                    title.setTextColor(getResources().getColor(R.color.white2));
                }else{
                    indicator.setVisibility(View.VISIBLE);
                    title.setTextColor(getResources().getColor(R.color.white));
                }
                index++;
            }
        }
    }

    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Log.d(TAG, "position : "+position);
            switch (position){
                case 0:
                    fragment = new GeneralFragment();
                    break;
                case 1:
                    fragment = new RevenueFragment();
                    break;
                case 2:
                    fragment = new GoalFragment();
                    break;
                case 3:
                    fragment = new ClientFragment();
                    break;
            }
            assert fragment != null;
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
