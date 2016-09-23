package com.tied.android.tiedapp.ui.fragments.sales;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.*;

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
import com.tied.android.tiedapp.customs.model.*;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.sales.ActivityAddSales;
import com.tied.android.tiedapp.ui.activities.sales.ActivitySalesFilter;
import com.tied.android.tiedapp.ui.activities.sales.ActivitySalesPrint;
import com.tied.android.tiedapp.ui.adapters.*;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.AllScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.NextWeekScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.ThisMonthScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.ThisWeekScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.tabs.TodayScheduleFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class SaleFragment extends Fragment implements OnChartValueSelectedListener, View.OnClickListener {

    public static final String TAG = SaleFragment.class
            .getSimpleName();

    private Bundle bundle;

    public FragmentIterationListener mListener;

    private ImageView img_segment, img_printer, img_plus, img_filter;
    private LinearLayout sub_layout, line_layout, client_layout;

    private SaleLineListAdapter line_adapter;
    private SaleClientListAdapter client_adapter;

    private ListView lines_listview, client_listview;

    private TextView txt_view_all;
    private PieChart mChart;

    boolean bLine = true;

    protected String[] mParties = new String[] {
            "Party A", "Party B", "Party C", "Party D"
    };

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new SaleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_home, null);

        initComponent(view);

        Log.d(TAG, "AM HERE AGAIN");
        return view;
    }

    public void initComponent(View view) {
        // Set up the ViewPager with the sections adapter.

        bundle = getArguments();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.green_color1));
        }

        img_segment = (ImageView) view.findViewById(R.id.img_segment);
        img_segment.setBackgroundResource(R.drawable.line);
        img_segment.setOnClickListener(this);

        img_printer = (ImageView) view.findViewById(R.id.img_printer);
        img_filter = (ImageView) view.findViewById(R.id.img_filter);
        img_plus = (ImageView) view.findViewById(R.id.img_plus);

        img_printer.setOnClickListener(this);
        img_filter.setOnClickListener(this);
        img_plus.setOnClickListener(this);

        sub_layout = (LinearLayout) view.findViewById(R.id.sub_layout);
        sub_layout.setVisibility(View.GONE);

        line_layout = (LinearLayout) view.findViewById(R.id.line_layout);
        client_layout = (LinearLayout) view.findViewById(R.id.client_layout);
        client_layout.setVisibility(View.GONE);

        txt_view_all = (TextView) view.findViewById(R.id.txt_view_all);
        txt_view_all.setOnClickListener(this);

        lines_listview = (ListView) view.findViewById(R.id.lines_listview);
        client_listview = (ListView) view.findViewById(R.id.client_listview);

        ArrayList<LineDataModel> lineDataModels = new ArrayList<>();
        ArrayList<ClientDataModel> clientDataModels = new ArrayList<>();

        for (int i = 0 ; i < 4 ; i++) {
            LineDataModel lineDataModel = new LineDataModel();

            lineDataModel.setLine_name("CREATIVE CO-OP");
            lineDataModel.setLine_date("Last sale: 5 days ago");
            lineDataModel.setPercent("48");
            lineDataModel.setPrice("$1,200,400");

            lineDataModels.add(lineDataModel);
        }

        line_adapter = new SaleLineListAdapter(0, lineDataModels, getActivity());
        lines_listview.setAdapter(line_adapter);
        line_adapter.notifyDataSetChanged();

        for (int i = 0 ; i < 7 ; i++) {
            ClientDataModel clientDataModel = new ClientDataModel();

            clientDataModel.setLine_name("Emmanuel lroko");
            clientDataModel.setLine_date("80 sales");
            clientDataModel.setPercent("48");
            clientDataModel.setPrice("$1,200,400");

            clientDataModels.add(clientDataModel);
        }

        client_adapter = new SaleClientListAdapter(clientDataModels, getActivity());
        client_listview.setAdapter(client_adapter);
        client_adapter.notifyDataSetChanged();

        mChart = (PieChart) view.findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        setData(3, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        Logger.write("It is created again");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_segment:
                bLine = !bLine;
                if (bLine) {
                    line_layout.setVisibility(View.VISIBLE);

                    sub_layout.setVisibility(View.GONE);
                    client_layout.setVisibility(View.GONE);

                    img_segment.setBackgroundResource(R.drawable.line);
                } else {
                    line_layout.setVisibility(View.GONE);

                    sub_layout.setVisibility(View.VISIBLE);
                    client_layout.setVisibility(View.VISIBLE);

                    img_segment.setBackgroundResource(R.drawable.clients);
                }
                break;
            case R.id.img_filter:
                MyUtils.startActivity(getActivity(), ActivitySalesFilter.class);
                break;
            case R.id.img_printer:
                MyUtils.startActivity(getActivity(), ActivitySalesPrint.class);
                break;
            case R.id.img_plus:
                MyUtils.startActivity(getActivity(), ActivityAddSales.class);
                break;
            case R.id.txt_view_all:
                ((MainActivity) getActivity()).launchFragment(Constants.SaleViewAll, bundle);
                break;
        }
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("$1,500.54\nTotal Sales for Feb, 2016");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 9, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 9, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 9, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(.8f), 9, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
