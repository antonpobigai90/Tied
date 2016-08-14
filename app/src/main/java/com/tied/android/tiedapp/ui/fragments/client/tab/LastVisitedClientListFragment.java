package com.tied.android.tiedapp.ui.fragments.client.tab;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.tied.android.tiedapp.ui.adapters.ClientDistantAdapter;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class LastVisitedClientListFragment extends ClientList{

    public static final String TAG = LastVisitedClientListFragment.class
            .getSimpleName();


    public void initComponent(View view) {
        super.initComponent(view);

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                String searchData = search.getText().toString().trim().toLowerCase();
                if(adapter != null){
                    ((ClientDistantAdapter)adapter).filter(searchData);
                }
            }
        });
    }

}
