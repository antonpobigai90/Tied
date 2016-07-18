package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Distance;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.util.RoundImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Emmanuel on 6/28/2016.
 */
public class ClientAdapter extends BaseAdapter {
    public static final String TAG = ClientAdapter.class
            .getSimpleName();

    public List _data;
    private ArrayList arraylist;
    Context _c;
    ViewHolder v;
    RoundImage roundedImage;

    public ClientAdapter(ArrayList clients, Context context) {
        _data = clients;
        _c = context;
        this.arraylist = clients;
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

    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (_data.get(i) instanceof Client) {
            final Client data = (Client) _data.get(i);
            view = li.inflate(R.layout.schedule_select_client_list_item, viewGroup,false);
            final ImageView imageView = (ImageView) view.findViewById(R.id.pic);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView address = (TextView) view.findViewById(R.id.address);

            name.setText(data.getFull_name());

            address.setText(data.getAddress().getLocationAddress());

            String logo = data.getLogo().equals("") ? null  : data.getLogo();
            Picasso.with(_c).
                    load(logo)
                    .into(new Target() {
                        @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            if (bitmap != null){
                                imageView.setImageBitmap(bitmap);
                            }else{
                                imageView.setImageResource(R.mipmap.default_avatar);
                            }
                        }
                        @Override public void onBitmapFailed(Drawable errorDrawable) { }
                        @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
                    });

        } else {
            final Distance distance = (Distance) _data.get(i);
            view = li.inflate(R.layout.schedule_select_client_text_distance, viewGroup,false);
            TextView range = (TextView) view.findViewById(R.id.txt_distance);
            String range_displayed = distance.getLower_bound()+" - "+distance.getUpper_bound()+" "+distance.getMeasurement();
            range.setText(range_displayed);
        }
        return view;
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public View getView(int i, View convertView, ViewGroup viewGroup) {
//        View view = convertView;
//        if (view == null) {
//            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = li.inflate(R.layout.schedule_select_client_list_item, viewGroup,false);
//        } else {
//            view = convertView;
//        }
//        v = new ViewHolder();
//        v.name = (TextView) view.findViewById(R.id.name);
//        v.address = (TextView) view.findViewById(R.id.address);
//        v.imageView = (ImageView) view.findViewById(R.id.pic);
//
//        final Client data = (Client) _data.get(i);
//        v.name.setText(data.getFull_name());
//
//        v.address.setText(data.getAddress().getLocationAddress());
//
//        String logo = data.getLogo().equals("") ? null  : data.getLogo();
//        Picasso.with(_c).
//                load(logo)
//                .into(new Target() {
//                    @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        if (bitmap != null){
//                            v.imageView.setImageBitmap(bitmap);
//                        }else{
//                            v.imageView.setImageResource(R.mipmap.default_avatar);
//                        }
//                    }
//                    @Override public void onBitmapFailed(Drawable errorDrawable) { }
//                    @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
//                });
//        view.setTag(data);
//        return view;
//    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        _data.clear();
        if (charText.length() == 0) {
            _data.addAll(arraylist);
        } else {
            for (Object wp : arraylist) {
                if(wp instanceof Client){
                    if (((Client) wp).getFull_name().toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        _data.add(wp);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
    static class ViewHolder {
        ImageView imageView;
        TextView name,address;
    }

}

