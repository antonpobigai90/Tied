package com.tied.android.tiedapp.ui.fragments.profile;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.auth.ServerInfo;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.ProfileActivity;
import com.tied.android.tiedapp.ui.listeners.ProfileFragmentListener;
import com.tied.android.tiedapp.util.DialogUtils;
import com.tied.android.tiedapp.util.PasswordDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = EditProfileFragment.class
            .getSimpleName();

    private EditText first_name, last_name, email, fax, company_name;
    private String firstNameText, lastNameText, emailText, faxText, companyNameText, homeAddressText, officeAddressText;
    TextView change;

    private ImageView confirm_edit, img_close;
    private Bundle bundle;
    private User user;

    public ProfileFragmentListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    public void initComponent(View view) {

        first_name = (EditText) view.findViewById(R.id.first_name);
        last_name = (EditText) view.findViewById(R.id.last_name);
        email = (EditText) view.findViewById(R.id.email);
        fax = (EditText) view.findViewById(R.id.fax);
        company_name = (EditText) view.findViewById(R.id.company_name);

        change = (TextView) view.findViewById(R.id.change);
        change.setOnClickListener(this);

        confirm_edit = (ImageView) view.findViewById(R.id.confirm_edit);
        img_close = (ImageView) view.findViewById(R.id.img_close);
        confirm_edit.setOnClickListener(this);
        img_close.setOnClickListener(this);

        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            user = gson.fromJson(user_json, User.class);

            first_name.setText(user.getFirst_name());
            last_name.setText(user.getLast_name());
            email.setText(user.getEmail());
            fax.setText(user.getFax());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileFragmentListener) {
            mListener = (ProfileFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(Constants.Profile, bundle);
        }
    }


    private void confirmEdit(){
        if(validate()){
            DialogUtils.displayProgress(getActivity());
            SignUpApi signUpApi = ((ProfileActivity) getActivity()).service;
            Call<ServerInfo> response = signUpApi.updateUser(user);
            response.enqueue(new Callback<ServerInfo>() {
                @Override
                public void onResponse(Call<ServerInfo> call, Response<ServerInfo> UpdateUserResponse) {
                    if (getActivity() == null) return;
                    ServerInfo UpdateUser = UpdateUserResponse.body();
                    Log.d(TAG + " onFailure", UpdateUserResponse.body().toString());
                    if (UpdateUser.isSuccess()) {
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
                        Toast.makeText(getActivity(), UpdateUser.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    DialogUtils.closeProgress();
                }

                @Override
                public void onFailure(Call<ServerInfo> UpdateUserCall, Throwable t) {
                    Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                    Log.d(TAG + " onFailure", t.toString());
                    DialogUtils.closeProgress();
                }
            });
        }
    }

    public boolean validate(){
        firstNameText = first_name.getText().toString();
        lastNameText = last_name.getText().toString();
        emailText = email.getText().toString();
        faxText = fax.getText().toString();
        companyNameText = company_name.getText().toString();

        return (firstNameText != null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm_edit:
                confirmEdit();
                break;
            case R.id.img_close:
                nextAction(bundle);
                break;
            case R.id.change:
                PasswordDialog alert = new PasswordDialog();
                alert.showDialog(getActivity());
                break;
        }
    }
}
