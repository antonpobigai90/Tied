package com.tied.android.tiedapp.ui.activities.coworker;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.ClientTerritoriesAdapter;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

public class CoWorkerTerritoriesActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = CoWorkerTerritoriesActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    LinearLayout back_layout;
    TextView txt_title;

    ListView line_listview;
    ClientTerritoriesAdapter territoriesAdapter;
    ArrayList<TerritoryModel> territoryModels = new ArrayList<TerritoryModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_worker_line);

        bundle = getIntent().getExtras();
        int page_index = bundle.getInt(Constants.SHOW_TERRITORY);

        user = MyUtils.getUserFromBundle(bundle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gradient));
        }

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);

        txt_title = (TextView) findViewById(R.id.txt_title);
        line_listview = (ListView) findViewById(R.id.lines_listview);

        String title = (page_index == 0) ? "Territories" : "Filter Territory";
        txt_title.setText(title);

        for (int i = 0 ; i < 10; i++) {
            TerritoryModel territoryModel = new TerritoryModel();

            territoryModel.setTerritory_name("lroko Technologies LLC");
            territoryModel.setNo_clients(30);

            territoryModels.add(territoryModel);
        }

        territoriesAdapter = new ClientTerritoriesAdapter(page_index, territoryModels, this);
        line_listview.setAdapter(territoriesAdapter);
        territoriesAdapter.notifyDataSetChanged();

        line_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (territoryModels.get(position).isCheck_status()) {
                    territoryModels.get(position).setCheck_status(false);
                } else {
                    territoryModels.get(position).setCheck_status(true);
                }

                territoriesAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                super.onBackPressed();
                break;
        }
    }
}
