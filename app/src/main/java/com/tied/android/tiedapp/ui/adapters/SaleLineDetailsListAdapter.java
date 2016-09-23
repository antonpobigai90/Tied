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
public class SaleLineDetailsListAdapter extends ClientParentAdapter {
    public static final String TAG = SaleLineDetailsListAdapter.class
            .getSimpleName();

    ViewHolder v;
    protected ArrayList<LineDataModel> arraylist = new ArrayList<LineDataModel>();
    Context context;

    public SaleLineDetailsListAdapter(ArrayList<LineDataModel> lines, Context context) {
        super(lines, context);
        this.arraylist = lines;
        this.context = context;
    }


    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (view == null) {
            v = new ViewHolder();
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.sale_client_list_item, viewGroup, false);

            LineDataModel data = (LineDataModel) _data.get(i);

            v.line_name = (TextView) view.findViewById(R.id.txt_client_name);
            v.line_date = (TextView) view.findViewById(R.id.txt_date);
            v.price = (TextView) view.findViewById(R.id.txt_price);

            v.line_name.setText(data.getLine_name());
            v.line_date.setText(data.getLine_date());
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
        TextView line_name, line_date, price;
    }

}

