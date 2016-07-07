package com.tied.android.tiedapp.ui.fragments.signups;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;
import com.tied.android.tiedapp.util.DialogUtils;
import com.tied.android.tiedapp.util.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class PhoneFaxFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = PhoneFaxFragment.class
            .getSimpleName();

    private EditText phone,fax;
    private RelativeLayout continue_btn;
    LinearLayout alert_valid;

    String phoneText, faxText;

    // Reference to our image view we will use
    public ImageView img_user_picture;
    private Bundle bundle;

    private SignUpFragmentListener mListener;

    public PhoneFaxFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_phone_fax, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initComponent(View view){

        phone = (EditText) view.findViewById(R.id.phone);
        fax = (EditText) view.findViewById(R.id.fax);

        alert_valid = (LinearLayout) view.findViewById(R.id.alert_valid);
        alert_valid.setVisibility(View.GONE);


        continue_btn = (RelativeLayout)view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);

        bundle = getArguments();
        if(bundle != null){
            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            User user = gson.fromJson(user_json, User.class);
            phone.setText(user.getPhone());
            fax.setText(user.getFax());
            ((SignUpActivity) getActivity()).loadAvatar(user, img_user_picture);
        }

        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));
//        fax.addTextChangedListener(new FaxNumberTextWatcher(fax));

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
            mListener.onFragmentInteraction(Constants.EnterCode, bundle);
        }
    }
    
    public void continue_action(){
        phoneText = phone.getText().toString();
//        phoneText = phoneText.replaceAll("[)(-]","");
//        phoneText = "+"+phoneText.replace(" ","");
        faxText = fax.getText().toString();

        DialogUtils.displayProgress(getActivity());
        bundle = getArguments();
        final Gson gson = new Gson();
        String user_json = bundle.getString("user");
        final User user = gson.fromJson(user_json, User.class);
        user.setPhone(phoneText);
        user.setFax(faxText);
        user.setSign_up_stage(Constants.EnterCode);

        SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
        Call<ServerRes> response = signUpApi.updateUser(user);
        response.enqueue(new Callback<ServerRes>() {
            @Override
            public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResResponse) {
                if(getActivity() == null) return;
                ServerRes ServerRes = ServerResResponse.body();
                if(ServerRes.isSuccess()){
                    Gson gson = new Gson();
                    boolean saved = user.save(getActivity().getApplicationContext());
                    if(saved){
                        String user_json = bundle.getString("user");
                        User user = gson.fromJson(user_json, User.class);
                        Log.d(TAG +" number", phoneText);
                        call_send_phone_vc(user);
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

    private void call_send_phone_vc(User user) {

        SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
        Call<ServerRes> response = signUpApi.sendPhoneCode(user.getId(), "+2348022020231");
        response.enqueue(new Callback<ServerRes>() {
            @Override
            public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResResponse) {
                if(getActivity() == null) return;
                ServerRes ServerRes = ServerResResponse.body();
                if(ServerRes.isSuccess()){
                    Gson gson = new Gson();
                    String json = gson.toJson(ServerRes);
                    bundle.putString(Constants.SERVER_INFO,json);
                    nextAction(bundle);
                    Log.d(TAG +" Sms enter", ServerResResponse.body().toString());
                    DialogUtils.closeProgress();
                }else{
                    Toast.makeText(getActivity(), ServerRes.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ServerRes> ServerResCall, Throwable t) {
                Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                Log.d(TAG +" onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });

//        //Todo api request for phone verification code to be sent
//        Gson gson = new Gson();
//        String json = gson.toJson(user);
//        String code = "123456";
//        Bundle bundle = new Bundle();
//        bundle.putString(Constants.USER_DATA, json);
//        bundle.putString(Constants.CODE,code);
//        Toast.makeText(getActivity(), "your code is 12345", Toast.LENGTH_LONG).show();
//        DialogUtils.closeProgress();
//        nextAction(bundle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                if (phone.getText().length() == 0) {
                    alert_valid.setVisibility(View.VISIBLE);
                    Utility.moveViewToScreenCenter( alert_valid, Utility.getResourceString(getActivity(), R.string.alert_valide_phone_number));
                } else {
                    continue_action();
                }
                break;
        }
    }
}
