package com.tied.android.tiedapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.profile.AddressFragment;
import com.tied.android.tiedapp.ui.fragments.profile.AvatarProfileFragment;
import com.tied.android.tiedapp.ui.fragments.profile.EditProfileFragment;
import com.tied.android.tiedapp.ui.fragments.profile.ProfileFragment;
import com.tied.android.tiedapp.ui.listeners.ProfileFragmentListener;

import java.io.File;
import java.io.IOException;

import retrofit2.Retrofit;

public class ProfileActivity extends FragmentActivity implements ProfileFragmentListener {

    public static final String TAG = ProfileActivity.class
            .getSimpleName();

    User user;
    public Bundle bundle, avatar_bundle;

    // Code for our image picker select action.
    public final int IMAGE_PICKER_SELECT = 999;

    // Activity result key for camera
    public final int REQUEST_TAKE_PHOTO = 11111;

    private Fragment fragment = null;
    public Fragment profileFragment = null;
    private int fragment_index = 0;

    private LinearLayout tab_bar,relativeLayout,selectedlayout, more;

    public Bitmap bitmap;

    public Uri imageUri = null, outputUri = null;

    public Retrofit retrofit;
    public SignUpApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout = (LinearLayout) findViewById(R.id.linearLayout);

        selectedlayout = (LinearLayout) findViewById(R.id.more);
        selectedlayout.setBackground(getResources().getDrawable(R.drawable.tab_selected));

        user = User.getUser(getApplicationContext());
        bundle = new Bundle();
        Gson gson = new Gson();
        String user_json = gson.toJson(user);
        bundle.putString(Constants.USER, user_json);
        launchFragment(Constants.Profile, bundle);

        retrofit = MainApplication.getInstance().getRetrofit();
        service = retrofit.create(SignUpApi.class);
    }

    @Override
    public void onBackPressed() {
        if(fragment_index == Constants.Profile){
            finish();
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

    public void OnFragmentInteractionListener(int action, Bundle bundle) {
        Log.d(TAG, " onFragmentInteraction "+action);
        launchFragment(action, bundle);

    }

    public void launchFragment(int pos, Bundle bundle) {
        fragment_index = pos;
        fragment = null;

        Log.d(TAG, "position "+pos);

        switch (pos) {
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
            default:
                finish();
        }

        fragment.setArguments(bundle);

        if (fragment != null) {
            Log.d(TAG, getSupportFragmentManager().getBackStackEntryCount() + "");
            while (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStackImmediate();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_place, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        }
    }
}
