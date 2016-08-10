package com.tied.android.tiedapp.ui.fragments.client;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.MyAddressAsyncTask;
import com.tied.android.tiedapp.customs.model.DataModel;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.client.ClientActivity;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.fragments.ClientDatePickerFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.ui.dialogs.SelectDataDialog;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.MyUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class AddClientFragment extends Fragment implements View.OnClickListener, SelectDataDialog.SelectedListener{

    public static final String TAG = AddClientFragment.class
            .getSimpleName();

    int[] id_industry = {1, 2, 3, 4};
    String[] txt_industry = {"Food","Travel","Technology","Business"};
    Boolean[] industry_status = {false,false,false,false};
    ArrayList<DataModel> listIndustry;

    int[] id_line = {1, 2};
    String[] txt_line = {"Line1","Line2"};
    Boolean[] line_status = {false,false};
    ArrayList<DataModel> listLine;

    public ImageView avatar;
    private EditText company,name, street, city, phone, zip, territory, fax, revenue, ytd_revenue, note;
    private LinearLayout ok_but;
    private TextView industry, line, birthday;

    Spinner stateSpinner;

    private String companyText, nameText, streetText, cityText, stateText, zipText, phoneText, noteText, birthdayText;
    private Location location;

    private int visit_frequency = 1;
//    private ImageView img_weekly, img_two_weeks,img_monthly,img_three_weeks;
    private LinearLayout weekly_layout, two_weeks_layout,monthly_layout,three_weeks_layout;
    LinearLayout visit_radio, birthday_layout;
    RelativeLayout industry_layout;

    int industry_id = 1;

    // Code for our image picker select action.
    public final int IMAGE_PICKER_SELECT = 999;

    // Activity result key for camera
    public final int REQUEST_TAKE_PHOTO = 11111;

    private User user;
    private Client client;
    private Bundle bundle;
    private Uri uri;

    FragmentIterationListener mListener;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment=new AddClientFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_client, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    public void initIndustry(){
        Call<List<DataModel>> response = MainApplication.getInstance().getRetrofit().create(SignUpApi.class).getIndustries();
        response.enqueue(new Callback<List<DataModel>>() {
            @Override
            public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> listResponse) {
                if (getActivity() == null) return;
                DialogUtils.closeProgress();
                List<DataModel> dataModelList = listResponse.body();
                Log.d(TAG + " onResponse", dataModelList.toString());
            }

            @Override
            public void onFailure(Call<List<DataModel>> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    public void initComponent(View view) {
        company = (EditText) view.findViewById(R.id.company);
        name = (EditText) view.findViewById(R.id.name);
        street = (EditText) view.findViewById(R.id.street);
        zip = (EditText) view.findViewById(R.id.zip);
        city = (EditText) view.findViewById(R.id.city);

        stateSpinner = (Spinner) view.findViewById(R.id.state);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.my_spinner_item, MyUtils.States.asArrayList());
        adapter.setDropDownViewResource(R.layout.my_spinner_dropdown);
        stateSpinner.setAdapter(adapter);
        stateSpinner.setSelection(adapter.getPosition("TX"));

        phone = (EditText) view.findViewById(R.id.phone);
        fax = (EditText) view.findViewById(R.id.fax);
        revenue = (EditText) view.findViewById(R.id.revenue);
        ytd_revenue = (EditText) view.findViewById(R.id.ytd_revenue);
        note = (EditText) view.findViewById(R.id.note);
        birthday = (TextView) view.findViewById(R.id.birthday);
        birthday_layout = (LinearLayout) view.findViewById(R.id.birthday_layout);
        birthday_layout.setOnClickListener(this);

        industry_layout = (RelativeLayout) view.findViewById(R.id.industry_layout);
        industry = (TextView) view.findViewById(R.id.industry);
        industry.setOnClickListener(this);

        line = (TextView) view.findViewById(R.id.line);
        line.setOnClickListener(this);

        weekly_layout = (LinearLayout) view.findViewById(R.id.weekly_layout);
        monthly_layout = (LinearLayout) view.findViewById(R.id.monthly_layout);
        two_weeks_layout = (LinearLayout) view.findViewById(R.id.two_weeks_layout);
        three_weeks_layout = (LinearLayout) view.findViewById(R.id.three_weeks_layout);

        weekly_layout.setOnClickListener(this);
        monthly_layout.setOnClickListener(this);
        three_weeks_layout.setOnClickListener(this);
        two_weeks_layout.setOnClickListener(this);
        visit_radio = (LinearLayout) view.findViewById(R.id.visit_radio);

        selectRadio(visit_radio,0);

        avatar = (ImageView) view.findViewById(R.id.avatar);
        ok_but = (LinearLayout) view.findViewById(R.id.ok_but);
        ok_but.setOnClickListener(this);
        avatar.setOnClickListener(this);

        listIndustry = new ArrayList<DataModel>();

        listLine = new ArrayList<DataModel>();
        for(int i = 0; i < id_line.length; i++){
            DataModel list_line = new DataModel(id_line[i],txt_line[i],line_status[i]);
            listLine.add(list_line);
        }

        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            user = gson.fromJson(user_json, User.class);

            ArrayList<String> stringArrayList = user.getIndustries();
            if (stringArrayList != null && stringArrayList.size() > 0){
                for(int i = 0; i < stringArrayList.size(); i++){
                    DataModel list_industry = new DataModel(i+1,stringArrayList.get(i),false);
                    listIndustry.add(list_industry);
                }
            }

            String client_json = bundle.getString(Constants.CLIENT_DATA);
            client = gson.fromJson(client_json, Client.class);

            if (client != null){
                String avatarURL = Constants.GET_LOGO_ENDPOINT + "avatar_" + user.getId() + ".jpg";
                MyUtils.Picasso.displayImage(avatarURL, avatar);
                name.setText(client.getFull_name());
                company.setText(client.getCompany());
                street.setText(client.getAddress().getStreet());
                zip.setText(client.getAddress().getZip());
                city.setText(client.getAddress().getStreet());
                stateSpinner.setSelection(adapter.getPosition(client.getAddress().getState()));
                phone.setText(client.getPhone());
                fax.setText(client.getFax());
                revenue.setText(client.getRevenue());
                ytd_revenue.setText(client.getYtd_revenue());
                note.setText(client.getNote());
                birthday.setText(client.getBirthday());
            }
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

    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(Constants.CreateSchedule, bundle);
        }
    }

    private boolean validated() {
        companyText = company.getText().toString();
        nameText = name.getText().toString();
        streetText = street.getText().toString();
        cityText = city.getText().toString();
        zipText = zip.getText().toString();
        stateText = stateSpinner.getSelectedItem().toString().trim();
        location = new Location(cityText, zipText, stateText, streetText);
        birthdayText = birthday.getText().toString();
        noteText = note.getText().toString();
        phoneText = phone.getText().toString();
        return !streetText.equals("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok_but:
                uri = ((ClientActivity) getActivity()).outputUri;
                if(client == null && uri == null){
                    Toast.makeText(getActivity(), "Upload user image", Toast.LENGTH_LONG).show();
                }
                else if (validated()) {
                    new GeocodeAsyncTask().execute();
                }
                break;
            case R.id.weekly_layout:
                selectRadio(visit_radio,0);
                break;
            case R.id.two_weeks_layout:
                selectRadio(visit_radio,1);
                break;
            case R.id.three_weeks_layout:
                selectRadio(visit_radio,2);
                break;
            case R.id.monthly_layout:
                selectRadio(visit_radio,3);
                break;
            case R.id.birthday_layout:
                DialogFragment dateFragment = new ClientDatePickerFragment();
                dateFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;
            case R.id.industry:
                SelectDataDialog alert = new SelectDataDialog(listIndustry,industry,this);
                alert.showDialog();
                break;
            case R.id.line:
                SelectDataDialog alert_line = new SelectDataDialog(listLine,line,this);
                alert_line.showDialog();
                break;
            case R.id.avatar:
                showChooser();
                break;
        }
    }

    public void selectRadio(LinearLayout visit_radio, int position){
        int index = 0;
        for(int i = 0; i < visit_radio.getChildCount(); i++){
            if(visit_radio.getChildAt(i) instanceof LinearLayout){
                LinearLayout child = (LinearLayout) visit_radio.getChildAt(i);
                ImageView img_radio = (ImageView) child.getChildAt(0);
                TextView title = (TextView) child.getChildAt(1);
                if(position != index){
                    img_radio.setBackgroundResource(R.mipmap.circle_uncheck);
                    title.setTextColor(getResources().getColor(R.color.semi_transparent_black));
                }else{
                    img_radio.setBackgroundResource(R.mipmap.circle_check2);
                    title.setTextColor(getResources().getColor(R.color.button_bg));
                    visit_frequency = i+1;
                    Log.d(TAG, "radio_value"+visit_frequency);
                }
                index++;
            }
        }
    }

    @Override
    public void selectedNow(DataModel dataModel, TextView txt) {
        String text_industry = "- " + dataModel.getName() + " -";
        txt.setText(text_industry);
    }

    public void showChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Take a pics", "Browse Gallery"},
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                                ((SignUpActivity) getActivity()).imageUri = Uri.fromFile(photo);
                                getActivity().startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                                break;

                            case 1:
                                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                getIntent.setType("image/*");

                                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                pickIntent.setType("image/*");

                                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                                getActivity().startActivityForResult(chooserIntent, IMAGE_PICKER_SELECT);

                                break;

                            default:
                                break;
                        }
                    }
                });
        builder.show();
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

            try {
                Log.d(TAG, location.getLocationAddress());
                addresses = geocoder.getFromLocationName(location.getLocationAddress(), 1);
            } catch (IOException e) {
                errorMessage = "Service not available";
                Log.e(TAG, errorMessage, e);
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
                if (client == null){
                    createClient();
                }else{
                    editClient();
                }
            }else{
                DialogUtils.closeProgress();
                Toast.makeText(getActivity(), "sorry location cannot be found in map", Toast.LENGTH_LONG).show();
            }
        }
    }

    public Client initClient(){
        Client client = new Client();
        client.setPhone(phoneText);
        client.setFull_name(nameText);
        client.setCompany(companyText);
        client.setAddress(location);
        client.setLine_id("1");
        client.setIndustry_id(1);
        client.setVisit_id(1);
        client.setBirthday(birthdayText);
        client.setDescription(noteText);

        return client;
    }



    private void createClient() {
        Client client = initClient();

        File file = new File(uri.getPath());

        Log.d("Uri", uri.getPath());
        Log.d("File path", file.getPath());
        Log.d("file Name", file.getName());

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        final MultipartBody.Part body =
                MultipartBody.Part.createFormData("logo", file.getName(), requestFile);

        RequestBody clientReq =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), new Gson().toJson(client));

        ClientApi clientApi = MainApplication.getInstance().getRetrofit().create(ClientApi.class);
        Call<ClientRes> response = clientApi.createClient(user.getToken(), clientReq, body);
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if (getActivity() == null) return;
                Log.d(TAG + " onResponse", resResponse.body().toString());
                ClientRes clientRes = resResponse.body();
                if (clientRes.isAuthFailed()) {
                    User.LogOut(getActivity().getApplicationContext());
                } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 201) {
                    Log.d(TAG + " client good", clientRes.getClient().toString());
                    bundle.putBoolean(Constants.NO_CLIENT_FOUND, false);
                    DialogUtils.closeProgress();
                    Client.clientCreated(getActivity().getApplicationContext());
                    MyUtils.startActivity(getActivity(), MainActivity.class, bundle);
                } else {
                    DialogUtils.closeProgress();
                    Toast.makeText(getActivity(), clientRes.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ClientRes> ClientResponseCall, Throwable t) {
                Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }


    private void editClient() {

        client = initClient();

        ClientApi clientApi = MainApplication.getInstance().getRetrofit().create(ClientApi.class);
        Call<ClientRes> response = null;
        if(uri == null){
            File file = new File(uri.getPath());

            Log.d("Uri", uri.getPath());
            Log.d("File path", file.getPath());
            Log.d("file Name", file.getName());

            // create RequestBody instance from file
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            RequestBody clientReq =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), new Gson().toJson(client));

            // MultipartBody.Part is used to send also the actual file name
            final MultipartBody.Part body =
                    MultipartBody.Part.createFormData("logo", file.getName(), requestFile);

            response = clientApi.createClient(user.getToken(), clientReq, body);

        }else{
            RequestBody clientReq =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), new Gson().toJson(client));

            response = clientApi.editNoAvatarClient(user.getToken(), client.getId(), clientReq);
        }

        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if (getActivity() == null) return;
                Log.d(TAG + " onResponse", resResponse.body().toString());
                ClientRes clientRes = resResponse.body();
                if (clientRes.isAuthFailed()) {
                    User.LogOut(getActivity().getApplicationContext());
                } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 201) {
                    Log.d(TAG + " client good", clientRes.getClient().toString());
                    DialogUtils.closeProgress();
                    bundle.putBoolean(Constants.NO_CLIENT_FOUND, false);
                    Client.clientCreated(getActivity().getApplicationContext());
                    MyUtils.startActivity(getActivity(), MainActivity.class, bundle);
                } else {
                    DialogUtils.closeProgress();
                    Toast.makeText(getActivity(), clientRes.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ClientRes> ClientResponseCall, Throwable t) {
                Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }


}
