package com.tied.android.tiedapp.retrofits.services;


import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Goal;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface GoalApi {

    @POST(Constants.GOALS)
    Call<ResponseBody> createGoal(@Body Goal goal);

    @PUT(Constants.UPDATE_GOAL_WITH_ID)
    Call<ResponseBody> updateGoal(@Path("goal_id") String goal_id,
                                     @Body Goal goal);

    @GET(Constants.USER_GOAL)
    Call<ResponseBody> getGoal();

    @GET(Constants.USER_GOAL)
    Call<ResponseBody> getUserGoals();

    @DELETE(Constants.DELETE_GOAL_WITH_ID)
    Call<ResponseBody> deleteGoal(@Path("goal_id") String goal_id);
}
