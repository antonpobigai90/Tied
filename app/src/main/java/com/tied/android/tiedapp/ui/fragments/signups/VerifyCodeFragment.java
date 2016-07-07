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
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;
import com.tied.android.tiedapp.util.Utility;

/**
 * Created by Emmanuel on 6/5/2016.
 */
public class VerifyCodeFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = VerifyPhoneFragment.class
            .getSimpleName();

    private SignUpFragmentListener mListener;

    LinearLayout back_layout, alert_valid;
    TextView txt_help, txt_next, txt_delete, txt_verify_code;
    StringBuffer temp = new StringBuffer();
    Context context;
    String phone_number, code;

    Bundle bundle;

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

        back_layout = (LinearLayout) view.findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);

        txt_verify_code = (TextView) view.findViewById(R.id.txt_verify_code);

        txt_help = (TextView) view.findViewById(R.id.txt_help);
        txt_help.setOnClickListener(this);

        txt_next = (TextView) view.findViewById(R.id.txt_next);
        txt_next.setOnClickListener(this);

        txt_delete = (TextView) view.findViewById(R.id.txt_delete);
        txt_delete.setOnClickListener(this);

        alert_valid = (LinearLayout) view.findViewById(R.id.alert_valid);
        alert_valid.setVisibility(View.GONE);


        bundle = getArguments();
        String ServerRes_str = bundle.getString(Constants.SERVER_INFO);
        Gson gson = new Gson();
        ServerRes ServerRes = gson.fromJson(ServerRes_str, ServerRes.class);
        if(ServerRes!= null && ServerRes.isSuccess()){
            code = ServerRes.getCode()+"";
            Log.d(TAG +" Code", ServerRes.getCode()+"");
        }else{
            nextAction(Constants.PhoneAndFax, bundle);
        }
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

    public void nextAction(int action, Bundle bundle) {
        if (mListener != null) {
            mListener.onFragmentInteraction(action,bundle);
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
                    if (code != null && code.equals(txt_verify_code.getText().toString())) {
                        bundle = getArguments();
                        Gson gson = new Gson();
                        String user_json = bundle.getString("user");
                        User user = gson.fromJson(user_json, User.class);
                        user.setSign_up_stage(Constants.OfficeAddress);
                        boolean saved = user.save(getActivity().getApplicationContext());
                        if(saved){
                            String json = gson.toJson(user);
                            bundle.putString(Constants.USER_DATA, json);
                            nextAction(Constants.OfficeAddress, bundle);
                        }else{
                            Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
            case R.id.txt_delete:
                txt_verify_code.setText("");
                temp.delete(0, temp.length());
                break;
        }
    }
}
