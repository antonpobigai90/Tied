package com.tied.android.tiedapp.ui.fragments.client;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.coworker.CoWorkerFilterActivity;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class MapFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = MapFragment.class
            .getSimpleName();

    public ViewPager mViewPager;
    public PagerAdapter mPagerAdapter;
    private Bundle bundle;
    private ClientsMapFragment clientsMapFragment;
    ClientsListFragment clientsListFragment;
    LinearLayout parent, back_layout;
    ImageView img_segment, img_filter, app_icon;
    User user;
    ArrayList<Client> clients;

    boolean bMap = true;
    EditText search;
    TextView search_button;
    Fragment fragment = null;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new MapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_client_list_layout, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.blue_status_bar));
        }

        initComponent(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initComponent(View view) {

        bundle = getArguments();

        app_icon = (ImageView) view.findViewById(R.id.app_icon);
        app_icon.setVisibility(View.VISIBLE);

        parent = (LinearLayout) view.findViewById(R.id.parent);
        parent.setBackgroundResource(R.drawable.background_blue);

        back_layout = (LinearLayout) view.findViewById(R.id.back_layout);
        back_layout.setVisibility(View.GONE);

        img_segment = (ImageView) view.findViewById(R.id.img_segment);
        img_segment.setOnClickListener(this);

        img_filter = (ImageView) view.findViewById(R.id.img_filter);
        img_filter.setOnClickListener(this);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

        mPagerAdapter = new PagerAdapter(getChildFragmentManager());
        if (mViewPager != null) {
            mViewPager.setAdapter(mPagerAdapter);
            if (bundle.getBoolean(Constants.CLIENT_LIST)){
                mViewPager.setCurrentItem(1);
                selectTab(1);
            }else{
                mViewPager.setCurrentItem(0);
                selectTab(0);
            }

        }
        onCustomSelected(mViewPager);
        img_segment.setBackgroundResource(R.drawable.map_active);

        search = (EditText) view.findViewById(R.id.search);
        search_button = (TextView) view.findViewById(R.id.search_button);
        search_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_segment:
                bMap = !bMap;
                if (bMap) {
                    mViewPager.setCurrentItem(0);

                    img_segment.setBackgroundResource(R.drawable.map_active);
                } else {
                    mViewPager.setCurrentItem(1);

                    img_segment.setBackgroundResource(R.drawable.list_active);
                }
                break;
            case R.id.img_filter:
                MainActivity.search_name = search.getText().toString();
                MyUtils.startRequestActivity(getActivity(), CoWorkerFilterActivity.class, Constants.ClientFilter, bundle);
                break;
            case R.id.search_button:
                if (search.getText().toString().length() > 3) {
//                    if (bMap) {
                        clientsMapFragment.loadClientsFilter(search.getText().toString());
//                    } else {
                        clientsListFragment.loadClientsFilter(search.getText().toString());
//                    }
                }
                break;
        }
    }

    public void selectTab(int position){
        switch (position){
            case 0:
                bMap = true;
                img_segment.setBackgroundResource(R.drawable.map_active);
                break;
            case 1:
                bMap = false;
                img_segment.setBackgroundResource(R.drawable.list_active);
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
//                Toast.makeText(LinesListActivity.this,"Selected page position: " + position, Toast.LENGTH_SHORT).show();
                selectTab(position);
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

            switch (position){
                case 0:
                    if(clientsMapFragment==null) {
                        clientsMapFragment=new ClientsMapFragment();
                    }
                    fragment = clientsMapFragment;
                    break;
                case 1:
                    if(clientsListFragment==null) {
                        clientsListFragment=new ClientsListFragment();
                    }
                    fragment = clientsListFragment;
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

    private void loadClients() {
        ClientLocation clientLocation = new ClientLocation();
        clientLocation.setDistance("0m");

        Coordinate coordinate = MyUtils.getCurrentLocation();
        if( coordinate == null ){
            coordinate = user.getOffice_address().getCoordinate();
        }
        clientLocation.setCoordinate(coordinate);

        final ClientApi clientApi =  MainApplication.createService(ClientApi.class);
        Call<ClientRes> response = clientApi.getClientsByLocation(user.getId(), clientLocation);
        response.enqueue(new retrofit2.Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if (getActivity()== null ) {
                    // Logger.write("null activity");
                    return;
                }
                //Logger.write("(((((((((((((((((((((((((((((999999");
                DialogUtils.closeProgress();
                ClientRes clientRes = resResponse.body();
                //Logger.write(clientRes.toString());
                try {
                    if (clientRes.isAuthFailed()) {
                        // User.LogOut(getActivity());
                    } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200) {
                        clients.addAll(clientRes.getClients());
                          /*
                            if (clients.size() > 0) {
                              clients = clients;
                                if (adapter != null) {
                                    adapter.listInit(clients);
                                }
                            }*/

                       // adapter.notifyDataSetChanged();
                    } else {
                        Logger.write("Error onResponse", clientRes.getMessage());
                    }
                }catch (Exception e) {
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ClientRes> call, Throwable t) {

                Logger.write(" onFailure", t.toString());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        clientsMapFragment.loadClientsFilter(search.getText().toString());
        clientsListFragment.loadClientsFilter(search.getText().toString());

//        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
