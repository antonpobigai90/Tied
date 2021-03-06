package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.graphics.Color;
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

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class SalesRepFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = SalesRepFragment.class
            .getSimpleName();

    ImageView img_help, img_self, img_group;
    LinearLayout back_layout, profile_layout, self_layout, group_layout;
    TextView txt_self, txt_group;
    int type_index;

    //    private Button continue_btn;
    private RelativeLayout continue_btn;

    // Reference to our image view we will use
    public ImageView img_user_picture;

    private FragmentIterationListener mListener;

    private Bundle bundle;

    private RequestQueue queue;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment=new SalesRepFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public SalesRepFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_sales_rep, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentIterationListener) {
            mListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(Constants.GroupDesc, bundle);
        }
    }

    public void initComponent(View view) {

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);
        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);


        img_help = (ImageView) view.findViewById(R.id.img_help);
        img_help.setOnClickListener(this);

        txt_self = (TextView) view.findViewById(R.id.txt_self);
        txt_group = (TextView) view.findViewById(R.id.txt_group);

        img_self = (ImageView) view.findViewById(R.id.img_self);
        img_group = (ImageView) view.findViewById(R.id.img_group);


        bundle = getArguments();
        MyUtils.initAvatar(bundle, img_user_picture);

        self_layout = (LinearLayout) view.findViewById(R.id.self_layout);
        self_layout.setOnClickListener(this);

        group_layout = (LinearLayout) view.findViewById(R.id.group_layout);
        group_layout.setOnClickListener(this);

        type_index = 0;
        setSelectType(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                continue_action();
                break;
            case R.id.self_layout:
                setSelectType(0);
                break;
            case R.id.group_layout:
                setSelectType(1);
                break;
        }
    }

    private void setSelectType(int index) {
        if(index == 0) {
            type_index = 0;
            img_self.setBackgroundResource(R.mipmap.dot_checked_icon);
            img_group.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            txt_self.setTextColor(Color.WHITE);
            txt_group.setTextColor(getResources().getColor(R.color.text_disable_color));
        } else {
            type_index = 1;
            img_self.setBackgroundResource(R.mipmap.dot_unchecked_icon);
            img_group.setBackgroundResource(R.mipmap.dot_checked_icon);
            txt_self.setTextColor(getResources().getColor(R.color.text_disable_color));
            txt_group.setTextColor(Color.WHITE);
        }
    }

    public void continue_action(){

        DialogUtils.displayProgress(getActivity());

        Bundle bundle = getArguments();

        Gson gson = new Gson();
        String user_json = bundle.getString(Constants.USER_DATA);
        final User user = gson.fromJson(user_json, User.class);
        user.setSale_type(getResources().getString(R.string.str_self));
        if(type_index == 1){
            user.setSale_type(getResources().getString(R.string.str_group));
        }
        user.setSign_up_stage(Constants.GroupDesc);

        Call<ServerRes> response = MainApplication.createService(SignUpApi.class).updateUser(user);
        response.enqueue(new Callback<ServerRes>() {
            @Override
            public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResResponse) {
                if (getActivity() == null) return;
                ServerRes ServerRes = ServerResResponse.body();
                Log.d(TAG +" onResponse", ServerResResponse.body().toString());
                if(ServerRes.isSuccess()){
                    Bundle bundle = new Bundle();
                    boolean saved = user.save(getActivity().getApplicationContext());
                    if(saved){
                        Gson gson = new Gson();
                        String json = gson.toJson(user);
                        bundle.putString(Constants.USER_DATA, json);
                        DialogUtils.closeProgress();
                        nextAction(bundle);
                    }else{
                        DialogUtils.closeProgress();
                        Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(), ServerRes.getMessage(), Toast.LENGTH_LONG).show();
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ServerRes> ServerResCall, Throwable t) {
                Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                Log.d(TAG +" onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });

    }
}
