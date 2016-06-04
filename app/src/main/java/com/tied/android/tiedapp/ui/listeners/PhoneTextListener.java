package com.tied.android.tiedapp.ui.listeners;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Emmanuel on 6/3/2016.
 */
public class PhoneTextListener implements TextWatcher {

    private String phone;
    private Context context;

    public PhoneTextListener(Context context, String phone) {
        this.context = context;
        this.phone = phone;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if(s.length() == 3){
            phone = s +" ";
            s = s.insert(4, phone);
        }else if(s.length() == 7){
            phone = s +" ";
            s = s.insert(8, phone);
        }else if(s.length() == 12){
            phone = s +"";
        }

//        if (email.matches(emailPattern) && s.length() > 0)
//        {
//            Toast.makeText(context,"valid email address",Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Toast.makeText(context,"Invalid email address",Toast.LENGTH_SHORT).show();
//        }
    }
}
