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

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.MyAddressAsyncTask;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;
import com.tied.android.tiedapp.util.DialogUtils;
import com.tied.android.tiedapp.util.MyUtils;

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
public class HomeAddressFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = HomeAddressFragment.class
            .getSimpleName();

    private SignUpFragmentListener mListener;

    private LinearLayout alert_valid;

    Bundle bundle;

    //    private Button continue_btn;
    private RelativeLayout continue_btn;

    // Reference to our image view we will use
    public ImageView img_user_picture;

    private EditText street, city, state, zip;
    private String cityText, stateText, streetText, zipText;
    private Location location;


    int fetchType = Constants.USE_ADDRESS_NAME;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment=new HomeAddressFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public HomeAddressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_home_address, container, false);
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

        continue_btn = (RelativeLayout) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);

        img_user_picture = (ImageView) view.findViewById(R.id.img_user_picture);

        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
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
        }
    }

    class GeocodeAsyncTask extends MyAddressAsyncTask {

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

            Bundle bundle = getArguments();

            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            final User user = gson.fromJson(user_json, User.class);
            user.setHome_address(location);

            SignUpApi signUpApi = ((SignUpActivity) getActivity()).service;
            Call<ServerRes> response = signUpApi.updateUser(user);
            response.enqueue(new Callback<ServerRes>() {
                @Override
                public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResponseResponse) {
                    if (getActivity() == null) return;
                    ServerRes ServerRes = ServerResponseResponse.body();
                    Log.d(TAG +" onFailure", ServerResponseResponse.body().toString());
                    if(ServerRes.isSuccess()){
                        Bundle bundle = new Bundle();
                        boolean saved = user.save(getActivity().getApplicationContext());
                        if (saved) {
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            bundle.putString(Constants.USER_DATA, json);
                            DialogUtils.closeProgress();
                            nextAction(Constants.Territory,bundle);
                            Log.d(TAG, "location: " + json);
                        } else {
                            DialogUtils.closeProgress();
                            MyUtils.showToast("user info  was not updated");
                        }
                    }else{
                        DialogUtils.closeProgress();
                        MyUtils.showToast(ServerRes.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ServerRes> ServerResponseCall, Throwable t) {
                    MyUtils.showToast("On failure : error encountered");
                    Log.d(TAG +" onFailure", t.toString());
                    DialogUtils.closeProgress();
                }
            });
        }
    }
}
