package com.tied.android.tiedapp.ui.activities.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.client.AddClientFragment;
import com.tied.android.tiedapp.ui.fragments.client.ViewClientFragment;
import com.tied.android.tiedapp.ui.fragments.signups.TerritoryFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;

/**
 * Created by femi on 9/11/2016.
 */
public class ViewClientActivity extends AppCompatActivity implements View.OnClickListener, FragmentIterationListener {

    public static final String TAG = ClientInfo.class
            .getSimpleName();
    private User user;
    private Bundle bundle;

    LinearLayout back_layout;
    TextView fax,email,phone,street,city_state;
    String address;
    Client client;
    Fragment fragment, currentFragment;
    int fragment_index, currentFragmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        bundle = getIntent().getExtras();
        launchFragment(1,  bundle);

    }
    public void launchFragment(int pos, Bundle bundle) {
        fragment = null;
        fragment_index = pos;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_top);
        currentFragmentID=pos;
        switch (pos) {
            case 1:
                fragment=new ViewClientFragment();
               break;
            default:
                finish();
        }

        fragment.setArguments(bundle);
        if (fragment != null) {
            Log.d(TAG, getSupportFragmentManager().getBackStackEntryCount() + "");
            Logger.write("TAGGGG: "+ fragment.getClass().getName());
            ft.replace(R.id.fragment_place, fragment, fragment.getClass().getName());
            ft.commit();
        }
        currentFragment=fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                onBackPressed();
                break;

        }
    }

    public void goBack(View v) {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {

    }
}


