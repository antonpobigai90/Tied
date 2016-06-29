package com.tied.android.tiedapp.ui.fragments.client;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.interfaces.retrofits.ClientApi;
import com.tied.android.tiedapp.objects.Client;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.client.ClientActivity;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.listeners.FragmentInterationListener;
import com.tied.android.tiedapp.util.DialogUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class AddClientFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = AddClientFragment.class
            .getSimpleName();


    public ImageView avatar;
    private EditText company,name, street, phone, territory, fax, revenue, ytd_revenue,note, birthday;
    private Button button_import;
    private TextView industry, select_line;

    private String companyText, nameText, streetText;

    // Code for our image picker select action.
    public final int IMAGE_PICKER_SELECT = 999;

    // Activity result key for camera
    public final int REQUEST_TAKE_PHOTO = 11111;

    private User user;
    private Bundle bundle;
    private Uri uri;

    FragmentInterationListener mListener;

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

    public void initComponent(View view) {
        company = (EditText) view.findViewById(R.id.company);
        name = (EditText) view.findViewById(R.id.name);
//        street = (EditText) view.findViewById(R.id.street);
//        phone = (EditText) view.findViewById(R.id.phone);
//        territory = (EditText) view.findViewById(R.id.territory);
//        fax = (EditText) view.findViewById(R.id.fax);
//        revenue = (EditText) view.findViewById(R.id.revenue);
//        ytd_revenue = (EditText) view.findViewById(R.id.ytd_revenue);
//        note = (EditText) view.findViewById(R.id.note);
//        birthday = (EditText) view.findViewById(R.id.birthday);

        avatar = (ImageView) view.findViewById(R.id.avatar);
        button_import = (Button) view.findViewById(R.id.button_import);
        button_import.setOnClickListener(this);
        avatar.setOnClickListener(this);

        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER);
            user = gson.fromJson(user_json, User.class);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInterationListener) {
            mListener = (FragmentInterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(Constants.Name, bundle);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_import:
                createClient();
                break;
            case R.id.avatar:
                showChooser();
                break;
        }
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

    private void createClient(){
        Client client = new Client();
        companyText = company.getText().toString();
        nameText = name.getText().toString();

        uri = ((ClientActivity) getActivity()).outputUri;
        File file = new File(uri.getPath());

        Log.d("Uri", uri.getPath());
        Log.d("File path", file.getPath());
        Log.d("file Name", file.getName());

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("logo", file.getName(), requestFile);

        RequestBody address =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), new Gson().toJson(user.getOffice_address()));

        RequestBody full_name =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), nameText);

        RequestBody company_name =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), companyText);

        ClientApi clientApi =  MainApplication.getInstance().getRetrofit().create(ClientApi.class);
        Call<ServerRes> response = clientApi.createClient(user.getToken(), company_name, full_name, address, body );
        response.enqueue(new Callback<ServerRes>() {
            @Override
            public void onResponse(Call<ServerRes> call, Response<ServerRes> ServerResponseResponse) {
                if (getActivity() == null) return;
//                ServerRes ServerRes = ServerResponseResponse.body();
//                Log.d(TAG +" onResponse", ServerResponseResponse.body().toString());
//                if(ServerRes.isSuccess()){
//                    Bundle bundle = new Bundle();
//                    boolean saved = user.save(getActivity().getApplicationContext());
//                    if(saved){
//                        Gson gson = new Gson();
//                        String json = gson.toJson(user);
//                        bundle.putString(Constants.USER, json);
//                        DialogUtils.closeProgress();
//                        nextAction(bundle);
//                    }else{
//                        DialogUtils.closeProgress();
//                        Toast.makeText(getActivity(), "user info  was not updated", Toast.LENGTH_LONG).show();
//                    }
//                }else{
//                    Toast.makeText(getActivity(), ServerRes.getMessage(), Toast.LENGTH_LONG).show();
//                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ServerRes> ServerResponseCall, Throwable t) {
                Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                Log.d(TAG +" onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }
}
