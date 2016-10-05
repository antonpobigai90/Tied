package com.tied.android.tiedapp.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.lines.AddLinesActivity;
import com.tied.android.tiedapp.ui.activities.lines.ViewLineActivity;
import com.tied.android.tiedapp.ui.activities.lines.ViewNewLineActivity;
import com.tied.android.tiedapp.ui.adapters.LinesAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import junit.framework.Test;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ZuumaPC on 8/18/2016.
 */
public class LinesFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String TAG = LinesFragment.class
            .getSimpleName();

    protected User user;
    protected Bundle bundle;
    protected ListView listView;
    List<Line> lines =new ArrayList<>();

    protected LinesAdapter adapter;
    ImageView img_plus;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new LinesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lines, container, false);



        initComponent(view);
        return view;
    }

    public void initComponent(View view){
        bundle = getArguments();
        user = MyUtils.getUserFromBundle(bundle);
        MyUtils.setColorTheme(getActivity(), bundle.getInt(Constants.SOURCE), view.findViewById(R.id.main_layout));

        img_plus = (ImageView) view.findViewById(R.id.img_plus);

        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  MyUtils.showNewLineDialog(getActivity(), "Create New Line", new MyUtils.MyDialogClickListener() {
                    @Override
                    public void onClick(Object response) {

                    }
                });*/
                MyUtils.startActivity(getActivity(), AddLinesActivity.class, bundle);
            }
        });

        listView = (ListView) view.findViewById(R.id.lines_listview);
        if(!MyUtils.currentUserIs(user)) {
            img_plus.setVisibility(View.GONE);
        }


        adapter = new LinesAdapter(lines, getActivity(), bundle);
        listView.setAdapter(adapter);
        Logger.write(user.toString());
        initLines(getActivity(), user, adapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int
            position, long id) {
        Log.d(TAG, "here---------------- listener");
        Line line = lines.get(position);
        bundle.putSerializable(Constants.LINE_DATA, line);
        MyUtils.startActivity(getActivity(), ViewLineActivity.class, bundle);
    }
    public void initLines(final Context context, User user, final ListAdapterListener listAdapterListener){
        final LineApi lineApi =  MainApplication.createService(LineApi.class);
        Call<ResponseBody> response = lineApi.getUserLines(user.getId(), 1);
        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    Logger.write(response.toString());
                    if (response.isAuthFailed()) {
                        User.LogOut(context);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code() == 200) {

                         lines .addAll( (ArrayList) response.getDataAsList(Constants.LINES_lIST, Line.class));
                        Logger.write("Lines loadeddddddddddddddddddddddddddddddddddddddddddddddd "+lines.size());
                         adapter.notifyDataSetChanged();
                        if(lines.isEmpty()) MyUtils.showNoResults(getView(), R.id.no_results);
                    }else{
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                }catch (IOException ioe) {
                    Logger.write(ioe);
                }
                catch (Exception jo) {
                    Logger.write(jo);
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(" onFailure", t.toString());
            }
        });
    }
}
