package com.watook.callback;

import android.app.Application;

import com.watook.model.ApplicationIdResponse;
import com.watook.model.MyProfile;
import com.watook.model.response.CodeValueResponse;
import com.watook.model.response.ProfileSaveResponse;
import com.watook.model.response.RegistrationResponse;
import com.watook.model.response.SaveLocationResponse;
import com.watook.model.response.UserListResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Avinash on 22-07-2017.
 */

public interface WatookApi {



    @GET("user/auth")
    Call<RegistrationResponse> authenticate(@Query("applicationId") String ApplicationId, @Query("fbToken") String fbToken);

    @POST("user/save")
    Call<ProfileSaveResponse> saveProfile(@Header("Content-Type") String content_type, @Header("token") String token, @Body HashMap map);

    @GET("master/codevalue")
    Call<CodeValueResponse> getCodeValue(@Header("Content-Type") String content_type, @Header("token") String token);

    @GET("user/list")
    Call<UserListResponse> getUserList(@Header("Content-Type") String content_type, @Header("token") String token);

    @POST("location/save")
    Call<SaveLocationResponse> saveLocation(@Header("Content-Type") String content_type, @Header("token") String token, @Body HashMap map);

    @GET
    Call<ApplicationIdResponse> getApplicationId(@Url String url, @Query("access_token") String token);
}
