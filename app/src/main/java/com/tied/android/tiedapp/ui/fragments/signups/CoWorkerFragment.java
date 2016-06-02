package com.tied.android.tiedapp.ui.fragments.signups;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.user.Boss;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class CoWorkerFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = CoWorkerFragment.class
            .getSimpleName();

    private EditText email,phone;
    private Button continue_btn;

    private ProgressBar progressBar;

    private EditText street, city, state, zip;
    private String cityText, stateText, streetText, zipText;
    private String emailText, phoneText;

    int fetchType = Constants.USE_ADDRESS_NAME;

    private SignUpFragmentListener mListener;

    private Boss boss;
    private Location location;

    public CoWorkerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_co_worker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }


    public void initComponent(View view){

        street = (EditText) view.findViewById(R.id.street);
        city = (EditText) view.findViewById(R.id.city);
        state = (EditText) view.findViewById(R.id.state);
        zip = (EditText) view.findViewById(R.id.zip);

        email = (EditText) view.findViewById(R.id.email);
        phone = (EditText) view.findViewById(R.id.phone);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        continue_btn = (Button) view.findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);
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
            mListener.onFragmentInteraction(Constants.AddBossNow, bundle);
        }
    }


    public boolean validated(){
        streetText = street.getText().toString();
        cityText = city.getText().toString();
        zipText = zip.getText().toString();
        stateText = state.getText().toString();
        location = new Location(cityText, zipText, stateText, streetText);

        emailText = email.getText().toString();
        phoneText = phone.getText().toString();
        return !streetText.equals("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

            boss = new Boss(emailText, phoneText, location);

            Bundle bundle = getArguments();

            Uri uri = ((SignUpActivity) getActivity()).outputUri;

            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            final User user = gson.fromJson(user_json, User.class);
            user.setBoss(boss);
            user.setSign_up_stage(Constants.AddBossNow);
            String json = gson.toJson(user);
            bundle.putString(Constants.USER, json);
            if(uri != null)
                bundle.putString("avatar", uri.toString());
            nextAction(bundle);
        }
    }

    public void continue_action() {
        if (validated()) {
            new GeocodeAsyncTask().execute();
        }
    }
}
