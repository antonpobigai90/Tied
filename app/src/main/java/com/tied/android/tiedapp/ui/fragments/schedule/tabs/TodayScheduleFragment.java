package com.tied.android.tiedapp.ui.fragments.schedule.tabs;

import android.util.Log;
import android.view.View;

import com.tied.android.tiedapp.customs.model.ScheduleDataModel;
import com.tied.android.tiedapp.customs.model.ScheduleTimeModel;
import com.tied.android.tiedapp.objects.schedule.DateRange;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.schedule.ScheduleDate;
import com.tied.android.tiedapp.objects.schedule.TimeRange;
import com.tied.android.tiedapp.util.HelperMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Emmanuel on 7/15/2016.
 */
public class TodayScheduleFragment extends SchedulesFragment implements View.OnClickListener {

    public static final String TAG = TodayScheduleFragment.class
            .getSimpleName();

    protected void initComponent(View view) {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String  today = sdf.format(now);
        timeRange = new TimeRange("00:00","23:59");
        dateRange = new DateRange(today, today);
        scheduleDate = new ScheduleDate(timeRange, dateRange);
        super.initComponent(view);
    }

    public ArrayList<ScheduleDataModel> parseSchedules(ArrayList<Schedule> scheduleArrayList) {

        Log.d(TAG + " parseSchedules", scheduleArrayList.toString());

        ArrayList<ScheduleDataModel> scheduleDataModels = new ArrayList<>();
        for (int i = 0; i < scheduleArrayList.size(); i++) {
            Schedule schedule = scheduleArrayList.get(i);

            ScheduleDataModel scheduleDataModel = new ScheduleDataModel();

            ScheduleTimeModel scheduleTimeModel = new ScheduleTimeModel(schedule.getId(),
                    schedule.getTitle(), schedule.getTime_range().getStart_time());

            ArrayList<ScheduleTimeModel> scheduleTimeModels = new ArrayList<ScheduleTimeModel>();
            scheduleTimeModels.add(scheduleTimeModel);

            String day = String.format("%02d", HelperMethods.getDayFromSchedule(schedule.getDate()));
            String week_day = WEEK_LIST[HelperMethods.getDayOfTheWeek(schedule.getDate()) - 1];

            scheduleDataModel.setScheduleTimeModel(scheduleTimeModels);
            scheduleDataModel.setTemperature("80");
            scheduleDataModel.setWeather("cloudy");
            scheduleDataModel.setDay(day);
            scheduleDataModel.setWeek_day(week_day);

            scheduleDataModels.add(scheduleDataModel);
        }
        return scheduleDataModels;
    }
}
