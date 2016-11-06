package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.ClientSaleDataModel;
import com.tied.android.tiedapp.customs.model.NotificationDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class ExpendableNotificationListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<NotificationDataModel>> _listDataChild;

    public ExpendableNotificationListAdapter(Context context, List<String> listDataHeader,
                                             HashMap<String, ArrayList<NotificationDataModel>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        NotificationDataModel model = (NotificationDataModel) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.notification_child_item, null);
        }

        TextView txt_title = (TextView) convertView.findViewById(R.id.txt_time);
        TextView txt_first = (TextView) convertView.findViewById(R.id.txt_first);
        TextView txt_end = (TextView) convertView.findViewById(R.id.txt_end);
        TextView txt_user = (TextView) convertView.findViewById(R.id.txt_user);
        TextView txt_new = (TextView) convertView.findViewById(R.id.txt_new);

        txt_title.setText(model.getTxt_time());
        txt_first.setText(model.getTxt_first());
        txt_end.setText(model.getTxt_end());
        txt_user.setText(model.getTxt_user());

        if (model.isbNew()) {
            txt_new.setVisibility(View.VISIBLE);
        } else {
            txt_new.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.notification_group_item, null);
        }

        TextView txt_date = (TextView) convertView.findViewById(R.id.txt_date);
        txt_date.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
