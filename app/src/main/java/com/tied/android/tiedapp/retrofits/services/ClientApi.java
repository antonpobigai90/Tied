package com.tied.android.tiedapp.retrofits.services;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.responses.Count;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface ClientApi {

    @Multipart
    @POST(Constants.CLIENTS)
    Call<ClientRes> createClient(@Header(Constants.TOKEN_HEADER) String token,
                                 @Part("client") RequestBody client,
                                 @Part MultipartBody.Part file);

    @GET(Constants.USER_CLIENTS)
    Call<ClientRes> getClients(@Header(Constants.TOKEN_HEADER) String token);

    @GET(Constants.USER_CLIENTS_COUNT)
    Call<Count> getClientsCount(@Header(Constants.TOKEN_HEADER) String token);

    @POST(Constants.USER_GE0_CLIENTS)
    Call<ClientRes> getClientsByLocation(@Header(Constants.TOKEN_HEADER) String token, @Body ClientLocation user);
}
