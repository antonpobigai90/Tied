package com.tied.android.tiedapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.services.LocationService;
import com.tied.android.tiedapp.ui.activities.coworker.CoWorkerActivity;
import com.tied.android.tiedapp.ui.activities.goal.LineGoalActivity;
import com.tied.android.tiedapp.ui.activities.sales.ActivityGroupedSales;
import com.tied.android.tiedapp.ui.activities.territories.ActivityTerritories;
import com.tied.android.tiedapp.ui.dialogs.DialogNewClient;
import com.tied.android.tiedapp.ui.fragments.DailyStatsFragment;
import com.tied.android.tiedapp.ui.fragments.LinesFragment;
import com.tied.android.tiedapp.ui.fragments.client.ClientAddFragment;
import com.tied.android.tiedapp.ui.fragments.client.MapFragment;
import com.tied.android.tiedapp.ui.fragments.profile.AvatarProfileFragment;
import com.tied.android.tiedapp.ui.fragments.profile.ProfileFragment;
import com.tied.android.tiedapp.ui.fragments.profile.ProfileFragment1;
import com.tied.android.tiedapp.ui.fragments.sales.SaleFragment;
import com.tied.android.tiedapp.ui.fragments.sales.SaleViewAllFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.CreateScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.ScheduleAppointmentsFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.ScheduleSuggestionFragment;
import com.tied.android.tiedapp.ui.fragments.signups.IndustryFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.ui.listeners.ImageReadyForUploadListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * Created by Daniel on 5/3/2016.
 */
public class MainActivity extends FragmentActivity implements FragmentIterationListener, View.OnClickListener {

    public static final String TAG = MainActivity.class
            .getSimpleName();

    public ImageView img_user_picture, add, drawerUserPicture;
    private ImageReadyForUploadListener imageReadyForUploadListener;

    private Fragment fragment = null;
    public Fragment profileFragment = null;
    private int fragment_index = 0;
    //LinearLayout relativeLayout , tab_bar;
    private LinearLayout  map_tab,  activity_layout, add_layout, more_layout, tab_actvity_schedule, alert_edit_msg, sale_tab;
    private RelativeLayout invite_menu;
    private TextView txt_schedules, txt_activities, info_msg, drawerFullName, drawerEmail;

    public Bitmap bitmap;

    public Uri imageUri = null, outputUri = null;
    Map<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();

    User user;
    public Bundle bundle;

    // Code for our image picker select action.
    public final int IMAGE_PICKER_SELECT = 999;

    // Activity result key for camera
    public final int REQUEST_TAKE_PHOTO = 11111;

    public Retrofit retrofit;
    public SignUpApi service;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    int currentFragmentID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_main);

        user = User.getUser(getApplicationContext());
        if(user == null){
            User.LogOut(getApplicationContext());
            finish();
        }

        startService(new Intent(this, LocationService.class));
        navigationView=(NavigationView)findViewById(R.id.navigation_view);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);

        //relativeLayout = (LinearLayout) findViewById(R.id.linearLayout);
       // tab_bar = (LinearLayout) findViewById(R.id.tab_bar);
        alert_edit_msg = (LinearLayout) findViewById(R.id.alert_edit_msg);

        //tab_actvity_schedule = (LinearLayout) findViewById(R.id.tab_activity_schedule);

        more_layout = (LinearLayout) findViewById(R.id.more);
        activity_layout = (LinearLayout) findViewById(R.id.activity);
        add_layout = (LinearLayout) findViewById(R.id.add_layout);
        invite_menu = (RelativeLayout) findViewById(R.id.invite_menu);
        map_tab = (LinearLayout) findViewById(R.id.map);
        sale_tab = (LinearLayout) findViewById(R.id.sales);

        add = (ImageView) findViewById(R.id.add);
       // txt_activities = (TextView) findViewById(R.id.txt_activities);
       // txt_schedules = (TextView) findViewById(R.id.txt_schedules);

        img_user_picture = (ImageView) findViewById(R.id.img_user_picture);
        drawerUserPicture = (ImageView) findViewById(R.id.user_picture_iv);

        more_layout.setOnClickListener(this);
        add.setOnClickListener(this);
//        txt_schedules.setOnClickListener(this);
     //   txt_activities.setOnClickListener(this);
        activity_layout.setOnClickListener(this);
//        img_user_picture.setOnClickListener(this);
        invite_menu.setOnClickListener(this);
        map_tab.setOnClickListener(this);
        sale_tab.setOnClickListener(this);
        drawerFullName=(TextView)findViewById(R.id.full_name_tv);
        drawerFullName.setText(user.getFirst_name()+" "+user.getLast_name());
        drawerEmail=(TextView)findViewById(R.id.email_tv);
        drawerEmail.setText(user.getEmail());

        Log.d(TAG, "Avatar Url : " + Constants.GET_AVATAR_ENDPOINT + "avatar_" + user.getId() + ".jpg");
        //String avatarURL =Constants.GET_AVATAR_ENDPOINT + "avatar_" + user.getId() + ".jpg";
       // MyUtils.Picasso.displayImage(user.getAvatarURL(), img_user_picture);
        MyUtils.Picasso.displayImage(user.getAvatarURL(), drawerUserPicture);
        retrofit = MainApplication.getInstance().getRetrofit();
        service = retrofit.create(SignUpApi.class);

        bundle = getIntent().getExtras();
        if(bundle == null){
            bundle = new Bundle();
            alert_edit_msg = (LinearLayout) findViewById(R.id.alert_edit_msg);
        }

        if(bundle.getBoolean(Constants.SCHEDULE_EDITED)){
            ShowSuccessMessage("Schedule successfully updated",Constants.SCHEDULE_EDITED);
        }

        if(bundle.getBoolean(Constants.CLIENT_EDITED)){
            ShowSuccessMessage("Client successfully updated",Constants.CLIENT_EDITED);
        }


        Gson gson = new Gson();
        String user_json = gson.toJson(user);

        bundle.putString(Constants.USER_DATA, user_json);
        if ((new Date().getTime() - MyUtils.getLastTimeAppRan()) > 24*60*60*1000) {
            //launchFragment(Constants.HomeSchedule, bundle);
            MyUtils.startActivity(this, DailyStatsActivity.class);
            MyUtils.setLastTimeAppRan(new Date().getTime());
           // return;
        }
       /* else if(bundle.getBoolean(Constants.NO_CLIENT_FOUND)){
            launchFragment(Constants.ClientAdd, bundle);
        }
        else if(bundle.getBoolean(Constants.NO_SCHEDULE_FOUND)){
            launchFragment(Constants.CreateSchedule, bundle);
        }
        else {*/
            launchFragment(Constants.AppointmentList, bundle);
        //}
        activity_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
    }

    Fragment currentFragment=null;
    public void launchFragment(int pos, Bundle bundle) {
        fragment_index = pos;
        fragment = null;

       // tab_bar.setVisibility(View.VISIBLE);
        more_layout.setBackground(null);
        activity_layout.setBackground(null);
        sale_tab.setBackground(null);
        add_layout.setBackground(null);
        map_tab.setBackground(null);
        //relativeLayout.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_top);
        currentFragmentID=pos;
        switch (pos) {
            case Constants.CreateSchedule:
                tab_actvity_schedule.setBackgroundResource(R.drawable.base_schedule);
                if(fragments.get(pos)==null) {
                    fragments.put(pos,CreateScheduleFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.ClientAdd:
               //// relativeLayout.setVisibility(View.GONE);
                tab_actvity_schedule.setBackgroundResource(R.drawable.base_schedule);
                if(fragments.get(pos)==null) {
                    fragments.put(pos,ClientAddFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.ActivitySchedule:
                activity_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                if(fragments.get(pos)==null) {
                    fragments.put(pos,  ClientAddFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.HomeSchedule:
                activity_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
               // tab_bar.setVisibility(View.GONE);
                if(fragments.get(pos)==null) {
                    fragments.put(pos, DailyStatsFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.ScheduleSuggestions:
                if(fragments.get(pos)==null) {
                    fragments.put(pos, ScheduleSuggestionFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.AddScheduleActivity:
                add_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                MyUtils.startActivity(this, AddOptionsActivity.class);
                return;
            case Constants.Profile:
                //relativeLayout.setVisibility(View.GONE);
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                if(fragments.get(pos)==null) {
                    fragments.put(pos, ProfileFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
               // tab_bar.setVisibility(View.VISIBLE);
                break;
          /*  case Constants.EditProfile:
                //relativeLayout.setVisibility(View.GONE);
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                if(fragments.get(pos)==null) {
                    fragments.put(pos, EditProfileFragment.newInstance(bundle));
                }
                fragment = fragments.get(pos);
                break;
            case Constants.ProfileAddress:
                //relativeLayout.setVisibility(View.GONE);
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                if(fragments.get(pos)==null) {
                    fragments.put(pos,  AddressFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);

                break;*/
            case Constants.Notification:
//                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
//                if(fragments.get(pos)==null) {
//                    fragments.put(pos, NotificationProfileFragment.newInstance(bundle) );
//                }
//                fragment = fragments.get(pos);
                break;

            case Constants.HomeSale:
                sale_tab.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                //relativeLayout.setVisibility(View.GONE);
                if(fragments.get(pos)==null) {
                    fragments.put(pos, SaleFragment.newInstance(bundle) );
                    Logger.write("it is new");
                }
                fragment = fragments.get(pos);
                //tab_bar.setVisibility(View.VISIBLE);
                break;
            case Constants.SaleViewAll:
                sale_tab.setBackground(getResources().getDrawable(R.drawable.tab_selected));
               // relativeLayout.setVisibility(View.GONE);
                if(fragments.get(pos)==null) {
                    fragments.put(pos, SaleViewAllFragment.newInstance(bundle) );
                    Logger.write("it is new");
                }
                fragment = fragments.get(pos);
                //tab_bar.setVisibility(View.VISIBLE);
                break;

            case Constants.Industry:
                //tab_bar.setVisibility(View.GONE);
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                if(fragments.get(pos)==null) {
                    fragments.put(pos, IndustryFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.ActivityFragment:
                activity_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                if(fragments.get(pos)==null) {
                    fragments.put(pos, ScheduleAppointmentsFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                break;
            case Constants.AppointmentList:
                //tab_bar.setVisibility(View.VISIBLE);
                if(fragments.get(pos)==null) {
                    fragments.put(pos, ScheduleAppointmentsFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
                //tab_bar.setVisibility(View.VISIBLE);
                break;

            case Constants.LineAndTerritory:
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                //relativeLayout.setVisibility(View.GONE);
                if(fragments.get(pos)==null) {
                    fragments.put(pos, LinesFragment.newInstance(bundle) );
                }
                fragment = fragments.get(pos);
               // tab_bar.setVisibility(View.VISIBLE);
                break;
            case Constants.MapFragment:
                map_tab.setBackground(getResources().getDrawable(R.drawable.tab_selected));
               // relativeLayout.setVisibility(View.GONE);
                if(fragments.get(pos)==null) {
                    fragments.put(pos, MapFragment.newInstance(bundle));
                }
                fragment = fragments.get(pos);
                //tab_bar.setVisibility(View.VISIBLE);
                break;
            case Constants.ProfileFragment:
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                if(fragments.get(pos)==null) {
                    fragments.put(pos, ProfileFragment.newInstance(bundle));
                }
                fragment = fragments.get(pos);
                break;

            default:
                finish();
        }

        if (fragment != null) {
           // Log.d(TAG, getSupportFragmentManager().getBackStackEntryCount() + "");
            //Logger.write("TAGGGG: "+ fragment.getClass().getName());
            addFragment(ft, currentFragment, fragment, fragment.getClass().getName());
        }
        currentFragment=fragment;
    }

    static long backPressed=0;
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
            return;
        }
        if(System.currentTimeMillis()-backPressed < 5000) {
            finish();
        }else{
            MyUtils.showToast("Press back again to exit");
        }
        //this.moveTaskToBack(true);
        backPressed=System.currentTimeMillis();
        return;
    }

    private void handleCrop(Uri outputUri) {
        imageReadyForUploadListener = (AvatarProfileFragment) profileFragment;
        ImageView avatar = ((AvatarProfileFragment) profileFragment).avatar;
        bundle.putString(Constants.AVATAR_STATE_SAVED, outputUri.toString());
        avatar.setImageBitmap(null);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), outputUri);
            avatar.setImageBitmap(bitmap);
            imageReadyForUploadListener.imageReadyUri(outputUri);
        } catch (IOException e) {
            MyUtils.showToast("An error occurred. Please try again.");
        }
    }

    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {
        Log.d(TAG, " OnFragmentInteractionListener " + action);
        launchFragment(action, bundle);
    }

    /**
     * Photo Selection result
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            handleCrop(outputUri);
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            outputUri = Uri.fromFile(new File(getFilesDir(), "cropped.jpg"));
            Uri selectedImage = imageUri;
            Crop.of(selectedImage, outputUri).asSquare().start(this);
        } else if (requestCode == IMAGE_PICKER_SELECT && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            outputUri = Uri.fromFile(new File(getFilesDir(), "cropped.jpg"));
            Crop.of(selectedImage, outputUri).asSquare().start(this);
        }
    }

    @Override
    public void onClick(View v) {
        Logger.write("ID "+v.getId());
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
        switch (v.getId()) {
            case R.id.add:
                //launchFragment(Constants.AddScheduleActivity, bundle);
//                MyUtils.startActivity(this, AddOptionsActivity.class);
                DialogNewClient alert = new DialogNewClient();
                alert.showDialog(this, bundle);
                break;
            case R.id.more:
                toggelDrawer();
                break;
            case R.id.activity:
                if(currentFragmentID==Constants.ActivityFragment) return;
              //  tab_actvity_schedule.setBackgroundResource(R.drawable.base_activities);
                launchFragment(Constants.ActivityFragment, bundle);
                break;
            case Constants.Profile:
                fragment = new ProfileFragment1();
                break;
            case Constants.EditProfile:
//                fragment = new EditProfileFragment();
                break;
            case Constants.ProfileAddress:
//                fragment = new AddressActivity();
                break;
            //case R.id.lines_menu:
               // MyUtils.startActivity(MainActivity.this, LinesAndTerritories.class,bundle);

            case R.id.lines_menu:
                MyUtils.startActivity(MainActivity.this, LinesAndTerritories.class,bundle);
                //if(currentFragmentID==Constants.LineAndTerritory) return;
               // launchFragment(Constants.LineAndTerritory, bundle);

                break;
            case R.id.coworker_menu:
                MyUtils.startActivity(MainActivity.this, CoWorkerActivity.class,bundle);
                break;
           /* case R.id.client_menu:
                bundle.putBoolean(Constants.CLIENT_LIST, true);
                MyUtils.startActivity(MainActivity.this, ClientMapAndListActivity.class,bundle);
                break;*/
            case R.id.specials_menu:
                bundle.putBoolean(Constants.CLIENT_LIST, true);
                MyUtils.startActivity(MainActivity.this, ActivityGroupedSales.class,bundle);
                break;

            case R.id.img_user_picture : case R.id.user_picture_iv:
                if(currentFragmentID==Constants.ProfileFragment) return;
                launchFragment(Constants.ProfileFragment, bundle);
                break;
            case R.id.territories_menu:
                MyUtils.startActivity(MainActivity.this, ActivityTerritories.class,bundle);
                break;

            case R.id.invite_menu:
               // MyUtils.startActivity(MainActivity.this, SendInviteActivity.class, bundle);
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Try Tied for Android!");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I'm using Tied for Android and I recommend it to manage your business. Click here: http://www.gettied.com");

                Intent chooserIntent = Intent.createChooser(shareIntent, "Share with");
                chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chooserIntent);
                break;
            case R.id.logout:
                User.LogOut(this);
                finish();
                break;
            case R.id.map:
               // MyUtils.startActivity(this, ClientMapAndListActivity.class, bundle);
                bundle.putBoolean(Constants.CLIENT_LIST, false);
                bundle.putInt(Constants.SHOW_FILTER, 0);

                if(currentFragmentID==Constants.MapFragment) return;
                launchFragment(Constants.MapFragment, bundle);
                break;
            case R.id.goal_menu:
                MyUtils.startActivity(this, LineGoalActivity.class);
                break;
            case R.id.sales:
                if(currentFragmentID==Constants.HomeSale) return;
                launchFragment(Constants.HomeSale, bundle);
                break;
        }
    }
    private void toggelDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }
    private void clearTabs() {
        activity_layout.setBackground(null);
        map_tab.setBackground(null);
    }

    public void ShowSuccessMessage(String message, final String forWhat){
        info_msg = (TextView) findViewById(R.id.info_msg);
        info_msg.setText(message);
        alert_edit_msg = (LinearLayout) findViewById(R.id.alert_edit_msg);
        final int _splashTime = 3000;
        alert_edit_msg.setVisibility(View.VISIBLE);
        final boolean _active = true;
        final int time = 3000;
        final Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && waited < time) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (final InterruptedException e) {
                    // do nothing
                } finally {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            alert_edit_msg.setVisibility(View.GONE);
                            bundle.putBoolean(forWhat, false);
                        }
                    });
                }
            }
        };
        splashTread.start();
        alert_edit_msg.setVisibility(View.VISIBLE);
    }

    public void addFragment(FragmentTransaction transaction, Fragment cCurrentFragment, Fragment targetFragment, String tag) {

        //transaction.setCustomAnimations(0,0,0,0);
        if(cCurrentFragment!=null) {
            Logger.write("removing current "+cCurrentFragment.getClass().getName());
            transaction.hide(cCurrentFragment);
        }
        // use a fragment tag, so that later on we can find the currently displayed fragment
      if(targetFragment.isAdded()) {
            Logger.write("was added");
            transaction.show(targetFragment).commit();
        }else {
            if(cCurrentFragment==null) {
                transaction.replace(R.id.fragment_place, targetFragment, tag)
                        //.addToBackStack(tag)
                        .commit();
            }else {
                transaction.add(R.id.fragment_place, targetFragment, tag)
                        .addToBackStack(tag)
                        .commit();
            }
        }
        //currentFragment=targetFragment;
    }

}
