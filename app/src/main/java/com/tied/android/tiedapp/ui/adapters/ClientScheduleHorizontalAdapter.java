package com.tied.android.tiedapp.ui.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Client;
import com.tied.android.tiedapp.ui.activities.schedule.CreateApointmentActivity;
import com.tied.android.tiedapp.util.RoundImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 6/29/2016.
 */
public class ClientScheduleHorizontalAdapter extends RecyclerView.Adapter<ClientScheduleHorizontalAdapter.ViewHolder> {

    private Activity activity;
    public List<Client> _data;
    private ArrayList<Client> arraylist;

    RoundImage roundedImage;

    public ClientScheduleHorizontalAdapter(List<Client> clients,Activity activity) {
        this.activity = activity;
        _data = clients;
        this.arraylist = new ArrayList<Client>();
        this.arraylist.addAll(_data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.schedule_clients_cardview_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ClientScheduleHorizontalAdapter.ViewHolder viewHolder, final int position) {

        final Client data = (Client) _data.get(position);
        viewHolder.name.setText(data.getFull_name());

        Picasso.with(activity).
                load(data.getLogo())
                .into(viewHolder.pic);

        viewHolder.schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Position clicked: " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, CreateApointmentActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != _data ? _data.size() : 0);
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView menue_icon;
        private ImageView pic;
        private TextView name;
        private TextView schedule;

        public ViewHolder(View view) {
            super(view);
            menue_icon = (ImageView) view.findViewById(R.id.menu_icon);
            pic = (ImageView) view.findViewById(R.id.pic);
            name = (TextView) view.findViewById(R.id.name);
            schedule = (TextView) view.findViewById(R.id.schedule);
        }
    }
}
