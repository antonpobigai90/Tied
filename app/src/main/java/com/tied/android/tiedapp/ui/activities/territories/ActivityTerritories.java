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
import android.widget.AdapterView;
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
import com.tied.android.tiedapp.ui.activities.client.ActivityClientProfile;
import com.tied.android.tiedapp.ui.activities.lines.LineClientListActivity;
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

import static com.tied.android.tiedapp.customs.Constants.Territory;

/**
 * Created by femi on 10/4/2016.
 */
public class ActivityTerritories extends AppCompatActivity implements  View.OnClickListener, AdapterView.OnItemClickListener{

        public static final String TAG = ActivityTerritories.class
            .getSimpleName();

        private Bundle bundle;
        private User user;

        ListView territories_listview;
        LineTerritoriesAdapter territoriesAdapter;

        ImageView img_close, img_edit;
        int page_index;

        ArrayList<Territory> territoryModels = new ArrayList<Territory>();
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
                    case R.id.img_add:
                        bundle.putSerializable("my_territories", territoryModels);
                        MyUtils.startRequestActivity(this, ActivityAddTerritory.class, Constants.ADD_TERRITORY, bundle);
                        break;
                }
        }

        public void initComponents() {

                img_close = (ImageView) findViewById(R.id.img_close);
                img_close.setOnClickListener(this);

               // img_edit = (ImageView) findViewById(R.id.img_edit);
               // img_edit.setOnClickListener(this);

                txt_client_info = (TextView) findViewById(R.id.txt_client_info);
                txt_description = (TextView) findViewById(R.id.txt_description);


                txt_client_info.setText("Territories");
                txt_description.setText("You currently serve 20 territories for");

                territories_listview = (ListView) findViewById(R.id.listView);
                territories_listview.setOnItemClickListener(this);

                territoriesAdapter = new LineTerritoriesAdapter(territoryModels, this);
                territories_listview.setAdapter(territoriesAdapter);
                territoriesAdapter.notifyDataSetChanged();
                initTerritories();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                //super.onActivityResult(requestCode, resultCode, data);
                Logger.write("REgdlkadf ajsdpfjasdf "+requestCode);
                if(requestCode==Constants.ADD_TERRITORY && resultCode==RESULT_OK) {
                    MyUtils.showMessageAlert(this, "Territory successfully added!");
                    territoryModels.clear();
                    territoriesAdapter.notifyDataSetChanged();
                    territories_listview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initTerritories();
                        }
                    }, 200);

                }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, "here---------------- listener");
            //Client client = clients.get(position);
            Bundle bundle =new Bundle();
            bundle.putSerializable(Constants.USER_DATA, user);
            bundle.putSerializable(Constants.TERRITORY_DATA, territoryModels.get(position));
            bundle.putString(Constants.CLIENT_LIST, "territory");
            MyUtils.startActivity(this, LineClientListActivity.class, bundle);
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

                            territoryModels.addAll( (ArrayList) response.getDataAsList(Constants.TerritoryData, Territory.class));
                            //Logger.write("Lines loadeddddddddddddddddddddddddddddddddddddddddddddddd "+territories.size());
                            territoriesAdapter.notifyDataSetChanged();
                            if( territoryModels.isEmpty()) MyUtils.showNoResults(ActivityTerritories.this.findViewById(R.id.main_layout), R.id.no_results);
                        }else{
                            MyUtils.showToast("Error encountered");
                            DialogUtils.closeProgress();
                        }
                        if(territoryModels.size()>0) findViewById(R.id.no_results).setVisibility(View.GONE);
                        else findViewById(R.id.no_results).setVisibility(View.VISIBLE);

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
