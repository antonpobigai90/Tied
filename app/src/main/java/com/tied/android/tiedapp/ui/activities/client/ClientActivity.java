package com.tied.android.tiedapp.ui.activities.client;

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
import android.widget.Toast;

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.client.AddClientFragment;
import com.tied.android.tiedapp.ui.fragments.signups.TerritoryFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;

import java.io.File;
import java.io.IOException;

/**
 * Created by Daniel on 5/3/2016.
 */
public class ClientActivity extends FragmentActivity implements View.OnClickListener, FragmentIterationListener{

    public static final String TAG = ClientActivity.class
            .getSimpleName();


    private Fragment fragment = null;
    public Fragment profileFragment = null;
    private int fragment_index = 0;

    private User user;
    private Bundle bundle;

    // Code for our image picker select action.
    public final int IMAGE_PICKER_SELECT = 999;

    // Activity result key for camera
    public final int REQUEST_TAKE_PHOTO = 11111;

    public Bitmap bitmap;
    private boolean isLaunched = false;

    public Uri imageUri = null, outputUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        user = User.getUser(getApplicationContext());
        bundle = new Bundle();
        Gson gson = new Gson();
        String user_json = gson.toJson(user);
        bundle.putString(Constants.USER_DATA, user_json);
        launchFragment(Constants.AddClient, bundle);
    }

    private void handleCrop(Uri outputUri) {
        ImageView avatar = ((AddClientFragment) fragment).avatar;
        avatar.setImageBitmap(null);
        Log.d("path * ", outputUri.getPath());
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
        Log.d("requestCode", requestCode + "");
        if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            handleCrop(outputUri);
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            outputUri = Uri.fromFile(new File(getFilesDir(), "client.jpg"));
            Uri selectedImage = imageUri;
            Crop.of(selectedImage, outputUri).asSquare().start(this);
        } else if (requestCode == IMAGE_PICKER_SELECT && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            outputUri = Uri.fromFile(new File(getFilesDir(), "client.jpg"));
            Crop.of(selectedImage, outputUri).asSquare().start(this);
        }
    }

    public void launchFragment(int pos, Bundle bundle) {
        fragment_index = pos;
        fragment = null;
        Log.d(TAG, "position " + pos);
        switch (pos) {
            case Constants.AddClient:
                fragment = new AddClientFragment();
                break;
            case Constants.Territory:
                fragment = new TerritoryFragment();
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
            ft.replace(R.id.fragment_place, fragment);
            ft.addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        }
    }

    public void OnFragmentInteractionListener(int action, Bundle bundle) {
        Log.d(TAG, " onFragmentInteraction " + action);
        launchFragment(action, bundle);
    }

    @Override
    public void onBackPressed() {
        if (fragment_index == Constants.AddClient) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.txt_done:
                finish();
                break;
        }
    }

}
