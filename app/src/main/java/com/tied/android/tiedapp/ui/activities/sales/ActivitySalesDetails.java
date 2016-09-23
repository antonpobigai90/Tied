package com.tied.android.tiedapp.ui.activities.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.ClientDataModel;
import com.tied.android.tiedapp.customs.model.LineDataModel;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.GeneralSelectObjectActivity;
import com.tied.android.tiedapp.ui.adapters.SaleLineDetailsListAdapter;
import com.tied.android.tiedapp.ui.adapters.SaleLineListAdapter;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

/**
 * Created by femi on 8/4/2016.
 */
public class ActivitySalesDetails extends AppCompatActivity implements  View.OnClickListener{
    private Bundle bundle;
    private User user;

    private ImageView img_back, img_filter;

    private ListView lines_listview;
    private SaleLineDetailsListAdapter line_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_line_details);

        bundle = getIntent().getExtras();

        initComponent();
    }
    private void initComponent() {

        img_back = (ImageView) findViewById(R.id.img_back);
        img_filter = (ImageView) findViewById(R.id.img_filter);

        img_back.setOnClickListener(this);
        img_filter.setOnClickListener(this);

        lines_listview = (ListView) findViewById(R.id.lines_listview);

        ArrayList<LineDataModel> lineDataModels = new ArrayList<>();
        ArrayList<ClientDataModel> clientDataModels = new ArrayList<>();

        for (int i = 0 ; i < 5 ; i++) {
            LineDataModel lineDataModel = new LineDataModel();

            lineDataModel.setLine_name("CREATIVE CO-OP");
            lineDataModel.setLine_date("January");
            lineDataModel.setPrice("$229,900");

            lineDataModels.add(lineDataModel);
        }

        line_adapter = new SaleLineDetailsListAdapter(lineDataModels, this);
        lines_listview.setAdapter(line_adapter);
        line_adapter.notifyDataSetChanged();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_filter:
                break;
        }
    }

    public void goBack(View v) {
        onBackPressed();
    }
}