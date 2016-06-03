package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.auth.UpdateUser;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.listeners.PhoneTextListener;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;

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
    private Button continue_btn;

    private ProgressBar progressBar;

    String phoneText, faxText;

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

    public void initComponent(View view){

        phone = (EditText) view.findViewById(R.id.phone);
        fax = (EditText) view.findViewById(R.id.fax);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        continue_btn = (Button) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);

        phone.addTextChangedListener(new PhoneTextListener());

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
        faxText = fax.getText().toString();
        if(validated()){
            progressBar.setVisibility(View.VISIBLE);

            final Bundle bundle = getArguments();

            final Gson gson = new Gson();
            String user_json = bundle.getString("user");
            final User user = gson.fromJson(user_json, User.class);
            user.setPhone(phoneText);
            user.setFax(faxText);
            user.setSign_up_stage(Constants.EnterCode);

            SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
            Call<UpdateUser> response = signUpApi.updateUser(user);
            response.enqueue(new Callback<UpdateUser>() {
                @Override
                public void onResponse(Call<UpdateUser> call, Response<UpdateUser> UpdateUserResponse) {
                    if(getActivity() == null) return;
                    UpdateUser UpdateUser = UpdateUserResponse.body();
                    Log.d(TAG +" onFailure", UpdateUserResponse.body().toString());
                    if(UpdateUser.isSuccess()){
                        Gson gson = new Gson();
                        boolean saved = user.save(getActivity().getApplicationContext());
                        if(saved){
                            String user_json = bundle.getString("user");
                            User user = gson.fromJson(user_json, User.class);
                            call_send_phone_vc(user);
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getActivity(), UpdateUser.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<UpdateUser> UpdateUserCall, Throwable t) {
                    Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                    Log.d(TAG +" onFailure", t.toString());
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        }else{
            Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
        }
    }

    private void call_send_phone_vc(User user) {

        SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
        Call<UpdateUser> response = signUpApi.sendPhoneCode(user.getId(), user.getPhone());
        response.enqueue(new Callback<UpdateUser>() {
            @Override
            public void onResponse(Call<UpdateUser> call, Response<UpdateUser> UpdateUserResponse) {
                if(getActivity() == null) return;
                Log.d(TAG +" onFailure", UpdateUserResponse.body().toString());

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<UpdateUser> UpdateUserCall, Throwable t) {
                Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                Log.d(TAG +" onFailure", t.toString());
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

//        //Todo api request for phone verification code to be sent
//        String json = gson.toJson(user);
//        bundle.putString(Constants.USER, json);
//        Gson gson = new Gson();
//        String code = "123456";
//        bundle.putString(Constants.CODE,code);
//        Toast.makeText(getActivity(), "your code is 123456", Toast.LENGTH_LONG).show();
//        progressBar.setVisibility(View.INVISIBLE);
//        nextAction(bundle);
    }

    public boolean validated(){
        
        return !phone.equals("") && !fax.equals("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                continue_action();
                break;
        }
    }
}
