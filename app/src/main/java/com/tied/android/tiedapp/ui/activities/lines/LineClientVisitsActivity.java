package com.tied.android.tiedapp.ui.activities.lines;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Visit;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.objects.visit.VisitFilter;
import com.tied.android.tiedapp.retrofits.services.VisitApi;
import com.tied.android.tiedapp.ui.activities.visits.ActivityAddVisits;
import com.tied.android.tiedapp.ui.adapters.VisitListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Upworker on 11/3/2016.
 */

public class LineClientVisitsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LineClientVisitsActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User user;
    protected ListView listView;
    List<Visit> visits = new ArrayList<Visit>();
    private  Client client;
    protected VisitListAdapter adapter;
    TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserLoggedIn();
        client = (Client) bundle.getSerializable(Constants.CLIENT_DATA);

        initComponent();
    }

    private void initComponent() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText(client.getCompany());

        listView = (ListView) findViewById(R.id.visits_listview);

        adapter = new VisitListAdapter(visits, null, this);
        listView.setAdapter(adapter);
        loadVisits();
    }

    private void loadVisits() {
        VisitFilter visitFilter = new VisitFilter();
        visitFilter.setClient_id(client.getId());
        visitFilter.setSchedule_id(null);
        visitFilter.setMonth(11);
        visitFilter.setYear(2016);
        visitFilter.setDate("2016-11-04");
        visitFilter.setDistance(4500);
        visitFilter.setUnit("mi");

        final VisitApi visitApi =  MainApplication.createService(VisitApi.class);
        Call<ResponseBody> response = visitApi.getClientVisits(client.getId(), visitFilter);
        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) {
                    // Logger.write("null activity");
                    return;
                }
                //Logger.write("(((((((((((((((((((((((((((((999999");
                DialogUtils.closeProgress();

                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());

                    if (response.isAuthFailed()) {
                        User.LogOut(LineClientVisitsActivity.this);
                        return;
                    }

                    _Meta meta = response.getMeta();
                    if (meta != null && meta.getStatus_code() == 200) {
                        visits.clear();
                        visits.addAll( (ArrayList) response.getDataAsList(Constants.CLIENTS_lIST, Visit.class));

                        adapter.notifyDataSetChanged();

                    } else {
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                } catch (Exception e) {
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Logger.write(" onFailure", t.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.img_filter:
                break;
            case R.id.img_plus:
                MyUtils.startRequestActivity(this, ActivityAddVisits.class, Constants.Visits);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.Visits && resultCode == this.RESULT_OK) {
            loadVisits();
        }
    }
}
