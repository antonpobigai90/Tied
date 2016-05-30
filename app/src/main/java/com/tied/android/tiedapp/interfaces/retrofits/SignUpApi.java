package com.tied.android.tiedapp.interfaces.retrofits;

import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.auth.CheckEmail;
import com.tied.android.tiedapp.objects.auth.Login;
import com.tied.android.tiedapp.objects.auth.UpdateAvatar;
import com.tied.android.tiedapp.objects.auth.UpdateUser;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

/**
 * Created by Emmanuel on 5/19/2016.
 */
public interface SignUpApi {

    @FormUrlEncoded
    @POST(Constants.AUTH_EMAIL_ENDPOINT)
    Call<CheckEmail> checkEmail(@Field("email") String email);


    @FormUrlEncoded
    @POST(Constants.AUTH_REGISTER_ENDPOINT)
    Call<Login> LoginSignUpUser(@Field("email") String email, @Field("password") String password, @Field("sign_up_stage") int sign_up_stage );

    @Multipart
    @PUT(Constants.USER_UPDATE_AVATAR)
    Call<UpdateAvatar> uploadAvatar(@Part("id") RequestBody id, @Part("token") RequestBody token, @Part("sign_up_stage") RequestBody sign_up_stage, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @PUT(Constants.USER_UPDATE_INFO)
    Call<UpdateUser> updateUserName(@Field("id") String id, @Field("token") String token, @Field("first_name") String first_name, @Field("last_name") String last_name, @Field("sign_up_stage") int sign_up_stage);

    @FormUrlEncoded
    @PUT(Constants.USER_UPDATE_INFO)
    Call<UpdateUser> updateUserPhoneAndFax(@Field("id") String id, @Field("token") String token, @Field("phone") String phone, @Field("fax") String fax, @Field("sign_up_stage") int sign_up_stage);

    @FormUrlEncoded
    @PUT(Constants.USER_UPDATE_INFO)
    Call<UpdateUser> updateOfficeAddress(@Field("id") String id, @Field("token") String token, @Field("office_address") Location office_address, @Field("sign_up_stage") int sign_up_stage);

    @FormUrlEncoded
    @PUT(Constants.USER_UPDATE_INFO)
    Call<UpdateUser> updateHomeAddress(@Field("id") String id, @Field("token") String token, @Field("home_address") Location home_address, @Field("sign_up_stage") int sign_up_stage);

}
