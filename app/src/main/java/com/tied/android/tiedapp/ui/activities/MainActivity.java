package com.tied.android.tiedapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.fragments.activities.EmployeesFragment;
import com.tied.android.tiedapp.ui.fragments.profile.AddressFragment;
import com.tied.android.tiedapp.ui.fragments.profile.AvatarProfileFragment;
import com.tied.android.tiedapp.ui.fragments.profile.EditProfileFragment;
import com.tied.android.tiedapp.ui.fragments.profile.NotificationProfileFragment;
import com.tied.android.tiedapp.ui.fragments.profile.ProfileFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.AddActivityFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.AddScheduleActivityFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.CreateScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.HomeScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.ScheduleSuggestionFragment;
import com.tied.android.tiedapp.ui.fragments.signups.IndustryFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentInterationListener;

import java.io.File;
import java.io.IOException;

import retrofit2.Retrofit;


/**
 * Created by Daniel on 5/3/2016.
 */
public class MainActivity extends FragmentActivity implements FragmentInterationListener, View.OnClickListener{

    public static final String TAG = SignUpActivity.class
            .getSimpleName();

    public ImageView img_user_picture, add;

    private Fragment fragment = null;
    public Fragment profileFragment = null;
    private int fragment_index = 0;

    private LinearLayout tab_bar,relativeLayout,activity_layout, add_layout, more_layout;

    private TextView txt_schedules, txt_activities;

    public Bitmap bitmap;

    public Uri imageUri = null, outputUri = null;

    User user;
    public Bundle bundle;

    // Code for our image picker select action.
    public final int IMAGE_PICKER_SELECT = 999;

    // Activity result key for camera
    public final int REQUEST_TAKE_PHOTO = 11111;

    public Retrofit retrofit;
    public SignUpApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout = (LinearLayout) findViewById(R.id.linearLayout);
        tab_bar = (LinearLayout) findViewById(R.id.tab_bar);

        more_layout = (LinearLayout) findViewById(R.id.more);
        activity_layout = (LinearLayout) findViewById(R.id.activity);
        add_layout = (LinearLayout) findViewById(R.id.add_layout);

        add = (ImageView) findViewById(R.id.add);
        txt_activities = (TextView) findViewById(R.id.txt_activities);
        txt_schedules = (TextView) findViewById(R.id.txt_schedules);

        more_layout.setOnClickListener(this);
        add.setOnClickListener(this);
        txt_schedules.setOnClickListener(this);
        txt_activities.setOnClickListener(this);
        activity_layout.setOnClickListener(this);

        img_user_picture = (ImageView) findViewById(R.id.img_user_picture);
        user = User.getUser(getApplicationContext());
        if (user.getAvatar_uri() != null) {
            Uri myUri = Uri.parse(user.getAvatar_uri());
            img_user_picture.setImageURI(myUri);
        }else{
            Picasso.with(this).
                    load(Constants.GET_AVATAR_ENDPOINT+"avatar_"+user.getId()+".jpg")
                    .resize(35,35)
                    .into(img_user_picture);
        }
        bundle = new Bundle();
        Gson gson = new Gson();
        String user_json = gson.toJson(user);
        bundle.putString(Constants.USER, user_json);
        if(user.isNewUser(getApplicationContext())){
            launchFragment(Constants.HomeSchedule, bundle);
        }else{
//            launchFragment(Constants.CreateSchedule, bundle);
            launchFragment(Constants.ScheduleSuggestions, bundle);
        }

        retrofit = MainApplication.getInstance().getRetrofit();
        service = retrofit.create(SignUpApi.class);
    }

    public void launchFragment(int pos, Bundle bundle) {
        fragment_index = pos;
        fragment = null;

        if(bundle == null){
            bundle = this.bundle;
        }


        more_layout.setBackground(null);
        activity_layout.setBackground(null);
        add_layout.setBackground(null);
        relativeLayout.setVisibility(View.VISIBLE);

        switch (pos) {
            case Constants.CreateSchedule:
                activity_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                fragment = new CreateScheduleFragment();
                break;
            case Constants.ActivitySchedule:
                relativeLayout.setVisibility(View.GONE);
                fragment = new AddActivityFragment();
                break;
            case Constants.HomeSchedule:
                tab_bar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                fragment = new HomeScheduleFragment();
                break;
            case Constants.ScheduleSuggestions:
                relativeLayout.setVisibility(View.GONE);
                fragment = new ScheduleSuggestionFragment();
                break;
            case Constants.TabActivities:
                fragment = new EmployeesFragment();
                break;
            case Constants.AddScheduleActiity:
                add_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                relativeLayout.setVisibility(View.GONE);
                fragment = new AddScheduleActivityFragment();
                break;
            case Constants.Profile:
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                relativeLayout.setVisibility(View.GONE);
                fragment = new ProfileFragment();
                break;
            case Constants.EditProfile:
                relativeLayout.setVisibility(View.GONE);
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                fragment = new EditProfileFragment();
                break;
            case Constants.ProfileAddress:
                relativeLayout.setVisibility(View.GONE);
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                fragment = new AddressFragment();
                break;
            case Constants.Notification:
                relativeLayout.setVisibility(View.GONE);
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                fragment = new NotificationProfileFragment();
                break;
            case Constants.Industry:
                relativeLayout.setVisibility(View.GONE);
                tab_bar.setVisibility(View.GONE);
                more_layout.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                fragment = new IndustryFragment();
                break;
            default:
                finish();
        }

        fragment.setArguments(bundle);
        if (fragment != null) {
            Log.d(TAG, getSupportFragmentManager().getBackStackEntryCount() + "");
            while (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStackImmediate();
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit)
                    ft.replace(R.id.fragment_place, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void loadAvatar(User user, ImageView img_user_picture){
        if (user.getAvatar_uri() != null){
            Uri myUri = Uri.parse(user.getAvatar_uri());
            img_user_picture.setImageURI(myUri);
        }else if(user.getAvatar() != null && !user.getAvatar().equals("")){
            Picasso.with(this).
                    load(user.getAvatar())
                    .resize(35,35)
                    .into(img_user_picture);
        }
    }

    private void handleCrop(Uri outputUri) {
        ImageView avatar =  ((AvatarProfileFragment) profileFragment).avatar;
        bundle.putString(Constants.AVATAR_STATE_SAVED, outputUri.toString());
        avatar.setImageBitmap(null);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), outputUri);
            avatar.setImageBitmap(bitmap);
        } catch (IOException e) {
            Toast.makeText(this, " error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {
        Log.d(TAG, " onFragmentInteraction " + action);
        launchFragment(action, bundle);
    }

    /**
     * Photo Selection result
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("requestCode",requestCode+"");

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
        switch (v.getId()){
            case R.id.add:
                launchFragment(Constants.AddScheduleActiity, bundle);
                break;
            case R.id.txt_activities:
                launchFragment(Constants.TabActivities, bundle);
                break;
            case R.id.txt_schedules:
                launchFragment(Constants.CreateSchedule, bundle);
                break;
            case R.id.more:
                launchFragment(Constants.Profile, bundle);
//                Intent intent = new Intent(this, ProfileActivity.class);
//                startActivity(intent);
                break;
            case R.id.activity:
                launchFragment(Constants.CreateSchedule, bundle);
                break;
            case Constants.Profile:
                relativeLayout.setVisibility(View.GONE);
                fragment = new ProfileFragment();
                break;
            case Constants.EditProfile:
                fragment = new EditProfileFragment();
                break;
            case Constants.ProfileAddress:
                fragment = new AddressFragment();
                break;
        }
    }
}
