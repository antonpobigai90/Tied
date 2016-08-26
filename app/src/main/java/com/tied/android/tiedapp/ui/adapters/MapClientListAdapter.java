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
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.util.RoundImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 8/20/2016.
 */
public class MapClientListAdapter extends BaseAdapter implements ListAdapterListener {
    public static final String TAG = "MapClientListAdapter";

    public List _data;
    Context _c;
    ViewHolder v;
    RoundImage roundedImage;

    public MapClientListAdapter(List<Client> clients, Context context) {
        _data = clients;
        _c = context;
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
            view = li.inflate(R.layout.map_client_list_item, viewGroup, false);

            Client data = (Client) _data.get(i);

            v.name = (TextView) view.findViewById(R.id.name);
            v.address = (TextView) view.findViewById(R.id.address);
            v.pic = (ImageView) view.findViewById(R.id.pic);

            v.name.setText(data.getFull_name());
            v.address.setText(data.getAddress().getStreet());
            MyUtils.Picasso.displayImage(data.getLogo(), v.pic);

            view.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();;
        }

        return view;
    }

    @Override
    public void listInit(ArrayList arrayList) {

    }


    static class ViewHolder {
        ImageView pic;
        TextView name,address;
    }

    public List getList(){
        return _data;
    }

}

