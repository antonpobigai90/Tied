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
import com.tied.android.tiedapp.customs.model.IndustryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZuumaPC on 7/14/2016.
 */
public class ClientSelectIndustryAdapter extends BaseAdapter {

    private static class ViewHolder {
        TextView txt_industry;
        ImageView img_industry;
    }

    public static final String TAG = ClientSelectIndustryAdapter.class
            .getSimpleName();

    public List<IndustryModel> _data;
    private ArrayList<IndustryModel> arraylist;
    Context _c;
    ViewHolder viewHolder;

    public ClientSelectIndustryAdapter(List<IndustryModel> industry_list, Context context) {
        _data = industry_list;
        _c = context;
        this.arraylist = new ArrayList<IndustryModel>();
        this.arraylist.addAll(_data);
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.dialog_client_select_indsutry_list_item, viewGroup,false);
        } else {
            view = convertView;
        }
        viewHolder = new ViewHolder();

        viewHolder.txt_industry = (TextView) view.findViewById(R.id.txt_industry);
        viewHolder.img_industry = (ImageView) view.findViewById(R.id.img_industry);

        final IndustryModel data = (IndustryModel) _data.get(i);

        viewHolder.txt_industry.setText(data.getName());
        if (data.isCheck_status()) {
            viewHolder.img_industry.setBackgroundResource(R.mipmap.circle_check2);
        } else {
            viewHolder.img_industry.setBackgroundResource(R.mipmap.circle_uncheck);
        }

        view.setTag(data);
        return view;
    }

    public void setSelectedIndex(int index){
        for(int i = 0; i< arraylist.size(); i++){
            if(i != index){
                _data.get(i).setCheck_status(false);
            }
            else{
                _data.get(i).setCheck_status(true);
            }
        }
        notifyDataSetChanged();
    }


}
