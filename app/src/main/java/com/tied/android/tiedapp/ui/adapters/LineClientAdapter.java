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
import com.tied.android.tiedapp.objects.Client;
import com.tied.android.tiedapp.util.RoundImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ZuumaPC on 7/7/2016.
 */
public class LineClientAdapter extends BaseAdapter {
    public static final String TAG = ClientScheduleAdapter.class
            .getSimpleName();

    public List<Client> _data;
    private ArrayList<Client> arraylist;
    Context _c;
    ViewHolder v;
    RoundImage roundedImage;

    public LineClientAdapter(List<Client> clients, Context context) {
        _data = clients;
        _c = context;
        this.arraylist = new ArrayList<Client>();
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
        View view = convertView;
        if (view == null) {
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.line_clients_list_item, viewGroup,false);
        } else {
            view = convertView;
        }

        v = new ViewHolder();
        v.name = (TextView) view.findViewById(R.id.name);
        v.imageView = (ImageView) view.findViewById(R.id.pic);

        final Client data = (Client) _data.get(i);
        v.name.setText(data.getFull_name());

        view.setTag(data);
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        _data.clear();
        if (charText.length() == 0) {
            _data.addAll(arraylist);
        } else {
            for (Client wp : arraylist) {
                if (wp.getFull_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    _data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView imageView;
        TextView name;
    }

}
