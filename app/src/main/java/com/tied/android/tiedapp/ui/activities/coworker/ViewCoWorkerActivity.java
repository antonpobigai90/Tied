package com.tied.android.tiedapp.ui.activities.coworker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.coworker.ActivityFragment;
import com.tied.android.tiedapp.ui.fragments.coworker.GeneralFragment;
import com.tied.android.tiedapp.util.MyUtils;

public class ViewCoWorkerActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = ViewCoWorkerActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    LinearLayout back_layout;
    LinearLayout general_tab, activity_tab, tab_bar;

    public ViewPager mViewPager;
    public PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coworker_view);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);

        general_tab = (LinearLayout) findViewById(R.id.general_tab);
        activity_tab = (LinearLayout) findViewById(R.id.activity_tab);
        tab_bar = (LinearLayout) findViewById(R.id.tab_bar);

        general_tab.setOnClickListener(this);
        activity_tab.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mPagerAdapter = new PagerAdapter(ViewCoWorkerActivity.this.getSupportFragmentManager());
        if (mViewPager != null) {
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.setCurrentItem(0);
            selectTab(tab_bar, 0);
        }

        onCustomSelected(mViewPager);

    }

    public void selectTab(LinearLayout tab_bar, int position){
        int index = 0;
        for(int i = 0; i < tab_bar.getChildCount(); i++){
            if(tab_bar.getChildAt(i) instanceof LinearLayout){
                LinearLayout child = (LinearLayout) tab_bar.getChildAt(i);
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

    public void onCustomSelected(ViewPager vpPager){
        // Attaching the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
//                Toast.makeText(LinesAndTerritories.this,"Selected page position: " + position, Toast.LENGTH_SHORT).show();
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.general_tab:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.activity_tab:
                mViewPager.setCurrentItem(1);
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
            switch (position){
                case 0:
                    fragment = new GeneralFragment();
                    break;
                case 1:
                    fragment = new ActivityFragment();
                    break;
            }
            assert fragment != null;
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
