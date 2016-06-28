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
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.HelpActivity;
import com.tied.android.tiedapp.ui.activities.ProfileActivity;
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

import java.io.File;
import java.io.IOException;

import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity implements SignUpFragmentListener {

    public static final String TAG = SignUpActivity.class
            .getSimpleName();

    private Fragment fragment = null;
    private int fragment_index = 0;

    // Code for our image picker select action.
    public final int IMAGE_PICKER_SELECT = 999;

    // Activity result key for camera
    public final int REQUEST_TAKE_PHOTO = 11111;

    public final int REQUEST_FACEBOOK_LOGIN = 64206;

    public final int REQUEST_TWITTER_LOGIN = 140;

    public Bitmap bitmap;

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
//            user.setSign_up_stage(4);
//            user.save(getApplicationContext());
            Log.d(TAG +" : 3", user.toString());
            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            String user_json = gson.toJson(user);
            bundle.putString(Constants.USER, user_json);
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

    public void launchFragment(int pos, Bundle bundle) {
        fragment_index = pos;
        fragment = null;

        this.bundle = bundle;

        switch (pos) {
            case Constants.EmailSignUp:
                fragment = new EmailSignUpFragment();
                break;
            case Constants.Password:
                fragment = new PasswordFragment();
                break;
            case Constants.Picture:
                fragment = new PictureFragment();
                break;
            case Constants.Name:
                fragment = new NameFragment();
                fragment.setArguments(bundle);
                break;
            case Constants.PhoneAndFax:
                fragment = new PhoneFaxFragment();
                fragment.setArguments(bundle);
                break;
            case Constants.EnterCode:
                fragment = new VerifyCodeFragment();
                fragment.setArguments(bundle);
                break;
            case Constants.OfficeAddress:
                fragment = new OfficeAddressFragment();
                break;
            case Constants.HomeAddress:
                fragment = new HomeAddressFragment();
                break;
            case Constants.Territory:
                fragment = new TerritoryFragment();
                break;
            case Constants.SalesRep:
                fragment = new SalesRepFragment();
                fragment.setArguments(bundle);
                break;
            case Constants.GroupDesc:
                fragment = new GroupDescFragment();
                break;
            case Constants.Industry:
                fragment = new IndustryFragment();
                break;
            case Constants.AddBoss:
                fragment = new AddBossFragment();
                break;
            case Constants.AddBossNow:
                fragment = new AddBossNowFragment();
                break;
            case Constants.CoWorkerCount:
                fragment = new CoWorkerCountFragment();
                break;
            case Constants.AddOptions:
                fragment = new AddInviteFragment();
                break;
            case Constants.CoWorker:
                fragment = new CoWorkerFragment();
                break;
            default: finish();
        }
        fragment.setArguments(bundle);

        if (fragment != null) {
            Log.d(TAG, getSupportFragmentManager().getBackStackEntryCount() + "");
            while (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStackImmediate();
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit)
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        Log. d(TAG, "fragment_index " + fragment_index);
        if (fragment_index == Constants.EmailSignUp || fragment_index == Constants.SignInUser) {
            Log. d(TAG, "am in fragment_index " + fragment_index);
            Intent intent = new Intent(this, WalkThroughActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if(fragment_index == Constants.Password){
            if(bundle != null){
                Bundle bundle = fragment.getArguments();
            }
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
        Intent invite_intent = new Intent(this, ProfileActivity.class);
        startActivity(invite_intent);
    }
}
