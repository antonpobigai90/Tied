package com.tied.android.tiedapp.ui.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.ui.activities.lines.LineAddTerritoryActivity;

import java.util.List;

/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class ClientTerritoriesAdapter extends BaseAdapter {

    private static class ViewHolder {
        TextView txt_territoy_name,txt_territory_client;
        ImageView img_check;
    }

    public static final String TAG = ClientTerritoriesAdapter.class
            .getSimpleName();

    public List<TerritoryModel> _data;
    Context _c;
    ViewHolder viewHolder;
    int _page_index;

    public ClientTerritoriesAdapter(int page_index, List<TerritoryModel> territory_list, Context context) {
        _data = territory_list;
        _c = context;
        _page_index = page_index;
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int position) {
        return _data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.client_territories_list_item, viewGroup,false);
        } else {
            view = convertView;
        }
        viewHolder = new ViewHolder();

        viewHolder.txt_territoy_name = (TextView) view.findViewById(R.id.territory);
        viewHolder.txt_territory_client = (TextView) view.findViewById(R.id.no_clients);
        viewHolder.img_check = (ImageView) view.findViewById(R.id.selector);

        if (_page_index == 0) {
            viewHolder.img_check.setVisibility(View.GONE);
        } else {
            viewHolder.txt_territory_client.setVisibility(View.GONE);
        }

        final TerritoryModel data = (TerritoryModel) _data.get(i);

        viewHolder.txt_territoy_name.setText(data.getTerritory_name());
        String sales = data.getNo_clients() + " Clients";
        viewHolder.txt_territory_client.setText(sales);
        if (data.isCheck_status()) {
            viewHolder.img_check.setBackgroundResource(R.drawable.circle_check2);
            viewHolder.txt_territoy_name.setTextColor(_c.getResources().getColor(R.color.light_gray2));
        } else {
            viewHolder.img_check.setBackgroundResource(R.drawable.unselectd_bg);
            viewHolder.txt_territoy_name.setTextColor(_c.getResources().getColor(R.color.grey));
        }

        view.setTag(data);
        return view;
    }
}
