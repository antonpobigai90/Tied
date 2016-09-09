package com.tied.android.tiedapp.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Goal;
import com.tied.android.tiedapp.ui.activities.lines.LineGoalActivity;
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZuumaPC on 9/3/2016.
 */
public class GoalsAdapter extends BaseAdapter implements ListAdapterListener {

    private static class ViewHolder {
        TextView txt_title,txt_date;
    }

    public static final String TAG = GoalsAdapter.class
            .getSimpleName();

    public List<Goal> _data;
    Context _c;
    ViewHolder viewHolder;

    public GoalsAdapter(List<Goal> line_list, Context context) {
        _data = line_list;
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
            view = li.inflate(R.layout.line_goal_list_item, viewGroup,false);
        } else {
            view = convertView;
        }
        viewHolder = new ViewHolder();

        viewHolder.txt_title = (TextView) view.findViewById(R.id.title);
        viewHolder.txt_date = (TextView) view.findViewById(R.id.date);

        final Goal data = (Goal) _data.get(i);

        viewHolder.txt_title.setText(data.getTitle());
        String date = "Ends" + data.getDate();
        viewHolder.txt_date.setText(date);
        view.setTag(data);
        return view;
    }

    @Override
    public void listInit(ArrayList arrayList) {
        this._data = arrayList;
        android.support.v4.view.ViewPager mViewPager = ((LineGoalActivity) _c).mViewPager;
        notifyDataSetChanged();
        mViewPager.getAdapter().notifyDataSetChanged();
    }
}
