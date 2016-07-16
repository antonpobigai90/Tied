package com.tied.android.tiedapp.ui.fragments.schedule.tabs;

import android.view.View;

import com.tied.android.tiedapp.objects.schedule.DateRange;
import com.tied.android.tiedapp.objects.schedule.ScheduleDate;
import com.tied.android.tiedapp.objects.schedule.TimeRange;

import java.util.Calendar;

/**
 * Created by Emmanuel on 7/15/2016.
 */
public class ThisMonthScheduleFragment extends SchedulesFragment implements View.OnClickListener {

    public static final String TAG = TodayScheduleFragment.class
            .getSimpleName();

    protected void initComponent(View view) {
        Calendar cal = Calendar.getInstance();
        timeRange = new TimeRange("00:00","23:59");
        dateRange = new DateRange("2016-07-01","2016-07-31");
        scheduleDate = new ScheduleDate(timeRange, dateRange);
        super.initComponent(view);
    }
}
