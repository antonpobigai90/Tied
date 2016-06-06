package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.auth.ServerInfo;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;
import com.tied.android.tiedapp.util.DialogUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class GroupDescFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = GroupDescFragment.class
            .getSimpleName();


    ImageView img_help, img_principal, img_subrep;
    LinearLayout principal_layout, subrep_layout;
    TextView txt_principal, txt_subrep;
    int type_index;

    private Bundle bundle;

    //    private Button continue_btn;
    private RelativeLayout continue_btn;

    // Reference to our image view we will use
    public ImageView img_user_picture;

    private SignUpFragmentListener mListener;

    public GroupDescFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_group_desc, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignUpFragmentListener) {
            mListener = (SignUpFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            mListener.onFragmentInteraction(Constants.Industry, bundle);
        }
    }

    public void initComponent(View view) {

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);
        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);


        img_help = (ImageView) view.findViewById(R.id.img_help);
        img_help.setOnClickListener(this);

        txt_principal = (TextView) view.findViewById(R.id.txt_principal);
        txt_subrep = (TextView) view.findViewById(R.id.txt_subrep);
        img_principal = (ImageView) view.findViewById(R.id.img_principal);
        img_subrep = (ImageView) view.findViewById(R.id.img_subrep);


        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            User user = gson.fromJson(user_json, User.class);
            Uri myUri = Uri.parse(user.getAvatar_uri());
            img_user_picture.setImageURI(myUri);
        }

        principal_layout = (LinearLayout) view.findViewById(R.id.principal_layout);
        principal_layout.setOnClickListener(this);

        subrep_layout = (LinearLayout) view.findViewById(R.id.subrep_layout);
        subrep_layout.setOnClickListener(this);

        type_index = 0;
        setSelectType(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                continue_action();
                break;
            case R.id.principal_layout:
                setSelectType(0);
                break;
            case R.id.subrep_layout:
                setSelectType(1);
                break;
        }
    }


    public void continue_action(){
        DialogUtils.displayProgress(getActivity());

        Bundle bundle = getArguments();

        Gson gson = new Gson();
        String user_json = bundle.getString("user");
        final User user = gson.fromJson(user_json, User.class);
        user.setGroup_description(getResources().getString(R.string.str_principal));
        if(type_index == 1){
            user.setGroup_description(getResources().getString(R.string.str_subrep));
        }
        user.setSign_up_stage(Constants.Industry);

        SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
        Call<ServerInfo> response = signUpApi.updateUser(user);
        response.enqueue(new Callback<ServerInfo>() {
            @Override
            public void onResponse(Call<ServerInfo> call, Response<ServerInfo> UpdateUserResponse) {
                if (getActivity() == null) return;
                ServerInfo UpdateUser = UpdateUserResponse.body();
                Log.d(TAG +" onResponse", UpdateUserResponse.body().toString());
                if(UpdateUser.isSuccess()){
                    Bundle bundle = new Bundle();
                    boolean saved = user.save(getActivity().getApplicationContext());
                    if(saved){
                        Gson gson = new Gson();
                        String json = gson.toJson(user);
                        bundle.putString(Constants.USER, json);
                        DialogUtils.closeProgress();
                        nextAction(bundle);
                    }else{
                        DialogUtils.closeProgress();
                        Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(), UpdateUser.getMessage(), Toast.LENGTH_LONG).show();
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ServerInfo> UpdateUserCall, Throwable t) {
                Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                Log.d(TAG +" onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    private void setSelectType(int index) {
        if(index == 0) {
            type_index = 0;
            img_principal.setBackgroundResource(R.mipmap.dot_checked_icon);
            img_subrep.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            txt_principal.setTextColor(Color.WHITE);
            txt_subrep.setTextColor(getResources().getColor(R.color.text_disable_color));
        } else {
            type_index = 1;
            img_principal.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            img_subrep.setBackgroundResource(R.mipmap.dot_checked_icon);
            txt_principal.setTextColor(getResources().getColor(R.color.text_disable_color));
            txt_subrep.setTextColor(Color.WHITE);
        }
    }
}
