package com.tied.android.tiedapp.ui.fragments.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.util.DemoData;

/**
 * Created by Emmanuel on 6/17/2016.
 */
public class ProfilePagerFragment extends Fragment {
    private static final String KEY_POSITION = "position" ;

    public static ProfilePagerFragment newInstance(int position) {
        ProfilePagerFragment frag = new ProfilePagerFragment();
        Bundle args=new Bundle();

        args.putInt(KEY_POSITION, position);
        frag.setArguments(args);

        return(frag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_item_cover, container, false);
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.profile_cover);
        getActivity().getLayoutInflater().inflate(DemoData.profile_layout[getArguments().getInt(KEY_POSITION)], layout);
        return view;
    }
}