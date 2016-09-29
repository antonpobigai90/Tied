package com.tied.android.tiedapp.retrofits.services;

/**
 * Created by femi on 9/4/2016.
 */

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Revenue;
import com.tied.android.tiedapp.objects.responses.ServerRes;
import com.tied.android.tiedapp.objects.user.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface RevenueApi {

    @POST(Constants.REVENUES)
    Call<ResponseBody> createRevenue(@Body Revenue revenue);
}
