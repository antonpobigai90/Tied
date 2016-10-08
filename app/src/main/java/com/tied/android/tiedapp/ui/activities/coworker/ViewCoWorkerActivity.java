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

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.ActivityDataModel;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.lines.LinesListActivity;
import com.tied.android.tiedapp.ui.activities.client.ClientMapAndListActivity;
import com.tied.android.tiedapp.ui.activities.sales.ActivityGroupedSales;
import com.tied.android.tiedapp.ui.adapters.ActivityAdapter;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

public class ViewCoWorkerActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = ViewCoWorkerActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    LinearLayout back_layout;
    private ImageView avatar, img_segment;
    private TextView name;
    private User coworker;

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
        user = MyUtils.getUserFromBundle(bundle);
        bundle.putInt(Constants.SOURCE, Constants.COWORKER_SOURCE);
        coworker = (User) bundle.getSerializable(Constants.USER_DATA);

        name = (TextView) findViewById(R.id.name);
        avatar = (ImageView) findViewById(R.id.avatar);
        img_segment  = (ImageView) findViewById(R.id.img_segment);

        name.setText(user.getFirst_name()+" "+user.getLast_name());
        MyUtils.Picasso.displayImage(user.getAvatarURL(), avatar);

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);
        activities_listview = (ListView) findViewById(R.id.activities_listview);
        activities_listview.setVisibility(View.GONE);

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

        lines = (LinearLayout) findViewById(R.id.lines);
        schedules = (LinearLayout) findViewById(R.id.schedules);
        territories = (LinearLayout) findViewById(R.id.territories);
        clients = (LinearLayout) findViewById(R.id.clients);
        //goals = (LinearLayout) findViewById(R.id.goals);
        sales = (LinearLayout) findViewById(R.id.sales);

        back_layout.setOnClickListener(this);
        img_segment.setOnClickListener(this);

        lines.setOnClickListener(this);
        schedules.setOnClickListener(this);
        territories.setOnClickListener(this);
        clients.setOnClickListener(this);
       // goals.setOnClickListener(this);
        sales.setOnClickListener(this);

        img_segment.setBackgroundResource(R.drawable.general);
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
                    activities_listview.setVisibility(View.GONE);
                } else {
                    img_segment.setBackgroundResource(R.drawable.activities);

                    bottom_layout.setVisibility(View.GONE);
                    activities_listview.setVisibility(View.VISIBLE);
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
}
