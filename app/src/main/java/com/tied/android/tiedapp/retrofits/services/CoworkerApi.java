package com.tied.android.tiedapp.retrofits.services;

import com.tied.android.tiedapp.customs.Constants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface CoworkerApi {

    @POST(Constants.ADD_COWORKER)
    Call<ResponseBody> addCoworker(@Path("user_id") String user_id, @Path("coworker_id") String coworker_id);
}
