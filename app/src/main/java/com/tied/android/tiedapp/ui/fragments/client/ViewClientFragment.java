package com.tied.android.tiedapp.ui.fragments.client;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.DialogClientOptions;
import com.tied.android.tiedapp.util.DialogYesNo;


public class ViewClientFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = ViewClientFragment.class
            .getSimpleName();

    public ImageView avatar;
    private LinearLayout icon_plus, icon_call;
    private TextView btn_delete;

    private Bundle bundle;
    private User user;

    private Client client;

    FragmentIterationListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_client, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    private void initComponent(View view) {
        btn_delete = (TextView) view.findViewById(R.id.btn_delete);
        icon_plus = (LinearLayout) view.findViewById(R.id.icon_plus);
        icon_call = (LinearLayout) view.findViewById(R.id.icon_call);

        avatar = (ImageView) view.findViewById(R.id.avatar);

        bundle = getArguments();

        btn_delete.setOnClickListener(this);
        icon_plus.setOnClickListener(this);
        icon_call.setOnClickListener(this);

        if (bundle != null) {
            Log.d(TAG, "bundle not null");
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            String client_json = bundle.getString(Constants.CLIENT_DATA);
            user = gson.fromJson(user_json, User.class);
            client = gson.fromJson(client_json, Client.class);

            String logo = client.getLogo().equals("") ? null  : client.getLogo();
            Picasso.with(getActivity()).
                    load(logo)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            if (bitmap != null) {
                                avatar.setImageBitmap(bitmap);
                            } else {
                                avatar.setImageResource(R.mipmap.default_avatar);
                            }
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });
        }
    }


    @Override
    public void onClick(View v) {
        int color = this.getResources().getColor(R.color.schedule_title_bg_color);
        switch (v.getId()){
            case R.id.btn_delete:
                color = this.getResources().getColor(R.color.alert_bg_color);
                DialogYesNo alert_delete = new DialogYesNo(getActivity(),"DELETE CLIENT","Are you sure want to delete this client","YES DELETE!",color,0);
                alert_delete.showDialog();
                break;
            case R.id.icon_plus:
                DialogClientOptions alert_client = new DialogClientOptions(client,getActivity(),bundle);
                alert_client.showDialog();
                break;
            case R.id.icon_call:
                color = this.getResources().getColor(R.color.green_color);
                DialogYesNo alert_call = new DialogYesNo(getActivity(),"CALL CLIENT","Are you sure want to call this client?. Call charges may apply","YES, CALL!",color,1);
                alert_call.showDialog();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentIterationListener) {
            mListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(Constants.CreateSchedule, bundle);
        }
    }
}
