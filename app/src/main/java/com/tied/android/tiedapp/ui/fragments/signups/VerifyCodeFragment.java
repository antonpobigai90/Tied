package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.util.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/5/2016.
 */
public class VerifyCodeFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = VerifyCodeFragment.class
            .getSimpleName();

    private FragmentIterationListener mListener;

    private LinearLayout back_layout, alert_valid;
    TextView txt_help, txt_next, txt_delete, txt_verify_code;
    StringBuffer temp = new StringBuffer();
    Context context;
    String code;

    private User user;

    Bundle bundle;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment=new VerifyCodeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_verify_code, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    private void initComponent(View view) {
        context = getActivity();

        txt_verify_code = (TextView) view.findViewById(R.id.txt_verify_code);

        txt_help = (TextView) view.findViewById(R.id.txt_help);
        txt_help.setOnClickListener(this);

        txt_next = (TextView) view.findViewById(R.id.txt_next);
        txt_next.setOnClickListener(this);

        txt_delete = (TextView) view.findViewById(R.id.txt_delete);
        txt_delete.setOnClickListener(this);

        alert_valid = (LinearLayout) view.findViewById(R.id.alert_valid);
        alert_valid.setVisibility(View.GONE);

        back_layout = (LinearLayout) view.findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);

        bundle = getArguments();
        String user_json = bundle.getString(Constants.USER_DATA);
        Gson gson = new Gson();
        user = gson.fromJson(user_json, User.class);
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

    public void nextAction(int action, Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action,bundle);
        }
    }


    public void onKeyPadClick(View v) {

        switch (v.getId()) {
            case R.id.img_key0:
                temp.append("0");
                break;
            case R.id.img_key1:
                temp.append("1");
                break;
            case R.id.img_key2:
                temp.append("2");
                break;
            case R.id.img_key3:
                temp.append("3");
                break;
            case R.id.img_key4:
                temp.append("4");
                break;
            case R.id.img_key5:
                temp.append("5");
                break;
            case R.id.img_key6:
                temp.append("6");
                break;
            case R.id.img_key7:
                temp.append("7");
                break;
            case R.id.img_key8:
                temp.append("8");
                break;
            case R.id.img_key9:
                temp.append("9");
                break;
        }

        txt_verify_code.setText(temp.toString());
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_layout:
                nextAction(Constants.PhoneAndFax, bundle);
                break;
            case R.id.txt_help:

                break;
            case R.id.txt_next:
                if (txt_verify_code.getText().length() == 0) {
                    alert_valid.setVisibility(View.VISIBLE);
                    Utility.moveViewToScreenCenter( alert_valid, Utility.getResourceString(context, R.string.alert_valide_verify_code));
                } else {
                    call_verify_code(user);
                }
                break;
            case R.id.txt_delete:
                txt_verify_code.setText("");
                temp.delete(0, temp.length());
                break;
        }
    }

    private void call_verify_code(User user) {

        DialogUtils.displayProgress(getActivity());
        Call<ServerRes> response = MainApplication.createService(SignUpApi.class)
                .verifyPhoneCode(user.getId(),code, user.getPhone());
        response.enqueue(new Callback<ServerRes>() {
            @Override
            public void onResponse(Call<ServerRes> call, Response<ServerRes> serverResResponse) {
                if(getActivity() == null) return;
                try {
                    ServerRes serverRes = serverResResponse.body();
                    Logger.write("Am here "+serverRes);
                    if (serverRes.isSuccess()) {
                        bundle = getArguments();
                        Gson gson = new Gson();
                        String user_json = bundle.getString(Constants.USER_DATA);
                        User user = gson.fromJson(user_json, User.class);
                        user.setSign_up_stage(Constants.OfficeAddress);
                        boolean saved = user.save(getActivity().getApplicationContext());
                        DialogUtils.closeProgress();
                        if(saved){
                            String json = gson.toJson(user);
                            bundle.putString(Constants.USER_DATA, json);
                            nextAction(Constants.OfficeAddress, bundle);
                        }else{
                            DialogUtils.closeProgress();
                            MyUtils.showToast("user info  was not updated");
                        }
                    } else {
                        DialogUtils.closeProgress();
                        Logger.write("Am here ");
                        MyUtils.showToast("An error occurred. Please try again");
                    }
                }catch (Exception e){
                    Logger.write(e);
                    DialogUtils.closeProgress();
                    MyUtils.showToast("An error occurred. Please try again");
                }
            }

            @Override
            public void onFailure(Call<ServerRes> serverResCall, Throwable t) {
                MyUtils.showToast("On failure : error encountered");
                Log.d(TAG +" onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }
}
