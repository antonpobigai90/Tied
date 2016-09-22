package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.ClientDataModel;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 6/28/2016.
 */
public class SaleClientListAdapter extends ClientParentAdapter {
    public static final String TAG = SaleClientListAdapter.class
            .getSimpleName();

    ViewHolder v;
    protected ArrayList<ClientDataModel> arraylist = new ArrayList<ClientDataModel>();

    public SaleClientListAdapter(ArrayList clients, Context context) {
        super(clients, context);
        this.arraylist = clients;
    }


    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (view == null) {
            v = new ViewHolder();
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.sale_client_list_item, viewGroup, false);

            ClientDataModel data = (ClientDataModel) _data.get(i);

            v.name = (TextView) view.findViewById(R.id.txt_client_name);
            v.month = (TextView) view.findViewById(R.id.txt_date);
            v.price = (TextView) view.findViewById(R.id.txt_price);

            v.name.setText(data.getClient_name());
            v.month.setText(data.getMonth());
            v.price.setText(data.getPrice());

            view.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();;
        }

        return view;
    }

    @Override
    public void listInit(ArrayList arrayList) {
        this._data = arrayList;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        TextView name,month,price;
    }

}

