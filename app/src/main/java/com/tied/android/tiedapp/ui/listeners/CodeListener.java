package com.tied.android.tiedapp.ui.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.tied.android.tiedapp.ui.fragments.signups.VerifyPhoneFragment;

/**
 * Created by Emmanuel on 6/4/2016.
 */
public class CodeListener implements TextWatcher {

    private VerifyPhoneFragment fragment;
    public CodeListener(VerifyPhoneFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length() == 5){
            fragment.progressBar.setVisibility(View.VISIBLE);
            fragment.continue_action();
        }
    }
}
