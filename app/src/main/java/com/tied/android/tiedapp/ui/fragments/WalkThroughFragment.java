package com.tied.android.tiedapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.ui.activities.signups.SignInActivity;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;
import com.tied.android.tiedapp.util.DemoData;
import com.tied.lib.coverflow.CoverFlow;
import com.tied.lib.coverflow.core.PagerContainer;

import me.relex.circleindicator.CircleIndicator;

/**
 * A placeholder fragment containing a simple view.
 */
public class WalkThroughFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = WalkThroughFragment.class
            .getSimpleName();

    private SignUpFragmentListener mListener;

    private TextView register, sign_in;

    public WalkThroughFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignUpFragmentListener) {
            mListener = (SignUpFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action, Bundle bundle) {
        if (mListener != null) {
            mListener.onFragmentInteraction(action,bundle);
        }
    }

    public void initComponent(View view){

        register = (TextView) view.findViewById(R.id.register);
        sign_in = (TextView) view.findViewById(R.id.sign_in);

        register.setOnClickListener(this);
        sign_in.setOnClickListener(this);

        PagerContainer container = (PagerContainer) view.findViewById(R.id.pager_container);
        ViewPager pager = container.getViewPager();
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);

        pager.setAdapter(new MyPagerAdapter());
        indicator.setViewPager(pager);

        pager.setClipChildren(false);
        //
        pager.setOffscreenPageLimit(15);

        new CoverFlow.Builder()
                .with(pager)
                .scale(0.3f)
                .pagerMargin(getResources().getDimensionPixelSize(R.dimen.pager_margin))
                .spaceSize(0f)
                .build();
    }


    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_cover,null);

            String[] str_walkthrough = getResources().getStringArray(R.array.str_walkthrough);

            ImageView imageView = (ImageView) view.findViewById(R.id.image_cover);
            imageView.setImageDrawable(getResources().getDrawable(DemoData.covers[position]));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            container.addView(view);

            TextView txt_walthrough = (TextView) view.findViewById(R.id.txt_walkthrough);
            txt_walthrough.setText(str_walkthrough[position]);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return DemoData.covers.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.register:
                startActivity(intent);
                break;
            case R.id.sign_in:
                intent = new Intent(getActivity(), SignInActivity.class);
                break;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
