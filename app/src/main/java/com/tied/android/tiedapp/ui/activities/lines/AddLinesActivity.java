package com.tied.android.tiedapp.ui.activities.lines;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.ui.MyEditText;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
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
public class AddLinesActivity extends AppCompatActivity implements  View.OnClickListener{
    private static final String TAG = AddLinesActivity.class.getSimpleName();
    private Bundle bundle;
    private User user;
    TextView btn_add;
    LinearLayout back_layout;

    MyEditText descriptonET, nameET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_line_add);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);

        MyUtils.initLines(this, user, null);

        initComponent();
    }

    private void initComponent() {

        nameET =(MyEditText) findViewById(R.id.name);
        descriptonET =(MyEditText) findViewById(R.id.description);

        btn_add = (TextView) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                saveForm();
                break;
            case R.id.back_layout:
                MyUtils.startActivity(this, MainActivity.class);
                break;
        }
    }

    private void saveForm() {

        String lineName = nameET.getText().toString().trim();
        Logger.write(lineName);
        if(lineName.isEmpty()) {
            MyUtils.showToast("You must provide a name for this line");
            return;
        }
        String description = descriptonET.getText().toString().trim();

        final Line line=new Line();
        line.setName(lineName);
        line.setDescription(description);

        saveLine(line);
    }

    private void saveLine(final Line line) {

        LineApi lineApi = MainApplication.createService(LineApi.class, user.getToken());
        DialogUtils.displayProgress(this);
        Call<ResponseBody> response = lineApi.createLine(line);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                try {
                    GeneralResponse response =new GeneralResponse(resResponse.body());
                    if (response.isAuthFailed()) {
                        User.LogOut(AddLinesActivity.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==201) {
                        DialogUtils.closeProgress();
                        Line the_line = response.getData(Constants.LINE_DATA, Line.class);
                        Logger.write("the_line: "+the_line.toString());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.LINE_DATA, the_line);
                        MainApplication.linesList.clear();
                        MyUtils.startActivity(AddLinesActivity.this, ViewLineActivity.class, bundle);
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
