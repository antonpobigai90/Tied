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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.auth.CheckEmail;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.activities.signups.WalkThroughActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;
import com.tied.android.tiedapp.util.AppDialog;
import com.tied.android.tiedapp.util.DialogUtils;
import com.tied.android.tiedapp.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class EmailSignUpFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = EmailSignUpFragment.class
            .getSimpleName();

    public CallbackManager callbackManager;
    private AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private SignUpFragmentListener mListener;

    private RelativeLayout continue_btn;
    LinearLayout alert_valid_email;
    private ImageView btn_close, img_facebook;

    boolean m_bExit = false;
    AppDialog alertDialog;

    private EditText email;

    String facebookId, firstName = "", lastName="", emailText="", avatar="";

    public EmailSignUpFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_email, container, false);
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
            mListener.onFragmentInteraction(Constants.Password,bundle);
        }
    }

    public void initComponent(View view){

        email = (EditText) view.findViewById(R.id.email);

        btn_close = (ImageView) view.findViewById(R.id.img_close);
        img_facebook = (ImageView) view.findViewById(R.id.img_facebook);
        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        img_facebook.setOnClickListener(this);

        Bundle bundle = getArguments();
        if(bundle != null){
            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            Log.d(TAG, user_json);
            User user = gson.fromJson(user_json, User.class);
            email.setText(user.getEmail());
        }

        initializeFaceBook();

        alert_valid_email = (LinearLayout) view.findViewById(R.id.alert_valid);
        alert_valid_email.setVisibility(View.GONE);
    }

    public void continue_action(){
        DialogUtils.displayProgress(getActivity());
        SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
        Call<CheckEmail> response = signUpApi.checkEmail(emailText);
        Log.d(TAG, response.request().url().toString());
        response.enqueue(new Callback<CheckEmail>() {
            @Override
            public void onResponse(Call<CheckEmail> call, Response<CheckEmail> checkEmailResponse) {
                if (getActivity() == null) return;
                CheckEmail checkEmail = checkEmailResponse.body();

                if(checkEmail.isSuccess()){
                    Bundle bundle = new Bundle();
                    User user = new User();
                    user.setEmail(emailText);
                    Gson gson = new Gson();
                    String user_json = gson.toJson(user);
                    bundle.putString("user", user_json);
                    nextAction(bundle);
                }else{
                    Toast.makeText(getActivity(), checkEmail.getMessage(), Toast.LENGTH_LONG).show();
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<CheckEmail> checkEmailCall, Throwable t) {
                Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                Log.d(TAG +" onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                emailText = email.getText().toString();
                if (!Utility.isEmailValid(emailText)) {
                    alert_valid_email.setVisibility(View.VISIBLE);
                    Utility.moveViewToScreenCenter( alert_valid_email, Utility.getResourceString(getActivity(), R.string.alert_valide_email));
                } else {
                    continue_action();
                }
                break;
            case R.id.img_close:
                Intent intent = new Intent(getActivity(), WalkThroughActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.img_facebook:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                break;
        }
    }

    private void initializeFaceBook() {
        FacebookSdk.sdkInitialize(getActivity());
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        accessToken = loginResult.getAccessToken();
                        GetUserProfile();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getActivity(), exception.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

        setAccessTokenTracker();
        setProfileTracker();
    }

    private void GetUserProfile() {
        DialogUtils.displayProgress(getActivity());
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {

                        DialogUtils.closeProgress();

                        Log.d("response ", response.toString()+"");

                        // TODO Auto-generated method stub
                        JSONObject obj = response.getJSONObject();
                        try {

                            facebookId = obj.getString("id");
                            avatar = "https://graph.facebook.com/" + facebookId + "/picture?type=large";
                            firstName = obj.getString("first_name");
                            lastName = obj.getString("last_name");
                            emailText = obj.getString("email");

                            continue_action();

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void setAccessTokenTracker(){
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                accessToken = currentAccessToken;
            }
        };
    }

    private void setProfileTracker(){
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                LoginManager.getInstance().logOut();
            }
        };
    }


}