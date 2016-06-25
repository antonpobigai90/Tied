package com.tied.android.tiedapp.ui.fragments.profile;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.MyListAsyncTask;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.auth.ServerInfo;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.ProfileActivity;
import com.tied.android.tiedapp.ui.listeners.ProfileFragmentListener;
import com.tied.android.tiedapp.util.DialogUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class AddressFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = AddressFragment.class
            .getSimpleName();

    public ProfileFragmentListener mListener;

    private EditText office_zip, office_street, office_city, home_zip, home_street, home_city;
    private String office_zipName, office_streetName, office_cityName, home_zipName, home_streetName, home_cityName;

    private ImageView confirm_edit, img_close;

    private Bundle bundle;
    private User user;

    private Location office_location, home_location;

    int fetchType = Constants.USE_ADDRESS_NAME;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_address, container, false);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm_edit:
                confirmEdit();
                break;
            case R.id.img_close:
                nextAction(Constants.Profile,bundle);
                break;
        }
    }

    public void initComponent(View view) {

        home_zip = (EditText) view.findViewById(R.id.home_zip);
        home_street = (EditText) view.findViewById(R.id.home_street);
        home_city = (EditText) view.findViewById(R.id.home_city);

        office_zip = (EditText) view.findViewById(R.id.office_zip);
        office_street = (EditText) view.findViewById(R.id.office_street);
        office_city = (EditText) view.findViewById(R.id.office_city);

        confirm_edit = (ImageView) view.findViewById(R.id.confirm_edit);
        img_close = (ImageView) view.findViewById(R.id.img_close);
        confirm_edit.setOnClickListener(this);
        img_close.setOnClickListener(this);

        bundle = getArguments();
        if (bundle != null) {
            Log.d(TAG, "bundle not null");
            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            user = gson.fromJson(user_json, User.class);

            Location office_location = user.getOffice_address();
            Log.d(TAG, office_location.toString());
            if(office_location != null){
                office_zip.setText(office_location.getZip());
                office_street.setText(office_location.getStreet());
                office_city.setText(office_location.getCity());
            }

            Location home_location = user.getHome_address();
            if(home_location != null){
                home_zip.setText(home_location.getZip());
                home_street.setText(home_location.getStreet());
                home_city.setText(home_location.getCity());
            }
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

    public void nextAction(int action,Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action, bundle);
        }
    }

    public boolean validate(){
        office_streetName = office_street.getText().toString();
        office_cityName = office_city.getText().toString();
        office_zipName = office_zip.getText().toString();

        home_streetName = home_street.getText().toString();
        home_cityName = home_city.getText().toString();
        home_zipName = home_zip.getText().toString();

        return (office_streetName != null);
    }

    private void confirmEdit(){
        if(validate()) {
            DialogUtils.displayProgress(getActivity());

            office_location.setStreet(office_streetName);
            office_location.setCity(office_cityName);
            office_location.setZip(office_zipName);

            home_location.setStreet(home_streetName);
            home_location.setCity(home_cityName);
            home_location.setZip(home_zipName);

            new GeocodeAsyncTask().execute();
        }
    }

    class GeocodeAsyncTask extends MyListAsyncTask {

        String errorMessage = "";
        JSONObject jObject;
        JSONObject places = null;
        String lat;

        @Override
        protected void onPreExecute() {
            DialogUtils.displayProgress(getActivity());
        }

        @Override
        protected List<Address> doInBackground(Void... params) {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = null;

            if (fetchType == Constants.USE_ADDRESS_NAME) {
                try {
                    Log.d(TAG, office_location.getLocationAddress());
                    addresses = geocoder.getFromLocationName(office_location.getLocationAddress(), 1);
                    addresses = geocoder.getFromLocationName(home_location.getLocationAddress(), 1);
                } catch (IOException e) {
                    errorMessage = "Service not available";
                    Log.e(TAG, errorMessage, e);
                }
            } else {
                errorMessage = "Unknown Type";
                Log.e(TAG, errorMessage);
            }

            if (addresses != null && addresses.size() > 0)
                return addresses;

            return null;
        }

        protected void onPostExecute(List<Address> addresses) {
            if (getActivity() == null) return;
            if (addresses.get(0) != null) {
                office_location.setLatitude(addresses.get(0).getLatitude());
                office_location.setLongitude(addresses.get(0).getLongitude());
            }

            if (addresses.get(1) != null) {
                home_location.setLatitude(addresses.get(1).getLatitude());
                home_location.setLongitude(addresses.get(1).getLongitude());
            }

            user.setOffice_address(office_location);
            user.setHome_address(home_location);

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
                            nextAction(Constants.Profile,bundle);
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
}
