package com.tied.android.tiedapp.ui.fragments.lines;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.Toast;
import com.google.gson.Gson;
import com.tied.android.tiedapp.objects._Meta;
import okhttp3.ResponseBody;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.ui.MyEditText;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.lines.AddLinesActivity;
import com.tied.android.tiedapp.ui.fragments.MyFormFragment;
import com.tied.android.tiedapp.util.DialogUtils;
import com.tied.android.tiedapp.util.HTTPConnection;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class GeneralFragment extends MyFormFragment implements View.OnClickListener{
    private ImageView avatar;
    MyEditText descriptonET, streetET, cityET,zipET, territoryET;

    Spinner stateSpinner;
    protected View continueBtn;
    Coordinate coordinate;
    Gson gson=new Gson();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_line_general, container, false);
        initComponents();
        return view;
    }

    @Override
    public void initComponents() {
        /*
        addEditTextToMap(R.id.description);
        addEditTextToMap(R.id.street);
        addEditTextToMap(R.id.city);
        addEditTextToMap(R.id.zip);

        */

        descriptonET=(MyEditText)view.findViewById(R.id.description);
        streetET=(MyEditText)view.findViewById(R.id.street);
        cityET=(MyEditText)view.findViewById(R.id.city);
        zipET=(MyEditText)view.findViewById(R.id.zip);
        territoryET = (MyEditText)view.findViewById(R.id.territory_et);
        stateSpinner = (Spinner) view.findViewById(R.id.state);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.my_spinner_item, MyUtils.States.asArrayList());
        adapter.setDropDownViewResource(R.layout.my_spinner_dropdown);
        stateSpinner.setAdapter(adapter);
        stateSpinner.setSelection(adapter.getPosition("TX"));
       // addClientBut=view.findViewById(R.id.add_client);
        //addClientBut.setOnClickListener(this);
        continueBtn=view.findViewById(R.id.continue_btn);
        continueBtn.setOnClickListener(this);
        addViewToMap(stateSpinner);



    }
    public void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.avatar:

                break;
           /* case R.id.add_client:
                GeneralSelectObjectActivity.setType(GeneralSelectObjectActivity.SELECT_CLIENT_TYPE, true);
                MyUtils.startActivity(getActivity(), GeneralSelectObjectActivity.class);
                break; */
            case R.id.continue_btn:
                saveForm();
                break;
        }
    }
    private void saveForm() {

        String lineName = ((AddLinesActivity)getActivity()).lineNameTextView.getText().toString().trim();
        Logger.write(lineName);
        if(lineName.isEmpty()) {
            MyUtils.showToast("You must provide a name for this line");
            return;
        }
        String description = descriptonET.getText().toString().trim();
        String street = streetET.getText().toString().trim();
        String city = cityET.getText().toString().trim();
        String state = stateSpinner.getSelectedItem().toString().trim();
        String zip=zipET.getText().toString().trim();
        if(street.isEmpty()) {
            MyUtils.showToast("You must provide a street address");
            return;
        }
        if(city.isEmpty()) {
            MyUtils.showToast("You must provide a city");
            return;
        }
        if(state.isEmpty()) {
            MyUtils.showToast("You must provide a state address");
            return;
        }
        if(zip.isEmpty()) {
            MyUtils.showToast("You must provide a zip code");
            return;
        }

        final Line line=new Line();
        line.setName(lineName);
        line.setDescription(description);
        final Location location = new Location(city, zip, state,  street);
        location.setCountry("US");

        MyUtils.getLatLon(street + ", " + city + ", " + state + " " + zip + " US", new HTTPConnection.AjaxCallback() {
            @Override
            public void run(int code, String response) {
                Gson gson=new Gson();
                coordinate=(Coordinate)gson.fromJson(response, Coordinate.class);
                location.setCoordinate(coordinate);
                line.setAddress(location);
                saveLine(line);
                Logger.write(location.toString());
            }
        });

    }
    public Map getFields() {
        return myFieldsMap;

    }
    private void saveLine(Line line) {
        LineApi lineApi = MainApplication.getInstance().getRetrofit().create(LineApi.class);
        User user=MyUtils.getUserLoggedIn();

        Call<ResponseBody> response = lineApi.createLine(user.getToken(), line);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (getActivity() == null) return;

                ResponseBody resp = resResponse.body();
                try {
                    JSONObject response=new JSONObject(resp.string());
                    if (MyUtils.isAuthFailed(response)) {
                        User.LogOut(getActivity().getApplicationContext());
                        return;
                    }
                    _Meta meta;
                    if(( meta=MyUtils.getMeta(response)) !=null && meta.getStatus_code()==201) {
                        AddLinesActivity activity=(AddLinesActivity)getActivity();
                        activity.setLine(gson.fromJson(response.getString("lines"), Line.class));
                        ((AddLinesActivity)getActivity()).moveNext();
                    }else{
                        
                    }

                }catch (IOException ioe) {
                    Logger.write(ioe);
                }
                catch (JSONException jo) {
                    Logger.write(jo);
                }
                /*if (clientRes.isAuthFailed()) {
                    User.LogOut(getActivity().getApplicationContext());
                } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 201) {
                    Log.d(TAG + " client good", clientRes.getClient().toString());
                    DialogUtils.closeProgress();
                    Client.clientCreated(getActivity().getApplicationContext());
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    DialogUtils.closeProgress();
                    Toast.makeText(getActivity(), clientRes.getMessage(), Toast.LENGTH_LONG).show();
                }*/
            }

            @Override
            public void onFailure(Call<ResponseBody> ClientResponseCall, Throwable t) {
                Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
              Logger.write("Requst failed: "+t.getCause());
                DialogUtils.closeProgress();
            }
        });
    }


}
