package com.tied.android.tiedapp.ui.fragments.sales;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.ClientDataModel;
import com.tied.android.tiedapp.customs.model.LineDataModel;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.sales.ActivityAddSales;
import com.tied.android.tiedapp.ui.activities.sales.ActivitySalesFilter;
import com.tied.android.tiedapp.ui.activities.sales.ActivitySalesPrint;
import com.tied.android.tiedapp.ui.adapters.SaleClientListAdapter;
import com.tied.android.tiedapp.ui.adapters.SaleLineListAdapter;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class SaleViewAllFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = SaleViewAllFragment.class
            .getSimpleName();

    private Bundle bundle;

    public FragmentIterationListener mListener;

    private ImageView img_back, img_plus, img_filter;
    private LinearLayout line_layout;

    private SaleLineListAdapter line_adapter;

    private ListView lines_listview;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new SaleViewAllFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_view_all, null);

        initComponent(view);

        Log.d(TAG, "AM HERE AGAIN");
        return view;
    }

    public void initComponent(View view) {
        // Set up the ViewPager with the sections adapter.

        bundle = getArguments();

        img_back = (ImageView) view.findViewById(R.id.img_back);
        img_filter = (ImageView) view.findViewById(R.id.img_filter);
        img_plus = (ImageView) view.findViewById(R.id.img_plus);

        img_back.setOnClickListener(this);
        img_filter.setOnClickListener(this);
        img_plus.setOnClickListener(this);

        line_layout = (LinearLayout) view.findViewById(R.id.line_layout);

        lines_listview = (ListView) view.findViewById(R.id.lines_listview);

        ArrayList<LineDataModel> lineDataModels = new ArrayList<>();

        for (int i = 0 ; i < 10 ; i++) {
            LineDataModel lineDataModel = new LineDataModel();

            lineDataModel.setLine_name("CREATIVE CO-OP");
            lineDataModel.setLine_date("5 days ago");
            lineDataModel.setPercent("48");
            lineDataModel.setPrice("$1,200,400");

            lineDataModels.add(lineDataModel);
        }

        line_adapter = new SaleLineListAdapter(lineDataModels, getActivity());
        lines_listview.setAdapter(line_adapter);
        line_adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                ((MainActivity) getActivity()).launchFragment(Constants.HomeSale, bundle);
                break;
            case R.id.img_printer:
                MyUtils.startActivity(getActivity(), ActivitySalesPrint.class);
                break;
            case R.id.img_plus:
                MyUtils.startActivity(getActivity(), ActivityAddSales.class);
                break;
        }
    }


}
