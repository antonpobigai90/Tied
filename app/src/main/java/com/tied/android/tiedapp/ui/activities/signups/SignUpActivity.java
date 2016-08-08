package com.tied.android.tiedapp.ui.activities.signups;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.activities.HelpActivity;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.fragments.signups.AddBossFragment;
import com.tied.android.tiedapp.ui.fragments.signups.AddBossNowFragment;
import com.tied.android.tiedapp.ui.fragments.signups.AddInviteFragment;
import com.tied.android.tiedapp.ui.fragments.signups.CoWorkerCountFragment;
import com.tied.android.tiedapp.ui.fragments.signups.CoWorkerFragment;
import com.tied.android.tiedapp.ui.fragments.signups.EmailSignUpFragment;
import com.tied.android.tiedapp.ui.fragments.signups.GroupDescFragment;
import com.tied.android.tiedapp.ui.fragments.signups.HomeAddressFragment;
import com.tied.android.tiedapp.ui.fragments.signups.IndustryFragment;
import com.tied.android.tiedapp.ui.fragments.signups.NameFragment;
import com.tied.android.tiedapp.ui.fragments.signups.OfficeAddressFragment;
import com.tied.android.tiedapp.ui.fragments.signups.PasswordFragment;
import com.tied.android.tiedapp.ui.fragments.signups.PhoneFaxFragment;
import com.tied.android.tiedapp.ui.fragments.signups.PictureFragment;
import com.tied.android.tiedapp.ui.fragments.signups.SalesRepFragment;
import com.tied.android.tiedapp.ui.fragments.signups.TerritoryFragment;
import com.tied.android.tiedapp.ui.fragments.signups.VerifyCodeFragment;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity implements SignUpFragmentListener {

    public static final String TAG = SignUpActivity.class
            .getSimpleName();

    private Fragment fragment = null;
    private int fragment_index = 0;
    int currentFragmentID=0;

    // Code for our image picker select action.
    public final int IMAGE_PICKER_SELECT = 999;

    // Activity result key for camera
    public final int REQUEST_TAKE_PHOTO = 11111;

    public final int REQUEST_FACEBOOK_LOGIN = 64206;

    public final int REQUEST_TWITTER_LOGIN = 140;

    public Bitmap bitmap;
    Map<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();

    public Retrofit retrofit;
    public SignUpApi service;

    private Bundle bundle;

    public Uri imageUri = null, outputUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        User user = User.getUser(getApplicationContext());
        if(user != null && user.getId() != null){
            Log.d(TAG, user.toString());
            user.setSign_up_stage(Constants.Picture);
            user.save(getApplicationContext());
            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            String user_json = gson.toJson(user);
            bundle.putString(Constants.USER_DATA, user_json);
            launchFragment(user.getSign_up_stage(), bundle);
        }else{
            launchFragment(Constants.EmailSignUp, null);
        }

        retrofit = MainApplication.getInstance().getRetrofit();
        service = retrofit.create(SignUpApi.class);
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

    Fragment currentFragment=null;
    public void launchFragment(int pos, Bundle bundle) {
        fragment_index = pos;
        fragment = null;

        this.bundle = bundle;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_top);
        currentFragmentID=pos;

        if(fragments.get(pos)==null) {
            switch (pos) {
                case Constants.EmailSignUp:
                    fragments.put(pos, EmailSignUpFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                case Constants.Password:
                    fragments.put(pos, PasswordFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                case Constants.Picture:
                    fragments.put(pos, PictureFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                case Constants.Name:
                    fragments.put(pos, NameFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                case Constants.PhoneAndFax:
                    fragments.put(pos, PhoneFaxFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                case Constants.EnterCode:
                    fragments.put(pos, VerifyCodeFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                case Constants.OfficeAddress:
                    fragments.put(pos, OfficeAddressFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                case Constants.HomeAddress:
                    fragments.put(pos, HomeAddressFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                case Constants.Territory:
                    fragments.put(pos, TerritoryFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                case Constants.SalesRep:
                    fragments.put(pos, SalesRepFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                case Constants.GroupDesc:
                    fragments.put(pos, GroupDescFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                case Constants.Industry:
                    fragments.put(pos, IndustryFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                case Constants.AddBoss:
                    fragments.put(pos, AddBossFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                case Constants.AddBossNow:
                    fragments.put(pos, AddBossNowFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                case Constants.CoWorker:
                    fragments.put(pos, CoWorkerFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                case Constants.CoWorkerCount:
                    fragments.put(pos, CoWorkerCountFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                case Constants.AddOptions:
                    fragments.put(pos, AddInviteFragment.newInstance(bundle) );
                    fragment = fragments.get(pos);
                    break;
                default: finish();
            }
        }

        if (fragment != null) {
            Log.d(TAG, getSupportFragmentManager().getBackStackEntryCount() + "");
            Logger.write("TAGGGG: "+ fragment.getClass().getName());
            addFragment(ft, currentFragment, fragment, fragment.getClass().getName());
        }
        currentFragment=fragment;
    }

    @Override
    public void onBackPressed() {
        Log. d(TAG, "fragment_index " + fragment_index);
        if (fragment_index == Constants.EmailSignUp || fragment_index == Constants.SignInUser) {
            MyUtils.startActivity(this, WalkThroughActivity.class);
        }else if(fragment_index == Constants.Password){
            launchFragment(Constants.EmailSignUp, bundle);
        }
        else {
            finish();
        }
    }

    @Override
    public void onFragmentInteraction(int action, Bundle bundle) {
        switch (action){
            case Constants.USE_ADDRESS_NAME:
                break;
            default:
                launchFragment(action, bundle);
        }
    }

    private void handleCrop(Uri outputUri) {
        ImageView avatar =  ((PictureFragment) fragment).avatar;
        ImageView img_user_picture = ((PictureFragment) fragment).img_user_picture;
        avatar.setImageBitmap(null);
        img_user_picture.setImageBitmap(null);
        Log.d("path * ", outputUri.getPath());
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), outputUri);
            avatar.setImageBitmap(bitmap);
            img_user_picture.setImageBitmap(bitmap);
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
        if(requestCode == REQUEST_FACEBOOK_LOGIN && resultCode == Activity.RESULT_OK){
            ((EmailSignUpFragment) fragment).callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        else if(requestCode == REQUEST_TWITTER_LOGIN){
            ((EmailSignUpFragment) fragment).authClient.onActivityResult(requestCode, resultCode, data);
        }
        else if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
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

    public class IncomingSms extends BroadcastReceiver {

        // Get the object of SmsManager
        final SmsManager sms = SmsManager.getDefault();

        public void onReceive(Context context, Intent intent) {

            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();

                        Log.d("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);

                        // Show Alert
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context,"senderNum: "+ senderNum + ", message: " + message, duration);
                        toast.show();

                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" +e);

            }
        }
    }

    public void addFragment(FragmentTransaction transaction, Fragment currentFragment, Fragment targetFragment, String tag) {

        //transaction.setCustomAnimations(0,0,0,0);
        if(currentFragment!=null) transaction.hide(currentFragment);
        // use a fragment tag, so that later on we can find the currently displayed fragment
        if(targetFragment.isAdded()) {
            transaction.show(targetFragment).commit();
        }else {
            transaction.add(R.id.fragment_place, targetFragment, tag)
                    .addToBackStack(tag)
                    .commit();
        }
    }

    public void onKeyPadClick(View v) {
        if(((VerifyCodeFragment) fragment) != null){
            ((VerifyCodeFragment) fragment).onKeyPadClick(v);
        }
    }

    public void helpButtonClicked(View v){
        Intent invite_intent = new Intent(this, HelpActivity.class);
        startActivity(invite_intent);
    }

    public void profileButtonClicked(View v){
        MyUtils.startActivity(this, MainActivity.class, bundle);
    }
}
