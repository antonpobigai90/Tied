package com.tied.android.tiedapp.retrofits.services;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.ScheduleRes;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.schedule.ScheduleDate;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface ScheduleApi {

    @POST(Constants.SCHEDULES)
    Call<ScheduleRes> createSchedule(@Header(Constants.TOKEN_HEADER) String token,
                                     @Body Schedule schedule);

    @GET(Constants.USER_SCHEDULE)
    Call<ScheduleRes> getSchedule(@Header(Constants.TOKEN_HEADER) String token);

    @POST(Constants.USER_SCHEDULES_BY_DATE)
    Call<ScheduleRes> getScheduleByDate(@Header(Constants.TOKEN_HEADER) String token, @Body ScheduleDate scheduleDate);
}
