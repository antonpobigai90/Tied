package com.tied.android.tiedapp.ui.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import com.tied.android.tiedapp.util.RoundImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 9/9/2016.
 */
public class CoWorkerHAdapter extends RecyclerView.Adapter<CoWorkerHAdapter.ViewHolder> implements ListAdapterListener {

    private Activity activity;
    public List<Client> _data;
    private ArrayList<Client> arraylist;

    RoundImage roundedImage;

    public CoWorkerHAdapter(List<Client> clients, Activity activity) {
        this.activity = activity;
        _data = clients;
        this.arraylist = new ArrayList<Client>();
        this.arraylist.addAll(_data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.coworker_h_item, viewGroup, false);
        return new ViewHolder(view);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final CoWorkerHAdapter.ViewHolder viewHolder, final int position) {

        final Client data = (Client) _data.get(position);
        String logo = data.getLogo().equals("") ? null  : data.getLogo();
        MyUtils.Picasso.displayImage(logo ,viewHolder.pic);
        MyListener myListener = new MyListener(data);
        viewHolder.pic.setOnClickListener(myListener);

    }

    @Override
    public int getItemCount() {
        return (null != _data ? _data.size() : 0);
    }

    @Override
    public void listInit(ArrayList arrayList) {
        this._data = arrayList;
        notifyDataSetChanged();
    }

    private class MyListener implements View.OnClickListener{
        private Client client;
        public MyListener(Client client){
            this.client = client;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.menu_icon:

                    break;
            }
        }
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView pic;
        public ViewHolder(View view) {
            super(view);
            pic = (ImageView) view.findViewById(R.id.pic);
        }
    }
}
