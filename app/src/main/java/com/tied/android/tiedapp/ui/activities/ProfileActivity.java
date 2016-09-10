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
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.soundcloud.android.crop.Crop;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.fragments.PrivacyFragment;
import com.tied.android.tiedapp.ui.fragments.profile.AddressFragment;
import com.tied.android.tiedapp.ui.fragments.profile.AvatarProfileFragment;
import com.tied.android.tiedapp.ui.fragments.profile.ChangePasswordFragment;
import com.tied.android.tiedapp.ui.fragments.profile.EditProfileFragment;
import com.tied.android.tiedapp.ui.fragments.profile.NotificationProfileFragment;
import com.tied.android.tiedapp.ui.fragments.profile.ProfileFragment;
import com.tied.android.tiedapp.ui.fragments.profile.SalesPrivacyFragment;
import com.tied.android.tiedapp.ui.fragments.signups.IndustryFragmentNew;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.ui.listeners.ImageReadyForUploadListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * Created by Daniel on 5/3/2016.
 */
public class ProfileActivity extends FragmentActivity implements FragmentIterationListener, View.OnClickListener {

    public static final String TAG = ProfileActivity.class
            .getSimpleName();

    public ImageView img_user_picture, add, drawerUserPicture;
    private ImageReadyForUploadListener imageReadyForUploadListener;

    private Fragment fragment = null;
    private int fragment_index = 0;
    public Fragment profileFragment = null;
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
    DrawerLayout drawerLayout;
    int currentFragmentID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bundle = getIntent().getExtras();
        launchFragment(Constants.Profile, bundle);
        retrofit = MainApplication.getInstance().getRetrofit();
        service = retrofit.create(SignUpApi.class);
    }

    Fragment currentFragment = null;

    public void launchFragment(int pos, Bundle bundle) {
        fragment = null;
        fragment_index = pos;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_top);
        currentFragmentID = pos;
        switch (pos) {
            case Constants.Profile:
                if (fragments.get(pos) == null) {
                    fragments.put(pos, ProfileFragment.newInstance(bundle));
                }
                fragment = fragments.get(pos);
                break;
            case Constants.EditProfile:
                if (fragments.get(pos) == null) {
                    fragments.put(pos, EditProfileFragment.newInstance(bundle));
                }
                fragment = fragments.get(pos);
                break;
            case Constants.ProfileAddress:
                if (fragments.get(pos) == null) {
                    fragments.put(pos, AddressFragment.newInstance(bundle));
                }
                fragment = fragments.get(pos);
                break;
            case Constants.Notification:
                if (fragments.get(pos) == null) {
                    fragments.put(pos, NotificationProfileFragment.newInstance(bundle));
                }
                fragment = fragments.get(pos);
                break;
            case Constants.Industry:
                if (fragments.get(pos) == null) {
                    // fragments.put(pos, IndustryFragment.newInstance(bundle) );
                    fragments.put(pos, new IndustryFragmentNew());
                }
                fragment = fragments.get(pos);
                break;
            case Constants.ChangePassword:
                if (fragments.get(pos) == null) {
                    fragments.put(pos, new ChangePasswordFragment());
                }
                fragment = fragments.get(pos);
                break;
            case Constants.PRIVACY:
                if (fragments.get(pos) == null) {
                    fragments.put(pos, new PrivacyFragment());
                }
                fragment = fragments.get(pos);
                break;
            case Constants.PRIVACY_SALES:
                if (fragments.get(pos) == null) {
                    fragments.put(pos, new SalesPrivacyFragment());
                }
                fragment = fragments.get(pos);
                break;
            default:
                finish();
        }

        if (fragment != null) {
            Log.d(TAG, getSupportFragmentManager().getBackStackEntryCount() + "");
            Logger.write("TAGGGG: " + fragment.getClass().getName());
            addFragment(ft, currentFragment, fragment, fragment.getClass().getName());
        }
        currentFragment = fragment;
    }

    static long backPressed = 0;

    @Override
    public void onBackPressed() {
        switch (fragment_index) {
            case Constants.Profile:
                finish();
            case Constants.EditProfile:
                launchFragment(Constants.Profile, bundle);
                break;
            case Constants.ProfileAddress:
                launchFragment(Constants.EditProfile, bundle);
                break;
            case Constants.Industry:
                launchFragment(Constants.EditProfile, bundle);
                break;
            default:
                finish();
        }
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
        Logger.write("ID " + v.getId());
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
        switch (v.getId()) {
            case Constants.Profile:
                fragment = new ProfileFragment();
                break;
            case Constants.EditProfile:
                fragment = new EditProfileFragment();
                break;
            case Constants.ProfileAddress:
                fragment = new AddressFragment();
                break;
            case Constants.Industry:
                fragment = new IndustryFragmentNew();
                break;
        }
    }

    public void profileButtonClicked(View v) {
        MyUtils.startActivity(this, MainActivity.class, bundle);
    }

    public void addFragment(FragmentTransaction transaction, Fragment currentFragment, Fragment targetFragment, String tag) {

        //transaction.setCustomAnimations(0,0,0,0);
        if (currentFragment != null) transaction.hide(currentFragment);
        // use a fragment tag, so that later on we can find the currently displayed fragment
        if (targetFragment.isAdded()) {
            transaction.show(targetFragment).commit();
        } else {
            transaction.add(R.id.fragment_place, targetFragment, tag)
                    .addToBackStack(tag)
                    .commit();
        }
    }
}
