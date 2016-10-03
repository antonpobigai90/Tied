package com.tied.android.tiedapp.ui.fragments.profile;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.activities.ProfileActivity;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.dialogs.PasswordDialog;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;

import org.apache.commons.lang.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = EditProfileFragment.class
            .getSimpleName();

    private EditText first_name, last_name, email, fax, company_name;
    private String firstNameText, lastNameText, emailText, faxText, companyNameText, homeAddressText, officeAddressText;

    private ImageView add_industry;
    private Bundle bundle;
    private User user;

    private TextView office_address_text, home_address_text;

    private LinearLayout home_address, office_address;
    public FragmentIterationListener mListener;

    private Button btnSaveChange, btnBack;
    private TextView tvTitle,tvChange;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new EditProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        firstNameText = first_name.getText().toString();
        lastNameText = last_name.getText().toString();
        emailText = email.getText().toString();
        faxText = fax.getText().toString();
        companyNameText = company_name.getText().toString();

        outState.putString(Constants.FIRST_NAME, firstNameText);
        outState.putString(Constants.LAST_NAME, lastNameText);
        outState.putString(Constants.EMAIL, emailText);
        outState.putString(Constants.FAX, faxText);
        outState.putString(Constants.COMPANY_NAME, companyNameText);

    }

    public void initComponent(View view) {

        first_name = (EditText) view.findViewById(R.id.first_name);
        last_name = (EditText) view.findViewById(R.id.last_name);
        email = (EditText) view.findViewById(R.id.email);
        fax = (EditText) view.findViewById(R.id.fax);
        company_name = (EditText) view.findViewById(R.id.company_name);

        home_address = (LinearLayout) view.findViewById(R.id.home_address);
        home_address_text = (TextView) view.findViewById(R.id.home_address_text);

        office_address = (LinearLayout) view.findViewById(R.id.office_address);
        office_address_text = (TextView) view.findViewById(R.id.office_address_text);

        //add_industry = (ImageView) view.findViewById(R.id.add_industry);
//        add_industry.setOnClickListener(this);
    //    office_address.setOnClickListener(this);
      //  home_address.setOnClickListener(this);

        btnSaveChange = (Button) view.findViewById(R.id.btnSaveChange);
        btnSaveChange.setOnClickListener(this);

        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.profile_edit));

        tvChange=(TextView)view.findViewById(R.id.tvChange);
        tvChange.setOnClickListener(this);

        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            user = gson.fromJson(user_json, User.class);

            first_name.setText(user.getFirst_name());
            last_name.setText(user.getLast_name());
            email.setText(user.getEmail());
            fax.setText(user.getFax());
            company_name.setText(user.getCompany_name());

            if (user.getOffice_address() != null) {
                office_address_text.setText(user.getOffice_address().getLocationAddress());
            }

            if (user.getHome_address() != null) {
                home_address_text.setText(user.getHome_address().getLocationAddress());
            }
/*
            if (user.getIndustries() != null && user.getIndustries().size() > 0) {
                String indust = StringUtils.join(user.getIndustries().toArray(), ", ");
                industry_list.setText(indust);
            }*/
        }
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
            mListener.OnFragmentInteractionListener(action, bundle);
        }
    }

    private void confirmEdit() {
        if (validate()) {
            DialogUtils.displayProgress(getActivity());

            user.setFirst_name(firstNameText);
            user.setLast_name(lastNameText);
            user.setFax(faxText);
            user.setCo_workers(companyNameText);

            SignUpApi signUpApi = ((ProfileActivity) getActivity()).service;
            Call<ServerRes> response = signUpApi.updateUser(user);
            response.enqueue(new Callback<ServerRes>() {
                @Override
                public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResponseResponse) {
                    if (getActivity() == null) return;
                    ServerRes ServerRes = ServerResponseResponse.body();
                    Log.d(TAG + " onFailure", ServerResponseResponse.body().toString());
                    if (ServerRes.isAuthFailed()) {
                        DialogUtils.closeProgress();
                        User.LogOut(getActivity());
                    } else if (ServerRes.isSuccess()) {
                        boolean saved = user.save(getActivity().getApplicationContext());
                        if (saved) {
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            bundle.putString(Constants.USER_DATA, json);
                            DialogUtils.closeProgress();
                            Toast.makeText(getActivity(), ServerRes.getMessage(), Toast.LENGTH_LONG).show();
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
                public void onFailure(Call<ServerRes> ServerResponseCall, Throwable t) {
                    Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                    Log.d(TAG + " onFailure", t.toString());
                    DialogUtils.closeProgress();
                }
            });
        }
    }

    public boolean validate() {
        firstNameText = first_name.getText().toString();
        lastNameText = last_name.getText().toString();
        emailText = email.getText().toString();
        faxText = fax.getText().toString();
        companyNameText = company_name.getText().toString();

        return (firstNameText != null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSaveChange:
                confirmEdit();
                break;
            case R.id.btnBack:
                nextAction(Constants.Profile, bundle);
                break;
            case R.id.tvChange:
                nextAction(Constants.ProfileAddress, bundle);
                break;
/*            case R.id.change:
                PasswordDialog alert = new PasswordDialog();
                alert.showDialog(getActivity(), user);
                break;*/
            /*case R.id.add_industry:
                bundle.putBoolean(Constants.EditingProfile, true);
                nextAction(Constants.Industry, bundle);
                break;*/
        }
    }
}
