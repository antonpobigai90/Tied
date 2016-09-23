package com.tied.android.tiedapp.ui.activities;

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
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.LinesFragment;
import com.tied.android.tiedapp.ui.fragments.TerritoriesFragment;
import com.tied.android.tiedapp.util.MyUtils;

public class LinesAndTerritories extends AppCompatActivity implements  View.OnClickListener {

    public static final String TAG = LinesAndTerritories.class
            .getSimpleName();

    public ViewPager mViewPager;
    public PagerAdapter mPagerAdapter;
    private Bundle bundle;
    private User user;

    LinearLayout back_layout;
    LinearLayout lines_tab, territories_tab, tab_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_lines_territories);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        initComponent();

        if (bundle.getBoolean(Constants.SHOW_TERRITORY)) {
            mViewPager.setCurrentItem(1);
        }
    }

    private void initComponent() {
        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);

        lines_tab = (LinearLayout) findViewById(R.id.lines_tab);
        territories_tab = (LinearLayout) findViewById(R.id.territories_tab);
        tab_bar = (LinearLayout) findViewById(R.id.tab_bar);

        lines_tab.setOnClickListener(this);
        territories_tab.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mPagerAdapter = new PagerAdapter(LinesAndTerritories.this.getSupportFragmentManager());
        if (mViewPager != null) {
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.setCurrentItem(0);
            selectTab(tab_bar, 0);
        }
        onCustomSelected(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.lines_tab:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.territories_tab:
                mViewPager.setCurrentItem(1);
                break;
        }
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

    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = new LinesFragment();
                    break;
                case 1:
                    fragment = new TerritoriesFragment();
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
