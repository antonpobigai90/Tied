package com.tied.android.tiedapp.ui.fragments.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tied.android.tiedapp.R;

/**
 * Created by hitendra on 9/9/2016.
 */
public class ChangePasswordFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_change_password, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        TextView tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.change_password));
    }
}
