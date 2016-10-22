package com.tied.android.tiedapp.ui.activities.coworker;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.ActivityDataModel;
import com.tied.android.tiedapp.objects.CoWorker;
import com.tied.android.tiedapp.objects.RevenueFilter;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.CoworkerApi;
import com.tied.android.tiedapp.ui.activities.lines.LinesListActivity;
import com.tied.android.tiedapp.ui.activities.client.ClientMapAndListActivity;
import com.tied.android.tiedapp.ui.activities.sales.ActivityGroupedSales;
import com.tied.android.tiedapp.ui.adapters.ActivityAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class ViewCoWorkerActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = ViewCoWorkerActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User currentUser;

    LinearLayout back_layout;
    private ImageView avatar, img_segment;
    private TextView name;
    private User coworker;
    View activitiesSection ;

    Boolean bGeneral = true;
    private LinearLayout bottom_layout;

    private ListView activities_listview;
    private ActivityAdapter activity_adapter;

    private LinearLayout lines, schedules, territories, clients, goals, sales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coworker_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gradient));
        }

        bundle = getIntent().getExtras();
        currentUser = MyUtils.getUserLoggedIn();
        bundle.putInt(Constants.SOURCE, Constants.COWORKER_SOURCE);
        coworker = (User) bundle.getSerializable(Constants.USER_DATA);

        name = (TextView) findViewById(R.id.name);
        avatar = (ImageView) findViewById(R.id.avatar);
        img_segment  = (ImageView) findViewById(R.id.img_segment);

        name.setText(coworker.getFirst_name()+" "+coworker.getLast_name());
        MyUtils.Picasso.displayImage(coworker.getAvatarURL(), avatar);

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);
        activities_listview = (ListView) findViewById(R.id.activities_listview);


        activitiesSection = findViewById(R.id.activities_section);
        activitiesSection.setVisibility(View.GONE);

        ArrayList<ActivityDataModel> activityDataModels = new ArrayList<>();

        for (int i = 0 ; i < 7 ; i++) {
            ActivityDataModel activityDataModel = new ActivityDataModel();

            activityDataModel.setDay("11");
            activityDataModel.setMonth("OCT 2016");
            activityDataModel.setTime_range("3pm - 4pm");
            activityDataModel.setTitle("Visited Mary Kay Dallas and");
            activityDataModel.setDescription("something happened");

            activityDataModels.add(activityDataModel);
        }

        activity_adapter = new ActivityAdapter(activityDataModels, this);
        activities_listview.setAdapter(activity_adapter);
        activity_adapter.notifyDataSetChanged();

        //check relationship

        lines = (LinearLayout) findViewById(R.id.lines);
        schedules = (LinearLayout) findViewById(R.id.schedules);
        territories = (LinearLayout) findViewById(R.id.territories);
        clients = (LinearLayout) findViewById(R.id.clients);
        //goals = (LinearLayout) findViewById(R.id.goals);
        sales = (LinearLayout) findViewById(R.id.sales);

        back_layout.setOnClickListener(this);
        img_segment.setOnClickListener(this);



        img_segment.setBackgroundResource(R.drawable.general);
        checkCoworkers();
    }

    private void setListeners() {
        lines.setOnClickListener(this);
        schedules.setOnClickListener(this);
        territories.setOnClickListener(this);
        clients.setOnClickListener(this);
        // goals.setOnClickListener(this);
        sales.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.img_segment:
                bGeneral = !bGeneral;
                if (bGeneral) {
                    img_segment.setBackgroundResource(R.drawable.general);

                    bottom_layout.setVisibility(View.VISIBLE);
                    activitiesSection.setVisibility(View.GONE);
                } else {
                    img_segment.setBackgroundResource(R.drawable.activities);

                    bottom_layout.setVisibility(View.GONE);
                    activitiesSection.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.lines:
                bundle.putInt(Constants.SHOW_LINE, 0);
                bundle.putInt(Constants.SHOW_FILTER, 1);
                MyUtils.startActivity(this, LinesListActivity.class, bundle);
                break;
            case R.id.territories:
                bundle.putInt(Constants.SHOW_TERRITORY, 0);
                bundle.putInt(Constants.SHOW_FILTER, 1);
                MyUtils.startActivity(this, CoWorkerTerritoriesActivity.class, bundle);
                break;
            case R.id.schedules:
                MyUtils.startActivity(this, CoWorkerSchedulesActivity.class, bundle);
                break;
            case R.id.clients:
                bundle.putInt(Constants.SHOW_FILTER, 1);
                MyUtils.startActivity(this, ClientMapAndListActivity.class, bundle);
                break;
           // case R.id.goals:
            //    MyUtils.startActivity(this, CoWorkergGoalsActivity.class, bundle);
            //    break;
            case R.id.sales:
                MyUtils.startActivity(this, ActivityGroupedSales.class, bundle);
                break;
        }
    }
    public void checkCoworkers() {
        //super.loadData();
        //if(addLinesActivity.getLine()==null) return;
        Logger.write("Loading data");
        DialogUtils.displayProgress(this);
        CoworkerApi coworkerApi = MainApplication.createService(CoworkerApi.class);

        final Call<ResponseBody> response = coworkerApi.isCoworker( currentUser.getId(), coworker.getId());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                try {
                    //Logger.write(resResponse.body().string());
                    //  JSONObject response = new JSONObject(resResponse.body().string());
                    GeneralResponse response=new GeneralResponse(resResponse.body());
                    Logger.write(response.toString());
                    if (response != null && response.isAuthFailed()) {
                        User.LogOut(ViewCoWorkerActivity.this);
                        return;
                    }
                    Logger.write(response.toString());
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        List<CoWorker> coworkers= response.getDataAsList("coworkers", CoWorker.class);
                        if(coworkers.size()==0) {
                           TextView tv=(TextView)findViewById(R.id.no_results);
                            tv.setText("You have not been added as a coworker by "+coworker.getFirst_name());
                            tv.setVisibility(View.VISIBLE);
                            findViewById(R.id.bottom_layout).setAlpha(0.1f);
                        }else{
                            CoWorker coWorker=coworkers.get(0);
                            if(coWorker.getCan_see().getClients()) {
                                clients.setOnClickListener(ViewCoWorkerActivity.this);
                            }else {
                                clients.setAlpha(0.1f);
                            }
                            if(coWorker.getCan_see().getLine()) lines.setOnClickListener(ViewCoWorkerActivity.this);
                            else{
                                lines.setAlpha(0.1f);
                            }
                            if(coWorker.getCan_see().getSales()) sales.setOnClickListener(ViewCoWorkerActivity.this);
                            else sales.setAlpha(0.1f);

                            if(coWorker.getCan_see().getTerritory()) territories.setOnClickListener(ViewCoWorkerActivity.this);
                            else territories.setAlpha(0.1f);

                            if(coWorker.getCan_see().getSchedules()) schedules.setOnClickListener(ViewCoWorkerActivity.this);
                            else schedules.setAlpha(0.1f);

                            if(!coWorker.getCan_see().getActivities()) {
                                activities_listview.setVisibility(View.GONE);
                                TextView tv=(TextView)findViewById(R.id.no_results_2);
                                tv.setVisibility(View.VISIBLE);
                                tv.setText("You cannot view "+MyUtils.makePossesive(coworker.getFirst_name())+" activities");
                            }
                        }
                    } else {
                        MyUtils.showToast(getString(R.string.connection_error));
                    }
                }catch (Exception e) {
                    MyUtils.showConnectionErrorToast(ViewCoWorkerActivity.this);
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d(TAG + " onFailure", t.toString());
                Logger.write(t.getMessage());
                MyUtils.showConnectionErrorToast(ViewCoWorkerActivity.this);
                DialogUtils.closeProgress();
            }
        });


        // DialogUtils.displayProgress(this);


    }
}
