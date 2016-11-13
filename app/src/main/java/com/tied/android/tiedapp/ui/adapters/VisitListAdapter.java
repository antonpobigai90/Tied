package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Visit;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.ui.activities.client.ClientMapAndListActivity;
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.util.RoundImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 8/20/2016.
 */
public class VisitListAdapter extends BaseAdapter {
    public static final String TAG = "VisitListAdapter";

    public List _visits;
    public List _clients;
    Context _c;
    ViewHolder v;

    public VisitListAdapter(List<Visit> visits, List<Client> clients, Context context) {
        _visits = visits;
        _clients = clients;
        _c = context;
    }

    @Override
    public int getCount() {
        return _visits.size();
    }

    @Override
    public Object getItem(int i) {
        return _visits.get(i);
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
            view = li.inflate(R.layout.visits_item_list, viewGroup, false);
        } else {
            v = (ViewHolder) convertView.getTag();;
        }

        Visit visit = (Visit) _visits.get(i);

        v.name = (TextView) view.findViewById(R.id.name);
        v.address = (TextView) view.findViewById(R.id.address);
        v.distance = (TextView)view.findViewById(R.id.distance);
        v.pic = (ImageView) view.findViewById(R.id.pic);
        v.time=(TextView) view.findViewById(R.id.time);
        v.day=(TextView) view.findViewById(R.id.day);
        v.month = (TextView)view.findViewById(R.id.month);

        if (_clients != null) {
            Client client = (Client) _clients.get(i);

            v.name.setText(MyUtils.getClientName(client));
            MyUtils.Picasso.displayImage(client.getLogo(), v.pic);

            v.address.setText(visit.getAddress().getStreet());
        } else {
            v.name.setText(visit.getTitle());
            v.pic.setVisibility(View.GONE);

            v.address.setText(visit.getAddress().getStreet() + ", " + visit.getAddress().getCity() + ", " + visit.getAddress().getState() + ", " + visit.getAddress().getZip());
        }

        v.distance.setText(MyUtils.getDistance(MyUtils.getCurrentLocation(), visit.getAddress().getCoordinate(), false)+"m");

        String[] date = visit.getVisit_date().split("-");
        v.day.setText(date[2]);
        v.month.setText(MyUtils.MONTHS_LIST[Integer.valueOf(date[1]).intValue() - 1]);

        v.time.setText(visit.getVisit_time());

        view.setTag(v);

        return view;
    }

    static class ViewHolder {
        ImageView pic;
        TextView name,address, distance, time, day, month;
    }

    public List getList(){
        return _visits;
    }

}

