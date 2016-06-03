package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.auth.UpdateUser;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class OfficeAddressFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = OfficeAddressFragment.class
            .getSimpleName();

    private SignUpFragmentListener mListener;

//    private Button continue_btn;
    private RelativeLayout continue_btn;
    private ProgressBar progressBar;

    private CheckBox same;

    private EditText street, city, state, zip;
    private String cityText, stateText, streetText, zipText;
    private Location location;


    int fetchType = Constants.USE_ADDRESS_NAME;

    public OfficeAddressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_office_address, container, false);
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

    public void nextAction(int action, Bundle bundle) {
        if (mListener != null) {
            mListener.onFragmentInteraction(action, bundle);
        }
    }

    public void initComponent(View view) {

        street = (EditText) view.findViewById(R.id.street);
        city = (EditText) view.findViewById(R.id.city);
        state = (EditText) view.findViewById(R.id.state);
        zip = (EditText) view.findViewById(R.id.zip);

        same = (CheckBox) view.findViewById(R.id.same);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

//        continue_btn = (Button) view.findViewById(R.id.continue_btn);
        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);
    }

    public void continue_action() {
        if (validated()) {
            new GeocodeAsyncTask().execute();
        }
    }

    private boolean validated() {
        streetText = street.getText().toString();
        cityText = city.getText().toString();
        zipText = zip.getText().toString();
        stateText = state.getText().toString();
        location = new Location(cityText, zipText, stateText, streetText);
        return !streetText.equals("");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_btn:
                continue_action();
                break;
        }
    }

    class GeocodeAsyncTask extends AsyncTask<Void, Void, Address> {

        String errorMessage = "";

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Address doInBackground(Void... none) {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = null;

            if (fetchType == Constants.USE_ADDRESS_NAME) {
                try {
                    Log.d(TAG, location.getLocationAddress());
                    addresses = geocoder.getFromLocationName(location.getLocationAddress(), 1);
                } catch (IOException e) {
                    errorMessage = "Service not available";
                    Log.e(TAG, errorMessage, e);
                }
            } else {
                errorMessage = "Unknown Type";
                Log.e(TAG, errorMessage);
            }

            if (addresses != null && addresses.size() > 0)
                return addresses.get(0);

            return null;
        }

        protected void onPostExecute(Address address) {
            if (address != null) {
                location.setLatitude(address.getLatitude());
                location.setLongitude(address.getLongitude());
            }

            Bundle bundle = getArguments();

            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            final User user = gson.fromJson(user_json, User.class);
            user.setOffice_address(location);
            if(same.isChecked()){
                user.setHome_address(location);
            }

            SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
            Call<UpdateUser> response = signUpApi.updateUser(user);
            response.enqueue(new Callback<UpdateUser>() {
                @Override
                public void onResponse(Call<UpdateUser> call, Response<UpdateUser> UpdateUserResponse) {
                    UpdateUser UpdateUser = UpdateUserResponse.body();
                    Log.d(TAG +" onFailure", UpdateUserResponse.body().toString());
                    if(UpdateUser.isSuccess()){
                        Bundle bundle = new Bundle();
                        boolean saved = user.save(getActivity().getApplicationContext());
                        if (saved) {
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            bundle.putString(Constants.USER, json);
                            progressBar.setVisibility(View.INVISIBLE);
                            if(same.isChecked()){
                                nextAction(Constants.Territory,bundle);
                            }else{
                                nextAction(Constants.HomeAddress, bundle);
                            }
                            Log.d(TAG, "location: " + json);
                        } else {
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

        }
    }
}
