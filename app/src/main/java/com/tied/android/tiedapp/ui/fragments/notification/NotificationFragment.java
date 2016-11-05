package com.tied.android.tiedapp.ui.fragments.notification;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.NotificationDataModel;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.adapters.ExpendableNotificationListAdapter;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Femi on 7/30/2016.
 */
public class NotificationFragment extends Fragment {
    private static final String TAG = NotificationFragment.class.getSimpleName();
    private Bundle bundle;
    private User user;

    ExpandableListView expandableListView;
    ImageView img_close;

    ExpendableNotificationListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, ArrayList<NotificationDataModel>> listDataChild;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new NotificationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.blue_status_bar));
        }

        bundle = getArguments();
        user = MyUtils.getUserFromBundle(bundle);

        initComponent(view);
        return view;
    }

    private void initComponent(View view) {

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        prepareListData();

        listAdapter = new ExpendableNotificationListAdapter(getActivity(), listDataHeader, listDataChild);
        expandableListView.setAdapter(listAdapter);

        expandableListView.expandGroup(0);
        expandableListView.expandGroup(1);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, ArrayList<NotificationDataModel>>();

        // Adding child data
        listDataHeader.add("JANUARY 10TH");
        listDataHeader.add("JANUARY 9TH");

        ArrayList<NotificationDataModel> notificationDataModels = new ArrayList<NotificationDataModel>();

        for (int i = 0 ; i < 4 ; i++) {
            NotificationDataModel notificationDataModel = new NotificationDataModel();

            notificationDataModel.setTxt_time("3:57 PM");
            if (i == 0) {   //notification status
                notificationDataModel.setTxt_first("Added");
                notificationDataModel.setTxt_end("as a client");
                notificationDataModel.setbNew(true);
            } else if (i == 1) {
                notificationDataModel.setTxt_first("Completed new a new");
                notificationDataModel.setTxt_end("Email");
                notificationDataModel.setbNew(true);
            } else if (i == 2) {
                notificationDataModel.setTxt_first("Visited");
                notificationDataModel.setTxt_end("site");
                notificationDataModel.setbNew(false);
            } else if (i == 3) {
                notificationDataModel.setTxt_first("New invitation received from");
                notificationDataModel.setTxt_end("");
                notificationDataModel.setbNew(false);
            }

            notificationDataModel.setTxt_user("Emmanuel Iroko");

            notificationDataModels.add(notificationDataModel);
        }

        listDataChild.put(listDataHeader.get(0), notificationDataModels); // Header, Child data
        listDataChild.put(listDataHeader.get(1), notificationDataModels); // Header, Child data

    }

}
