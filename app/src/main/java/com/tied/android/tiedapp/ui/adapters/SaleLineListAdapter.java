package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.LineDataModel;
import com.tied.android.tiedapp.ui.activities.sales.ActivitySalesDetails;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 6/28/2016.
 */
public class SaleLineListAdapter extends ClientParentAdapter {
    public static final String TAG = SaleLineListAdapter.class
            .getSimpleName();

    ViewHolder v;
    protected ArrayList<LineDataModel> arraylist = new ArrayList<LineDataModel>();
    Context context;

    public SaleLineListAdapter(ArrayList<LineDataModel> lines, Context context) {
        super(lines, context);
        this.arraylist = lines;
        this.context = context;
    }


    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (view == null) {
            v = new ViewHolder();
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.sale_line_list_item, viewGroup, false);

            LineDataModel data = (LineDataModel) _data.get(i);

            v.item_cell = (RelativeLayout) view.findViewById(R.id.item_cell);
            v.item_cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyUtils.startActivity(context, ActivitySalesDetails.class);
                }
            });

            v.line_name = (TextView) view.findViewById(R.id.txt_line_name);
            v.line_date = (TextView) view.findViewById(R.id.txt_date);
            v.percent = (TextView) view.findViewById(R.id.txt_percent);
            v.price = (TextView) view.findViewById(R.id.txt_price);

            if (i % 2 == 0) {
                v.item_cell.setBackgroundResource(R.color.white);
            } else {
                v.item_cell.setBackgroundResource(R.color.light_grey3);
            }

            v.line_name.setText(data.getLine_name());
            v.line_date.setText(String.format("Last sale: %s", data.getLine_date()));
            v.percent.setText(String.format("(%s%s)", data.getPercent(), "%"));
            v.price.setText(data.getPrice());

            view.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

        return view;
    }

    @Override
    public void listInit(ArrayList arrayList) {
        this._data = arrayList;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        RelativeLayout item_cell;
        ImageView imageView;
        TextView line_name, line_date, price, percent;
    }

}

