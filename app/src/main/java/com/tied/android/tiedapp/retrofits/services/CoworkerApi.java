package com.tied.android.tiedapp.retrofits.services;

import com.tied.android.tiedapp.customs.Constants;

import com.tied.android.tiedapp.objects.RevenueFilter;
import com.tied.android.tiedapp.objects.user.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface CoworkerApi {

    @POST(Constants.ADD_COWORKER)
    Call<ResponseBody> addCoworker(@Header(Constants.TOKEN_HEADER) String token,  @Body User coworker_id);

    @GET(Constants.GET_COWORKERS)
    Call<ResponseBody> getCoworkers(@Header(Constants.TOKEN_HEADER) String token, @Path("user_id") String user_id, @Path("group") String group,  @Path("count") int count, @Path("filter") RevenueFilter filter);
}
