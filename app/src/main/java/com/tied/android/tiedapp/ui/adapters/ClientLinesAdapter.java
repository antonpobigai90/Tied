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
import com.tied.android.tiedapp.objects.Line;

import java.util.List;

/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class ClientLinesAdapter extends BaseAdapter {

    private static class ViewHolder {
        TextView txt_line_name,txt_line_sales;
        ImageView img_check;
    }

    public static final String TAG = ClientLinesAdapter.class
            .getSimpleName();

    public List<Line> _data;
    Context _c;
    ViewHolder viewHolder;

    public ClientLinesAdapter(List<Line> line_list, Context context) {
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
            view = li.inflate(R.layout.client_lines_list_item, viewGroup,false);
        } else {
            view = convertView;
        }
        viewHolder = new ViewHolder();

        viewHolder.txt_line_name = (TextView) view.findViewById(R.id.line);
        viewHolder.txt_line_sales = (TextView) view.findViewById(R.id.sales);
        viewHolder.img_check = (ImageView) view.findViewById(R.id.selector);


        final Line data = (Line) _data.get(i);

        viewHolder.txt_line_name.setText(data.getName());
        String sales = data.getSales() + "Total sales";
        viewHolder.txt_line_sales.setText(sales);
        if (data.isCheck_status()) {
            viewHolder.img_check.setBackgroundResource(R.drawable.circle_check2);
        } else {
            viewHolder.img_check.setBackgroundResource(R.drawable.unselectd_bg);
        }

        viewHolder.img_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedIndex(i);
            }
        });

        view.setTag(data);
        return view;
    }

    public void setSelectedIndex(int index){
        for(int i = 0; i< _data.size(); i++){
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
