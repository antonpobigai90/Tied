package com.tied.android.tiedapp.ui.activities.lines;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Goal;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.GoalApi;
import com.tied.android.tiedapp.ui.dialogs.DatePickerFragment;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LineAddGoalActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LineAddGoalActivity.class
            .getSimpleName();
    private User user;
    private Bundle bundle;
    private EditText goal_name, how_much, note;
    private TextView end_date, date_selected;
    
    private String name, set_goal, dateText, noteText;

    LinearLayout back_layout, layout_date, ok_but;
    private Goal goal;
    private Line line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_add_goal);

        goal_name = (EditText) findViewById(R.id.goal_name);
        how_much = (EditText) findViewById(R.id.how_much);
        end_date = (TextView) findViewById(R.id.date);
        date_selected = (TextView) findViewById(R.id.date_selected);
        note = (EditText) findViewById(R.id.note);

        bundle = getIntent().getExtras();
        goal = (Goal) bundle.getSerializable(Constants.GOAL_DATA);
        line = (Line) bundle.getSerializable(Constants.LINE_DATA);
        if (goal != null){
            goal_name.setText(goal.getTitle());
            how_much.setText(goal.getValue());
            end_date.setText(HelperMethods.getFormatedDate(goal.getDate()));
            date_selected.setText(goal.getDate());
            note.setText(goal.getDescription());
        }
        else{
            goal = new Goal();
        }

        user = MyUtils.getUserFromBundle(bundle);

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        layout_date = (LinearLayout) findViewById(R.id.layout_date);
        ok_but = (LinearLayout) findViewById(R.id.ok_but);

        layout_date.setOnClickListener(this);
        back_layout.setOnClickListener(this);
        ok_but.setOnClickListener(this);
    }
    
    public void validate(){
        name = goal_name.getText().toString().trim();
        set_goal = how_much.getText().toString().trim();
        dateText = date_selected.getText().toString().trim();
        noteText = note.getText().toString().trim();

        if(name.isEmpty()) {
            MyUtils.showToast( "You must provide a Goal title");
            return;
        }
        if(set_goal.isEmpty()) {
            MyUtils.showToast("You must set a goal target");
            return;
        }
        if(dateText.isEmpty()) {
            MyUtils.showToast( "You must provide end date");
            return;
        }

        if(noteText.isEmpty()) {
            MyUtils.showToast( "You must provide end date");
            return;
        }

        goal.setTitle(name);
        goal.setValue(set_goal);
        goal.setDescription(noteText);
        goal.setDate(dateText);
        goal.setUser_id(user.getId());
        goal.setObject_id(line.getId());

    }

    public void createGoal(){
        validate();

        GoalApi goalApi = MainApplication.createService(GoalApi.class, user.getToken());
        DialogUtils.displayProgress(this);
        Call<ResponseBody> response = goalApi.createGoal(goal);
        Log.d(TAG, response.request().url().toString());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                try {
                    GeneralResponse response =new GeneralResponse(resResponse.body());
                    if (response.isAuthFailed()) {
                        User.LogOut(LineAddGoalActivity.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==201) {
                        DialogUtils.closeProgress();
                        Goal the_line = response.getData(Constants.GOAL_DATA, Goal.class);
                        Logger.write("the_line: "+the_line.toString());
                        bundle.putSerializable(Constants.GOAL_DATA, the_line);
                        MainApplication.goals.clear();
                        MyUtils.startActivity(LineAddGoalActivity.this, LineGoalActivity.class, bundle);
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
            public void onFailure(Call<ResponseBody> ClientResponseCall, Throwable t) {
                Logger.write("Request failed: "+t.getMessage());
                DialogUtils.closeProgress();
            }
        });
        
    }

    private void updateGoal(final Goal goal) {

        validate();

        GoalApi goalApi = MainApplication.createService(GoalApi.class, user.getToken());
        DialogUtils.displayProgress(this);
        Call<ResponseBody> response = goalApi.updateGoal(goal.getId(), goal);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                try {
                    GeneralResponse response =new GeneralResponse(resResponse.body());
                    if (response.isAuthFailed()) {
                        User.LogOut(LineAddGoalActivity.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==200) {
                        Goal the_goal = response.getData(Constants.GOAL_DATA, Goal.class);
                        if(the_goal.getId().equals(goal.getId())){
                            Logger.write("Update goal id +"+the_goal.getId());
                            bundle.putSerializable(Constants.GOAL_DATA, the_goal);
                            MyUtils.startActivity(LineAddGoalActivity.this, LineViewGoalActivity.class, bundle);
                        }
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
            public void onFailure(Call<ResponseBody> ClientResponseCall, Throwable t) {
                Logger.write("Request failed: "+t.getCause());
                DialogUtils.closeProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.layout_date:
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.show(this.getSupportFragmentManager(), "datePicker");
                break;
            case  R.id.ok_but:
                if (goal.getId() == null){
                    createGoal();
                }else {
                    updateGoal(goal);
                }
                break;
        }
    }
}
