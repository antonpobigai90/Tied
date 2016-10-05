package com.tied.android.tiedapp.ui.fragments.profile;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ProfileApi;
import com.tied.android.tiedapp.ui.dialogs.DialogSelectPicture;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.ImageReadyForUploadListener;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.File;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class AvatarProfileFragment extends Fragment implements View.OnClickListener, ImageReadyForUploadListener{
    public static final String TAG = ProfileFragment1.class
            .getSimpleName();

    public ImageView avatar;
    public static Uri imageUri = null, outputUri = null;
    private ImageReadyForUploadListener imageReadyForUploadListener;
    public Bitmap bitmap;

    // Code for our image picker select action.
    public final int IMAGE_PICKER_SELECT = 999;

    // Activity result key for camera
    public final int REQUEST_TAKE_PHOTO = 11111;
    public Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile1, container, false);

        avatar = (ImageView) view.findViewById(R.id.avatar);
        avatar.setOnClickListener(this);

        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            User user = gson.fromJson(user_json, User.class);
            Log.d(TAG, "user.getAvatar()"+user.getAvatar());
            MyUtils.Picasso.displayImage(user.getAvatar(),avatar);
//            if (user.getAvatar_uri() != null && new File(user.getAvatar_uri()).exists()) {
//                Uri myUri = Uri.parse(user.getAvatar_uri());
//                avatar.setImageURI(myUri);
//            }else{
//
//            }
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(outputUri != null){
            avatar.setImageURI(outputUri);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar:
//                startActivity(new Intent(getActivity(), SelectPicture.class));
                DialogSelectPicture alert = new DialogSelectPicture();
                alert.showDialog(getActivity(),bundle);
                break;
        }
    }

    @Override
    public void imageReadyUri(final Uri uri) {

        DialogUtils.displayProgress(getActivity());
        Gson gson = new Gson();
        String user_json = bundle.getString(Constants.USER_DATA);
        User user = gson.fromJson(user_json, User.class);

        File file = new File(uri.getPath());

        Log.d("Uri", uri.getPath());
        Log.d("File path", file.getPath());
        Log.d("file Name", file.getName());

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

        RequestBody id =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), user.getId());

        ProfileApi profileApi = MainApplication.getInstance().getRetrofit().create(ProfileApi.class);
        // finally, execute the request
        Call<ServerRes> call = profileApi.uploadAvatar(user.getToken() ,id, body);
        call.enqueue(new Callback<ServerRes>() {

            @Override
            public void onResponse(Call<ServerRes> call, Response<ServerRes> updateAvatarResponse) {
                if (getActivity() == null) return;
                ServerRes ServerRes = updateAvatarResponse.body();
                Log.d(TAG, ServerRes.toString() );
                if(ServerRes.isSuccess()){
                    Gson gson = new Gson();
                    Bundle bundle = getArguments();
                    String user_json = bundle.getString(Constants.USER_DATA, "");
                    User user = gson.fromJson(user_json, User.class);
                    user.setAvatar_uri(null);
                    user.setAvatar(ServerRes.getUser().getAvatar()+new Date().getTime());
                    boolean saved = user.save(getActivity().getApplicationContext());
                    if(saved){
                        DialogUtils.closeProgress();
                        user_json = gson.toJson(user);
                        bundle.putString(Constants.USER_DATA, user_json);
                        ProfileFragment.vpProfile.getAdapter().notifyDataSetChanged();
                    }else {
                        DialogUtils.closeProgress();
                        Toast.makeText(getActivity(), "user information  was not updated", Toast.LENGTH_LONG).show();
                    }
                }else{
                    DialogUtils.closeProgress();
                    Toast.makeText(getActivity(), ServerRes.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ServerRes> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
                DialogUtils.closeProgress();
            }
        });
    }

}
