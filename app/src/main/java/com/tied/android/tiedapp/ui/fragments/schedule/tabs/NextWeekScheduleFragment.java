package com.tied.android.tiedapp.ui.fragments.schedule.tabs;

import android.support.v4.util.Pair;
import android.view.View;

import com.tied.android.tiedapp.objects.schedule.DateRange;
import com.tied.android.tiedapp.objects.schedule.ScheduleDate;
import com.tied.android.tiedapp.objects.schedule.TimeRange;

import java.util.Calendar;

/**
 * Created by Emmanuel on 7/15/2016.
 */
public class NextWeekScheduleFragment extends SchedulesFragment implements View.OnClickListener {

    public static final String TAG = TodayScheduleFragment.class
            .getSimpleName();

    protected void initComponent(View view) {
        Calendar cal = Calendar.getInstance();
        Pair<String,String> date_range_pairs = null;
        date_range_pairs = getWeekRange(cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR) + 1);
        timeRange = new TimeRange("00:00","23:59");
        dateRange = new DateRange(date_range_pairs.first,date_range_pairs.second);
        scheduleDate = new ScheduleDate(timeRange, dateRange);
        super.initComponent(view);
    }
}