package com.tied.android.tiedapp.ui.activities.lines;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.adapters.ClientTerritoriesAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Femi on 7/30/2016.
 */
public class LineAddTerritoryActivity extends AppCompatActivity implements  View.OnClickListener{
    private static final String TAG = LineAddTerritoryActivity.class.getSimpleName();
    private Bundle bundle;
    private User user;

    TextView txt_add, selected_count;
    ImageView img_close;
    EditText search;
    LinearLayout add_layout;

    ListView line_listview;
    ClientTerritoriesAdapter territoriesAdapter;
    ArrayList<TerritoryModel> territoryModels = new ArrayList<TerritoryModel>();

    int total_selected_cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_line_territories);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);

        initComponent();
    }

    private void initComponent() {

        img_close =(ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        txt_add = (TextView) findViewById(R.id.txt_add);
        txt_add.setOnClickListener(this);
        search = (EditText) findViewById(R.id.search);

        add_layout = (LinearLayout) findViewById(R.id.add_layout);
        selected_count = (TextView) findViewById(R.id.selected_count);

        line_listview = (ListView) findViewById(R.id.list);

        for (int i = 0 ; i < 10; i++) {
            TerritoryModel territoryModel = new TerritoryModel();

            territoryModel.setTerritory_name("Coppell, TX");
            territoryModel.setNo_clients(30);

            territoryModels.add(territoryModel);
        }

        territoriesAdapter = new ClientTerritoriesAdapter(2, territoryModels, this);
        line_listview.setAdapter(territoriesAdapter);
        territoriesAdapter.notifyDataSetChanged();

        line_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (territoryModels.get(position).isCheck_status()) {
                    territoryModels.get(position).setCheck_status(false);
                    total_selected_cnt--;
                } else {
                    territoryModels.get(position).setCheck_status(true);
                    total_selected_cnt++;
                }

                territoriesAdapter.notifyDataSetChanged();

                if (total_selected_cnt > 0) {
                    add_layout.setVisibility(View.VISIBLE);
                    selected_count.setText(String.format("Add %d territories to line", total_selected_cnt));
                } else {
                    add_layout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_add:

                break;
            case R.id.img_close:
                finish();
                break;
        }
    }

}
