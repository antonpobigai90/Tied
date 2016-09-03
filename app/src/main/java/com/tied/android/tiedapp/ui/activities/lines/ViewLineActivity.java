package com.tied.android.tiedapp.ui.activities.lines;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Femi on 7/30/2016.
 */
public class ViewLineActivity extends AppCompatActivity implements  View.OnClickListener{
    private Bundle bundle;
    private User user;
    LinearLayout back_layout;
    RelativeLayout clients_layout;

    private TextView name, description;
    private Line line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_view);

        bundle = getIntent().getExtras();
        line = (Line) bundle.getSerializable(Constants.LINE_DATA);
        user = MyUtils.getUserFromBundle(bundle);
        initComponent(line);
    }

    private void initComponent(Line line) {
        name = (TextView) findViewById(R.id.name);
        name.setText(line.getName());

        description = (TextView) findViewById(R.id.description);
        description.setText(line.getDescription());
        clients_layout = (RelativeLayout) findViewById(R.id.clients_layout);
        clients_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.clients_layout:
                MyUtils.startActivity(this, LineClientList.class);
                break;
            case R.id.ship_layout:
                MyUtils.showAddressDialog(this, "Shipping information", null, new MyUtils.MyDialogClickListener() {
                    @Override
                    public void onClick(Object response) {
                        line.setDescription("Updated it here");
                        updateLine(line);
                    }
                });
                break;

        }
    }

    private void updateLine(final Line line) {

        LineApi lineApi = MainApplication.createService(LineApi.class, user.getToken());
        DialogUtils.displayProgress(this);
        Call<ResponseBody> response = lineApi.updateLine(line.getId(), line);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                try {
                    GeneralResponse response =new GeneralResponse(resResponse.body());
                    if (response.isAuthFailed()) {
                        User.LogOut(ViewLineActivity.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        Line the_line = response.getData(Constants.LINE_DATA, Line.class);
                        if(the_line.getId().equals(line.getId())){
                            Logger.write("Update line id +"+the_line.getId());
                            name.setText(line.getName());
                            description.setText(line.getDescription());
                        }
                    }else{
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                }catch (IOException ioe) {
                    Logger.write(ioe);
                }
                catch (Exception jo) {
                    Logger.write(jo);
                }
                DialogUtils.closeProgress();
            }
            @Override
            public void onFailure(Call<ResponseBody> ClientResponseCall, Throwable t) {
                Logger.write("Request failed: "+t.getCause());
                DialogUtils.closeProgress();
            }
        });
    }
}
