package com.tied.android.tiedapp.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.ActivityDataModel;
import com.tied.android.tiedapp.objects.Line;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class ActivityAdapter extends BaseAdapter {

    private static class ViewHolder {
        TextView day,month, time_range, title, description;
    }

    public static final String TAG = ActivityAdapter.class
            .getSimpleName();

    public ArrayList<ActivityDataModel> _data;
    Context _c;
    ViewHolder viewHolder;

    public ActivityAdapter(ArrayList<ActivityDataModel> activity_list, Context context) {
        _data = activity_list;
        _c = context;
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
            view = li.inflate(R.layout.coworker_activity_list_item, viewGroup,false);
        } else {
            view = convertView;
        }
        viewHolder = new ViewHolder();

        viewHolder.day = (TextView) view.findViewById(R.id.day);
        viewHolder.month = (TextView) view.findViewById(R.id.month);
        viewHolder.time_range = (TextView) view.findViewById(R.id.time_range);
        viewHolder.title = (TextView) view.findViewById(R.id.title);
        viewHolder.description = (TextView) view.findViewById(R.id.description);

        final ActivityDataModel data = (ActivityDataModel) _data.get(i);

        viewHolder.day.setText(data.getDay());
        viewHolder.month.setText(data.getMonth());
        viewHolder.time_range.setText(data.getTime_range());
        viewHolder.title.setText(data.getTitle());
        viewHolder.description.setText(data.getDescription());

        view.setTag(data);
        return view;
    }
}
