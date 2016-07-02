package com.tied.android.tiedapp.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.ScheduleDataModel;
import com.tied.android.tiedapp.customs.model.ScheduleTimeModel;
import com.tied.android.tiedapp.util.RoundImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ScheduleListAdapter extends BaseAdapter {
    public static final String TAG = ScheduleListAdapter.class
            .getSimpleName();

    public List<ScheduleDataModel> _data;
    private ArrayList<ScheduleDataModel> arraylist;
    Context _c;
    ViewHolder v;
    RoundImage roundedImage;

    public ScheduleListAdapter(List<ScheduleDataModel> schedules, Context context) {
        _data = schedules;
        _c = context;
        this.arraylist = new ArrayList<ScheduleDataModel>();
        this.arraylist.addAll(_data);
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ScheduleDataModel data = (ScheduleDataModel) _data.get(i);
        Log.d(TAG, data.toString());
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.schedule_timeline_list_item, viewGroup, false);

        } else {
            view = convertView;
        }

        v = new ViewHolder();
        v.day = (TextView) view.findViewById(R.id.day);
        v.week_day = (TextView) view.findViewById(R.id.week_day);
        v.temperature = (TextView) view.findViewById(R.id.temperature);
        v.weather = (TextView) view.findViewById(R.id.weather);

        v.day.setText(data.getDay());
        v.week_day.setText(data.getWeek_day());
        v.temperature.setText(data.getTemperature());
        v.weather.setText(data.getWeather());

        v.timeLine = (LinearLayout) view.findViewById(R.id.timeline);

        ArrayList<ScheduleTimeModel> scheduleTimeModels = data.getScheduleTimeModel();
        for (ScheduleTimeModel schedule : scheduleTimeModels) {
            View schedule_view = LayoutInflater.from(_c).inflate(R.layout.schedule_list_item, null);
            LinearLayout linearLayout = (LinearLayout) schedule_view.findViewById(R.id.schedule);
            TextView time = (TextView) linearLayout.findViewById(R.id.time);
            TextView message = (TextView) linearLayout.findViewById(R.id.message);
            time.setText(schedule.getTime());
            message.setText(schedule.getTitle());
            v.timeLine.addView(linearLayout);
        }

        view.setTag(data);
        return view;
    }


    static class ViewHolder {
        TextView day, week_day, temperature, weather;
        LinearLayout timeLine;
    }

}
