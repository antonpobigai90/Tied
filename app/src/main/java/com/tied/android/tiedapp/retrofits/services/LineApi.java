package com.tied.android.tiedapp.retrofits.services;

/**
 * Created by Femi on 7/26/2016.
 */

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.responses.Count;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface LineApi {


    @POST(Constants.LINES)
    Call<ResponseBody> createLine(@Body Line line);

    @PUT(Constants.UPDATE_LINE_WITH_ID)
    Call<ResponseBody> updateLine(@Path("line_id")  String line_id,@Body Line line);

    @GET(Constants.USER_LINES)
    Call<ResponseBody> getLines(@Header(Constants.TOKEN_HEADER) String token);

    @GET(Constants.GET_LINE_WITH_ID)
    Call<Line> getLineWithId(@Header(Constants.TOKEN_HEADER) String token, @Path("line_id")  String line_id);

    @GET(Constants.USER_LINES)
    Call<ResponseBody> getUserLines();

    @GET(Constants.USER_LINE_REVENUES)
    Call<ResponseBody> getLineRevenues(@Header(Constants.TOKEN_HEADER) String token,
                                          @Path("line_id")  String line_id,
                                          @Path("page_number")  int page_number);

    @GET(Constants.USER_LINE_COUNT)
    Call<Count> getLineCount(@Header(Constants.TOKEN_HEADER) String token);

    @POST(Constants.USER_GE0_LINES)
    Call<ClientRes> getLineByLocation(@Header(Constants.TOKEN_HEADER) String token, @Body ClientLocation clientLocation);
}
