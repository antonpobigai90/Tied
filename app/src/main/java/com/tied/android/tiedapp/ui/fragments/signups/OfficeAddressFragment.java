package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
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
import com.tied.android.tiedapp.customs.MyAsyncTask;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.auth.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;
import com.tied.android.tiedapp.util.DialogUtils;

import org.json.JSONObject;

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

    // Reference to our image view we will use
    public ImageView img_user_picture;

    private LinearLayout alert_valid;

    //    private CheckBox same;
    LinearLayout checkbox_layout;
    ImageView img_check;
    boolean same = false;

    private EditText street, city, state, zip;
    private String cityText, stateText, streetText, zipText;
    private Location location;
    private Bundle bundle;

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

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);

        img_check = (ImageView) view.findViewById(R.id.img_check);
        img_check.setBackgroundResource(R.mipmap.dot_unchecked_icon);

        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);

        checkbox_layout = (LinearLayout) view.findViewById(R.id.checkbox_layout);
        checkbox_layout.setOnClickListener(this);

        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            User user = gson.fromJson(user_json, User.class);
            ((SignUpActivity) getActivity()).loadAvatar(user, img_user_picture);
        }

        alert_valid = (LinearLayout) view.findViewById(R.id.alert_valid);
        alert_valid.setVisibility(View.GONE);
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
            case R.id.checkbox_layout:
                same = !same;
                if (same) {
                    img_check.setBackgroundResource(R.mipmap.dot_checked_icon);
                } else {
                    img_check.setBackgroundResource(R.mipmap.dot_unchecked_icon);
                }
                break;
        }
    }

    class GeocodeAsyncTask extends MyAsyncTask {

        String errorMessage = "";
        JSONObject jObject;
        JSONObject places = null;
        String lat;

        @Override
        protected void onPreExecute() {
            DialogUtils.displayProgress(getActivity());
        }

        @Override
        protected Address doInBackground(Void... params) {
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
            if (getActivity() == null) return;
            if (address != null) {
                Coordinate coordinate = new Coordinate(address.getLatitude(), address.getLongitude());
                location.setCoordinate(coordinate);
            }

            Log.d(TAG," Location : "+  location.toString());

            Bundle bundle = getArguments();

            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            final User user = gson.fromJson(user_json, User.class);
            user.setOffice_address(location);
            if (same) {
                user.setHome_address(location);
            }

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
                            if (same) {
                                nextAction(Constants.Territory, bundle);
                            } else {
                                nextAction(Constants.HomeAddress, bundle);
                            }
                            Log.d(TAG, "location: " + json);
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
    }
}
