package com.tied.android.tiedapp.interfaces.retrofits;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.responses.ServerRes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface ClientApi {

    @FormUrlEncoded
    @POST(Constants.USER_CHANGE_PASSWORD)
    Call<ServerRes> changePassword(@Header(Constants.TOKEN_HEADER) String token, @Field("password") String password, @Field("new_password") String new_password);

    @Multipart
    @POST(Constants.CLIENTS)
    Call<ServerRes> createClient(@Header(Constants.TOKEN_HEADER) String token,
                                 @Part("company") RequestBody company,
                                 @Part("full_name") RequestBody full_name,
                                 @Part("address") RequestBody address,
                                 @Part MultipartBody.Part file);

    @GET(Constants.USER_CLIENTS)
    Call<ClientRes> getClients(@Header(Constants.TOKEN_HEADER) String token);
}
