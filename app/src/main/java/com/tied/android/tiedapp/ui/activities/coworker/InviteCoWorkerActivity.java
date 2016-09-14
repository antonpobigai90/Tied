package com.tied.android.tiedapp.ui.activities.coworker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.CoWorker;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.CoworkerApi;
import com.tied.android.tiedapp.retrofits.services.UserApi;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.util.Utility;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InviteCoWorkerActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = InviteCoWorkerActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    private EditText email;
    private String emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_coworker);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);

        email = (EditText) findViewById(R.id.email);

    }

    public void continue_action(){

        DialogUtils.displayProgress(this);
        Call<ResponseBody> response =  MainApplication.createService(UserApi.class, user.getToken()).findByEmail(emailText);
        Log.d(TAG, response.request().url().toString());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseBodyResponse) {
                if (this == null) return;
                try {
                    GeneralResponse generalResponse = new GeneralResponse(responseBodyResponse.body());
                    User found = generalResponse.getData(Constants.USER, User.class);
                    _Meta meta = generalResponse.getMeta();
                    if(meta.getStatus_code() == 200){
                        Logger.write("Found user : "+found);
                        call_add_CoWorker(user.getId(), found.getId());
                    }else{
                        DialogUtils.closeProgress();
                        MyUtils.showToast(meta.getUser_message());
                    }
                }catch (Exception e) {
                    Logger.write(e);
                    DialogUtils.closeProgress();
                    MyUtils.showToast(InviteCoWorkerActivity.this.getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> checkEmailCall, Throwable t) {
                // Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                MyUtils.showToast(InviteCoWorkerActivity.this.getString(R.string.connection_error));
                Logger.write(TAG +" onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }
    public void call_add_CoWorker(String user_id, String coworker_id){
        CoWorker coWorker = new CoWorker();
        coWorker.setUser_id(user_id);
        coWorker.setCoworker_id(coworker_id);
        coWorker.setVerified(false);
        Logger.write("coWorker : "+coWorker);
        Call<ResponseBody> response =  MainApplication.createService(CoworkerApi.class, user.getToken()).addCoworker(user_id, coWorker);
        Log.d(TAG, response.request().url().toString());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseBodyResponse) {
                if (this == null) return;
                try {
                    GeneralResponse generalResponse = new GeneralResponse(responseBodyResponse.body());
                    CoWorker createdCoWorker = generalResponse.getData(Constants.COWORKER, CoWorker.class);
                    Logger.write("createdCoWorker : "+createdCoWorker);
                    _Meta meta = generalResponse.getMeta();
                    if(meta.getStatus_code() == 200){
                        MyUtils.startActivity(InviteCoWorkerActivity.this,CoWorkerActivity.class, bundle);
                    }else{
                        DialogUtils.closeProgress();
                        MyUtils.showToast(meta.getUser_message());
                    }
                }catch (Exception e) {
                    Logger.write(e);
                    MyUtils.showToast("Error encountered");
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> checkEmailCall, Throwable t) {
                // Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                MyUtils.showToast(InviteCoWorkerActivity.this.getString(R.string.connection_error));
                Logger.write(TAG +" onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                super.onBackPressed();
                break;
            case R.id.add:
                MyUtils.startActivity(this, InviteCoWorkerActivity.class, bundle);
                break;
            case R.id.invite:
                emailText = email.getText().toString();
                if (!Utility.isEmailValid(emailText)) {
                    MyUtils.showAlert(this, Utility.getResourceString(this, R.string.alert_valide_email));
                } else {
                    continue_action();
                }
                break;
        }
    }
}
