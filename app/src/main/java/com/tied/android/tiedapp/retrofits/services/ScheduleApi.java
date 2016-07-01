package com.tied.android.tiedapp.retrofits.services;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.ScheduleRes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface ScheduleApi {

    @Multipart
    @POST(Constants.SCHEDULE)
    Call<ScheduleRes> createClient(@Header(Constants.TOKEN_HEADER) String token,
                                 @Part("client") RequestBody client,
                                 @Part MultipartBody.Part file);

    @GET(Constants.USER_SCHEDULE)
    Call<ScheduleRes> getSchedule(@Header(Constants.TOKEN_HEADER) String token);
}
