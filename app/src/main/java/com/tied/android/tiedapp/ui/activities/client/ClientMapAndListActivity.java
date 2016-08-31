package com.tied.android.tiedapp.ui.activities.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.client.ClientsListFragment;
import com.tied.android.tiedapp.ui.fragments.client.ClientsMapFragment;
import com.tied.android.tiedapp.util.MyUtils;

public class ClientMapAndListActivity extends AppCompatActivity implements  View.OnClickListener {

    public static final String TAG = ClientMapAndListActivity.class
            .getSimpleName();

    public ViewPager mViewPager;
    public PagerAdapter mPagerAdapter;
    private Bundle bundle;
    private User user;

    LinearLayout back_layout, tab_container;
    ImageView img_list_clients, img_map_clients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list_layout);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
        initComponent();

    }

    private void initComponent() {
        tab_container = (LinearLayout) findViewById(R.id.tab_container);
        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);

        img_list_clients = (ImageView) findViewById(R.id.img_list_clients);
        img_map_clients = (ImageView) findViewById(R.id.img_map_clients);

        img_list_clients.setOnClickListener(this);
        img_map_clients.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mPagerAdapter = new PagerAdapter(ClientMapAndListActivity.this.getSupportFragmentManager());
        if (mViewPager != null) {
            mViewPager.setAdapter(mPagerAdapter);
            if (bundle.getBoolean(Constants.CLIENT_LIST)){
                mViewPager.setCurrentItem(1);
                selectTab(tab_container, 1);
            }else{
                mViewPager.setCurrentItem(0);
                selectTab(tab_container, 0);
            }

        }
        onCustomSelected(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.img_map_clients:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.img_list_clients:
                mViewPager.setCurrentItem(1);
                break;
        }
    }

    public void selectTab(LinearLayout tab_bar, int position){
        switch (position){
            case 0:
                img_map_clients.setImageResource(R.drawable.ic_map_active);
                img_list_clients.setImageResource(R.drawable.ic_list_inactive);
                break;
            case 1:
                img_map_clients.setImageResource(R.drawable.ic_map_inactive);
                img_list_clients.setImageResource(R.drawable.ic_list_active);
                break;
            default:;
        }
    }

    public void onCustomSelected(ViewPager vpPager){
        // Attaching the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
//                Toast.makeText(LinesAndTerritories.this,"Selected page position: " + position, Toast.LENGTH_SHORT).show();
                selectTab(tab_container, position);
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

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = new ClientsMapFragment();
                    break;
                case 1:
                    fragment = new ClientsListFragment();
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
