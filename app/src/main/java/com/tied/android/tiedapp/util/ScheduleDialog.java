package com.tied.android.tiedapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.client.Client;

/**
 * Created by Emmanuel on 6/21/2016.
 */
public class ScheduleDialog {

    RelativeLayout cancel;
    private TextView name, address, distance, phone;
    private ImageView pic;

//    public void showDialog(Activity activity, String msg){
    public void showDialog(Client client, Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.schedule_dialog);

        name = (TextView) dialog.findViewById(R.id.name);
        address = (TextView) dialog.findViewById(R.id.address);
        distance = (TextView) dialog.findViewById(R.id.distance);
        phone = (TextView) dialog.findViewById(R.id.phone);
        pic = (ImageView) dialog.findViewById(R.id.pic);

        name.setText(client.getCompany());
        address.setText(client.getAddress().getStreet());
        distance.setText("0.5 miles");
        phone.setText(client.getPhone());

        Picasso.with(activity).
                load(client.getLogo())
                .into(new Target() {
                    @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        if (bitmap != null){
                            pic.setImageBitmap(bitmap);
                        }else{
                            pic.setImageResource(R.mipmap.default_avatar);
                        }
                    }
                    @Override public void onBitmapFailed(Drawable errorDrawable) { }
                    @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
                });

        cancel = (RelativeLayout) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
