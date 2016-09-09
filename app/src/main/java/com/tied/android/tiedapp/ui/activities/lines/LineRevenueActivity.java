package com.tied.android.tiedapp.ui.activities.lines;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Revenue;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.activities.sales.ActivityAddSales;
import com.tied.android.tiedapp.ui.adapters.RevenueAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import com.tied.android.tiedapp.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by femi on 9/4/2016.
 */
public class LineRevenueActivity  extends AppCompatActivity implements  View.OnClickListener{

    private Bundle bundle;
    private User user;
    View backButton;
    List revenueList=new ArrayList<Revenue>();;
    int page=1;
    RevenueAdapter adapter;
    ListView revenueLV;
    View addBut;
    TextView totalRevenue;

    private TextView name, description;
    private Line line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_revenues);

        bundle = getIntent().getExtras();
        line = (Line) bundle.getSerializable(Constants.LINE_DATA);
        user = MyUtils.getUserFromBundle(bundle);
        initComponents();
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id) {
            case R.id.add:
                MyUtils.startRequestActivity(this, ActivityAddSales.class, Constants.ADD_SALES, bundle);
                break;
        }
    }

    public void initComponents() {
        revenueLV=(ListView)findViewById(R.id.list);
        addBut = findViewById(R.id.add);
        addBut.setOnClickListener(this);

        adapter=new RevenueAdapter(this, revenueList);
        revenueLV.setAdapter(adapter);
        totalRevenue = (TextView) findViewById(R.id.txt_total_revenue);

    }


    public void loadData() {
        //super.loadData();
        //if(addLinesActivity.getLine()==null) return;
        Logger.write("Loading data");
        DialogUtils.displayProgress(this);
        LineApi lineApi = MainApplication.getInstance().getRetrofit().create(LineApi.class);
        final Call<ResponseBody> response = lineApi.getLineRevenues(user.getToken(), line.getId(), page);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                try {
                    //Logger.write(resResponse.body().string());
                    //  JSONObject response = new JSONObject(resResponse.body().string());
                    GeneralResponse response=new GeneralResponse(resResponse.body());

                    if (response != null && response.isAuthFailed()) {
                        User.LogOut(LineRevenueActivity.this);
                        return;
                    }
                    Logger.write(response.toString());
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        revenueList.addAll(response.getDataAsList("revenues", Revenue.class));
                        adapter.notifyDataSetChanged();
                    } else {
                        MyUtils.showToast(getString(R.string.connection_error));
                    }
                }catch (Exception e) {
                    MyUtils.showConnectionErrorToast(LineRevenueActivity.this);
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d(TAG + " onFailure", t.toString());
                Logger.write(t.getMessage());
                MyUtils.showConnectionErrorToast(LineRevenueActivity.this);
                DialogUtils.closeProgress();
            }
        });


       // DialogUtils.displayProgress(this);


    }

    public void setLineTotalRevenue() {
        LineApi lineApi = MainApplication.getInstance().getRetrofit().create(LineApi.class);
        final Call<ResponseBody> response2 = lineApi.getLineTotalRevenue(user.getToken(), line.getId());
        response2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                try {
                    //Logger.write(resResponse.body().string());
                    //  JSONObject response = new JSONObject(resResponse.body().string());
                    GeneralResponse response=new GeneralResponse(resResponse.body());
                    // Logger.write("RESPONSSSSSSSSSSSSSSSSSSSS "+response.toString());
                    if (response != null && response.isAuthFailed()) {
                        User.LogOut(LineRevenueActivity.this);
                        return;
                    }

                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        // revenueList.addAll(response.getDataAsList("revenues", Revenue.class));
                        // adapter.notifyDataSetChanged();
                        line.setTotal_revenue(response.getData("line", Line.class).getTotal_revenue());
                        totalRevenue.setText(MyUtils.moneyFormat(line.getTotal_revenue()));
                        //totalRevenueBodyTV.setText(MyUtils.moneyFormat(line.getTotal_revenue()));

                    } else {
                        // MyUtils.showToast(getString(R.string.connection_error));
                    }
                }catch (Exception e) {
                    // MyUtils.showConnectionErrorToast(LineRevenueActivity.this);
                    //Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d(TAG + " onFailure", t.toString());
                Logger.write(t.getMessage());
                MyUtils.showConnectionErrorToast(LineRevenueActivity.this);
                DialogUtils.closeProgress();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        revenueList.clear();
        loadData();
        setLineTotalRevenue();
    }

    public void goBack(View v) {
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Logger.write("REgdlkadf ajsdpfjasdf "+requestCode);
        if(requestCode==Constants.ADD_SALES && requestCode==RESULT_OK) {
            revenueList.clear();
            loadData();
        }
    }
}
