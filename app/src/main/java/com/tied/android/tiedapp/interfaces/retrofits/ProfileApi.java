package com.tied.android.tiedapp.interfaces.retrofits;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.auth.ServerRes;
import com.tied.android.tiedapp.objects.user.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface ProfileApi {

    @FormUrlEncoded
    @POST(Constants.USER_CHANGE_PASSWORD)
    Call<ServerRes> changePassword(@Header(Constants.TOKEN_HEADER) String token, @Field("password") String password, @Field("new_password") String new_password);

    @GET(Constants.GET_INDUSTRIES)
    Call<User> getUser();
}
