package com.tied.android.tiedapp.retrofits.services;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Territory;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by femi on 10/4/2016.
 */
public interface  TerritoryApi {
    @POST(Constants.TERRITORIES)
    Call<ResponseBody> create(@Body Line line);

    @PUT(Constants.UPDATE_TERRITORY_WITH_ID)
    Call<ResponseBody> update(@Path("territory_id") String line_id, @Body Territory territory);

    @GET(Constants.USER_TERRITORIES)
    Call<ResponseBody> getTerritories(@Path("user_id") String user_id, @Path("page_number") int page_number);

}
