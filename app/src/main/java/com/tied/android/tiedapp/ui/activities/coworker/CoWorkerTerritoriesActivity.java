package com.tied.android.tiedapp.ui.activities.coworker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.objects.Territory;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.TerritoryApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.territories.ActivityTerritories;
import com.tied.android.tiedapp.ui.adapters.ClientTerritoriesAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class CoWorkerTerritoriesActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = CoWorkerTerritoriesActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    LinearLayout back_layout, add_button;
    TextView txt_title;

    ListView line_listview;
    RelativeLayout top_layout;
    ClientTerritoriesAdapter territoriesAdapter;
    ArrayList<Territory> territoryModels = new ArrayList<Territory>();
    ArrayList<Territory> selectedTerritories = new ArrayList<Territory>();
    Boolean signle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_worker_line);

        bundle = getIntent().getExtras();
        int page_index = bundle.getInt(Constants.SHOW_TERRITORY);
        int filter_index = bundle.getInt(Constants.SHOW_FILTER);
        signle = bundle.getBoolean("single");

        user = MyUtils.getUserFromBundle(bundle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            if (filter_index == 0) {
                window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.gradient));
            }
        }

        top_layout = (RelativeLayout) findViewById(R.id.top_layout);
        if (filter_index == 0) {
            top_layout.setBackgroundResource(R.drawable.background_blue);
        } else {
            top_layout.setBackgroundResource(R.drawable.background_gradient);
        }

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_layout.setOnClickListener(this);

        add_button = (LinearLayout) findViewById(R.id.add_button);
        add_button.setOnClickListener(this);

        txt_title = (TextView) findViewById(R.id.txt_title);
        line_listview = (ListView) findViewById(R.id.lines_listview);

        String title = (page_index == 0) ? "Territories" : "Filter Territory";
        txt_title.setText(title);

        territoriesAdapter = new ClientTerritoriesAdapter(page_index, territoryModels, this);
        line_listview.setAdapter(territoriesAdapter);
        territoriesAdapter.notifyDataSetChanged();

        initTerritories();

        line_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Territory item = territoryModels.get(position);

                if (!signle) {
                    if (territoryModels.get(position).isCheck_status()) {
                        territoryModels.get(position).setCheck_status(false);

                        for (int i = 0; i < selectedTerritories.size(); i++) {
                            Territory selectedTerritory = selectedTerritories.get(i);

                            if (selectedTerritory.getId().equals(item.getId())) {
                                selectedTerritories.remove(i);
                            }
                        }
                    } else {
                        territoryModels.get(position).setCheck_status(true);

                        selectedTerritories.add(item);
                    }
                } else {
                    selectedTerritories.clear();
                    territoryModels.get(position).setCheck_status(true);
                    selectedTerritories.add(item);

                    for (int i = 0 ; i < territoryModels.size() ; i++) {
                        if (!item.getId().equals(territoryModels.get(i).getId())) {
                            territoryModels.get(i).setCheck_status(false);
                        }
                    }
                }

                territoriesAdapter.notifyDataSetChanged();
            }
        });
    }

    public void initTerritories(){
        final TerritoryApi territoryApi =  MainApplication.createService(TerritoryApi.class);
        Call<ResponseBody> response = territoryApi.getTerritories(user.getId(), 1);
        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    Logger.write(response.toString());
                    if (response.isAuthFailed()) {
                        User.LogOut(CoWorkerTerritoriesActivity.this);
                        return;
                    }
                    _Meta meta=response.getMeta();


                    if(meta !=null && meta.getStatus_code() == 200) {

                        territoryModels.addAll( (ArrayList) response.getDataAsList(Constants.TerritoryData, Territory.class));
                        //Logger.write("Lines loadeddddddddddddddddddddddddddddddddddddddddddddddd "+territories.size());
                        territoriesAdapter.notifyDataSetChanged();
                        if( territoryModels.isEmpty()) MyUtils.showNoResults(CoWorkerTerritoriesActivity.this.findViewById(R.id.parent), R.id.no_results);
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(" onFailure", t.toString());
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                finish();
                break;
            case R.id.add_button:
                Intent intent = new Intent();
                Bundle b =new Bundle();
                b.putSerializable("selected", selectedTerritories);

                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finishActivity(Constants.SELECT_TERRITORY);
                finish();
                break;
        }
    }
}
