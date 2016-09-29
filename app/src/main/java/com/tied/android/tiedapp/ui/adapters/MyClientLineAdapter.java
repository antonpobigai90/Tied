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
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.util.RoundImage;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Femi on 7/22/2016.
 */
public class MyClientLineAdapter extends BaseAdapter {
    public static final String TAG = ClientScheduleAdapter.class
            .getSimpleName();

    public ArrayList<Client> _data;
    Context _c;
    ViewHolder viewHolder;

    public MyClientLineAdapter(ArrayList<Client> data, Context context) {
        _data = data;
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            viewHolder=new ViewHolder();
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.line_clients_list_item, viewGroup,false);
            viewHolder.roundedImage=(CircleImageView)view.findViewById(R.id.pic);
            viewHolder.name=(TextView)view.findViewById(R.id.name);
            viewHolder.description=(TextView)view.findViewById(R.id.description);
            viewHolder.selector=(ImageView)view.findViewById(R.id.selector);

            view.setTag(viewHolder);
        } else {
           viewHolder= (ViewHolder) convertView.getTag();
        }

        if( _data.get(i) instanceof Client){
            final Client client = (Client) _data.get(i);
            viewHolder.name.setText(client.getFull_name());
            MyUtils.Picasso.displayImage(client.getLogo(), viewHolder.roundedImage);
            viewHolder.description.setText(client.getAddress().getCity()+", "+client.getAddress().getState());

            if (client.getCheckStatus()) {
                viewHolder.selector.setBackgroundResource(R.drawable.circle_check2);
            } else {
                viewHolder.selector.setBackgroundResource(R.drawable.unselectd_bg);
            }
        }
        return view;
    }

    static class ViewHolder {
        ImageView selector;
        TextView name, description;
        CircleImageView roundedImage;
    }
}
