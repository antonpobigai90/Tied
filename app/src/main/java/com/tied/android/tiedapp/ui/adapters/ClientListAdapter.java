package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.util.RoundImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Emmanuel on 6/28/2016.
 */
public class ClientListAdapter extends BaseAdapter {
    public static final String TAG = ClientListAdapter.class
            .getSimpleName();

    public List _data;
    private ArrayList arraylist;
    Context _c;
    ViewHolder v;
    RoundImage roundedImage;

    public ClientListAdapter(ArrayList clients, Context context) {
        _data = clients;
        _c = context;
        this.arraylist = clients;
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int i) {
        return _data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (view == null) {
            v = new ViewHolder();
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.schedule_select_client_list_item, viewGroup, false);

            Client data = (Client) _data.get(i);

            v.name = (TextView) view.findViewById(R.id.name);
            v.address = (TextView) view.findViewById(R.id.address);
            v.imageView = (ImageView) view.findViewById(R.id.pic);

            v.name.setText(data.getFull_name());
            v.address.setText(data.getAddress().getLocationAddress());
            MyUtils.Picasso.displayImage(data.getLogo(), v.imageView);

            view.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();;
        }

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        _data.clear();
        if (charText.length() == 0) {
            _data.addAll(arraylist);
        } else {
            for (Object wp : arraylist) {
                if(wp instanceof Client){
                    if (((Client) wp).getFull_name().toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        _data.add(wp);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }


    static class ViewHolder {
        ImageView imageView;
        TextView name,address;
    }

}

