package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
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
public class NameFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = NameFragment.class
            .getSimpleName();

    private EditText first_name, last_name;
    //    private Button continue_btn;
    private RelativeLayout continue_btn;
    LinearLayout alert_valid;

    // Reference to our image view we will use
    public ImageView img_user_picture;

    String firstNameText, lastNameText;

    private SignUpFragmentListener mListener;

    private Bundle bundle;
    private User user;

    public NameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_name, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    public void initComponent(View view) {

        first_name = (EditText) view.findViewById(R.id.first_name);
        last_name = (EditText) view.findViewById(R.id.last_name);

        alert_valid = (LinearLayout) view.findViewById(R.id.alert_valid);
        alert_valid.setVisibility(View.GONE);

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);

        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);

        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            bundle = getArguments();
            String user_json = bundle.getString("user");
            user = gson.fromJson(user_json, User.class);
            first_name.setText(user.getFirst_name());
            last_name.setText(user.getLast_name());
            ((SignUpActivity) getActivity()).loadAvatar(user, img_user_picture);
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


    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            mListener.onFragmentInteraction(Constants.PhoneAndFax, bundle);
        }
    }

    public void continue_action() {
        DialogUtils.displayProgress(getActivity());
        user.setFirst_name(firstNameText);
        user.setLast_name(lastNameText);
        user.setSign_up_stage(Constants.PhoneAndFax);
        SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
        Call<ServerRes> response = signUpApi.updateUser(user);
        response.enqueue(new Callback<ServerRes>() {
            @Override
            public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResResponse) {
                if (getActivity() == null) return;
                ServerRes ServerRes = ServerResResponse.body();
                Log.d(TAG + " onFailure", ServerResResponse.body().toString());
                if (ServerRes.isSuccess()) {
                    Bundle bundle = new Bundle();
                    boolean saved = user.save(getActivity().getApplicationContext());
                    if (saved) {
                        Gson gson = new Gson();
                        String json = gson.toJson(user);
                        bundle.putString(Constants.USER, json);
                        DialogUtils.closeProgress();
                        nextAction(bundle);
                    } else {
                        DialogUtils.closeProgress();
                        Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), ServerRes.getMessage(), Toast.LENGTH_LONG).show();
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ServerRes> ServerResCall, Throwable t) {
                Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_btn:
                firstNameText = first_name.getText().toString();
                lastNameText = last_name.getText().toString();
                boolean first_name_match = firstNameText.matches("^[\\p{L} .'-]+$");
                if (!first_name_match) {
                    alert_valid.setVisibility(View.VISIBLE);
                    Utility.moveViewToScreenCenter(alert_valid, Utility.getResourceString(getActivity(), R.string.alert_valide_name));
                } else if (firstNameText.length() == 0) {
                    alert_valid.setVisibility(View.VISIBLE);
                    Utility.moveViewToScreenCenter(alert_valid, Utility.getResourceString(getActivity(), R.string.alert_valide_name_empty));
                } else {
                    continue_action();
                }
                break;
        }
    }
}
