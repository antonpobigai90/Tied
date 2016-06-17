package com.tied.android.tiedapp.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tied.android.tiedapp.ui.fragments.profile.ProfilePagerFragment;

/**
 * Created by Emmanuel on 6/17/2016.
 */
public class ProfilePagerAdapter extends FragmentPagerAdapter {
    Context ctxt=null;

    public ProfilePagerAdapter(Context ctxt, FragmentManager mgr) {
        super(mgr);
        this.ctxt=ctxt;
    }

    @Override
    public int getCount() {
        return(3);
    }

    @Override
    public Fragment getItem(int position) {
        return(ProfilePagerFragment.newInstance(position));
    }
}