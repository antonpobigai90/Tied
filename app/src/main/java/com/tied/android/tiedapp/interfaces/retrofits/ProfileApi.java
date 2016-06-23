package com.tied.android.tiedapp.interfaces.retrofits;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface ProfileApi {

    @GET(Constants.GET_INDUSTRIES)
    Call<User> getUser();
}
