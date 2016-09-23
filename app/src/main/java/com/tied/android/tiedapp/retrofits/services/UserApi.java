package com.tied.android.tiedapp.retrofits.services;

import com.tied.android.tiedapp.customs.Constants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface UserApi {

    @GET(Constants.USER_FIND_BY_EMAIL)
    Call<ResponseBody> findByEmail(@Path("email")  String email);
}
