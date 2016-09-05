package com.tied.android.tiedapp.ui.fragments.lines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Revenue;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.activities.lines.AddLinesActivityOld;
import com.tied.android.tiedapp.ui.activities.sales.ActivityAddSales;
import com.tied.android.tiedapp.ui.adapters.RevenueAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.fragments.MyFormFragment;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 6/22/2016.
 */
public class RevenueFragmentOld extends MyFormFragment implements View.OnClickListener{
    private ImageView avatar;
    private ListView revenueLV;
    RevenueAdapter adapter;
    List<Revenue> revenueList;
    AddLinesActivityOld addLinesActivity;
    int page=1;
    View view, addBut;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_line_revenue, container, false);
        addLinesActivity=(AddLinesActivityOld) getActivity();
        revenueList = new ArrayList<Revenue>();
        adapter = new RevenueAdapter(getActivity(),revenueList);
        initComponents();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.avatar:
                break;
            case R.id.add_revenue:
                MyUtils.startRequestActivity(getActivity(), ActivityAddSales.class, Constants.ADD_SALES);
                break;
        }
    }
    @Override
    public void initComponents() {
        revenueLV=(ListView)view.findViewById(R.id.revenue_lv);
        addBut = view.findViewById(R.id.add_revenue);
        addBut.setOnClickListener(this);
        revenueLV.setAdapter(adapter);
        loadData();
    }

    @Override
    public void loadData() {
        //super.loadData();
        if(addLinesActivity.getLine()==null) return;
        Logger.write("Loading data");
        DialogUtils.displayProgress(getActivity());
        LineApi lineApi = MainApplication.getInstance().getRetrofit().create(LineApi.class);
        final Call<ResponseBody> response = lineApi.getLineRevenues(addLinesActivity.getUser().getToken(), addLinesActivity.getLine().getId(), page);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (getActivity() == null) return;


                DialogUtils.closeProgress();
                try {
                    //Logger.write(resResponse.body().string());
                  //  JSONObject response = new JSONObject(resResponse.body().string());
                    GeneralResponse response=new GeneralResponse(resResponse.body());

                    if (response != null && response.isAuthFailed()) {
                        User.LogOut(getActivity());
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
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d(TAG + " onFailure", t.toString());
                Logger.write(t.getMessage());
                DialogUtils.closeProgress();
            }
        });
    }
}
