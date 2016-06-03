package com.tied.android.tiedapp.ui.listeners;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

/**
 * Created by Emmanuel on 6/3/2016.
 */
public class EmailTextListener implements TextWatcher {

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String email;
    private Context context;
    public EmailTextListener(Context context, String email) {
        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (email.matches(emailPattern) && s.length() > 0)
        {
            Toast.makeText(context,"valid email address",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context,"Invalid email address",Toast.LENGTH_SHORT).show();
        }
    }
}