package com.tied.android.tiedapp.ui.activities.territories;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Territory;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.retrofits.services.TerritoryApi;
import com.tied.android.tiedapp.ui.adapters.LineTerritoriesAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by femi on 10/4/2016.
 */
public class ActivityTerritories extends AppCompatActivity implements  View.OnClickListener{

private Bundle bundle;
private User user;

        ListView territories_listview;
        LineTerritoriesAdapter territoriesAdapter;

        ImageView img_close, img_edit;
        int page_index;
    ArrayList<Territory> territories =new ArrayList<>();
        TextView txt_client_info, txt_description;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_territory);

                    bundle = getIntent().getExtras();
                user=MyUtils.getUserFromBundle(bundle);
                    page_index = bundle.getInt(Constants.SHOW_LINE);

                    MyUtils.setColorTheme(this, bundle.getInt(Constants.SOURCE), findViewById(R.id.main_layout));

                    initComponents();
            }

        @Override
        public void onClick(View view) {
                int id=view.getId();
                switch (id) {
                case R.id.img_close:
                finish();
                break;
                case R.id.img_edit:
                //MyUtils.startActivity(this, LineSelectTerritoriesActivity.class, bundle);
                break;
                }
        }

        public void initComponents() {

                img_close = (ImageView) findViewById(R.id.img_close);
                img_close.setOnClickListener(this);

                img_edit = (ImageView) findViewById(R.id.img_edit);
                img_edit.setOnClickListener(this);

                txt_client_info = (TextView) findViewById(R.id.txt_client_info);
                txt_description = (TextView) findViewById(R.id.txt_description);

                if (page_index == 0) {
                txt_client_info.setText("Lines");
                txt_description.setText("You currently serve 20 lines for");
                } else {
                txt_client_info.setText("Territories");
                txt_description.setText("You currently serve 20 territories for");
                }
                territories_listview = (ListView) findViewById(R.id.listView);

                ArrayList<TerritoryModel> territoryModels = new ArrayList<TerritoryModel>();
                for (int i = 0 ; i < 10; i++) {
                TerritoryModel territoryModel = new TerritoryModel();

                territoryModel.setTerritory_name("Denton Tap, MD");
                if (i % 2 == 0) {
                territoryModel.setCheck_status(true);
                } else {
                territoryModel.setCheck_status(false);
                }

                territoryModels.add(territoryModel);
                }

                territoriesAdapter = new LineTerritoriesAdapter(territoryModels, this);
                territories_listview.setAdapter(territoriesAdapter);
                territoriesAdapter.notifyDataSetChanged();
            initTerritories();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                //super.onActivityResult(requestCode, resultCode, data);
                Logger.write("REgdlkadf ajsdpfjasdf "+requestCode);
                if(requestCode==Constants.ADD_SALES && requestCode==RESULT_OK) {

                }
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
                        User.LogOut(ActivityTerritories.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code() == 200) {

                        territories.addAll( (ArrayList) response.getDataAsList(Constants.TerritoryData, Territory.class));
                        //Logger.write("Lines loadeddddddddddddddddddddddddddddddddddddddddddddddd "+territories.size());
                        territoriesAdapter.notifyDataSetChanged();
                        if(territories.isEmpty()) MyUtils.showNoResults(ActivityTerritories.this.findViewById(R.id.main_layout), R.id.no_results);
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
}
