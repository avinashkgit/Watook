package com.watook.callback;

import com.watook.model.response.ApplicationIdResponse;
import com.watook.model.response.CodeValueResponse;
import com.watook.model.response.ConnectionsResponse;
import com.watook.model.response.MyLikesResponse;
import com.watook.model.response.NearByListResponse;
import com.watook.model.response.PreferencesSaveResponse;
import com.watook.model.response.ProfileSaveResponse;
import com.watook.model.response.RegistrationResponse;
import com.watook.model.response.RequestSaveResponse;
import com.watook.model.response.SaveLocationResponse;
import com.watook.model.response.UserListResponse;
import com.watook.model.response.UserResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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

    @GET("user/nearbylist")
    Call<NearByListResponse> getNearByList(@Header("Content-Type") String content_type, @Header("token") String token, @Query("userId") String s);

    @POST("location/save")
    Call<SaveLocationResponse> saveLocation(@Header("Content-Type") String content_type, @Header("token") String token, @Body HashMap map);

    @GET
    Call<ApplicationIdResponse> getApplicationId(@Url String url, @Query("access_token") String token);

    @POST("prefernces/save")
    Call<PreferencesSaveResponse> setPreferences(@Header("Content-Type") String content_type, @Header("token") String token, @Body HashMap map);

    @GET("user/get")
    Call<UserResponse> getUser(@Header("Content-Type") String content_type, @Header("token") String token, @Query("userId") String s, @Query("requestId") String j);

    @POST("request/save")
    Call<RequestSaveResponse> saveRequest(@Header("Content-Type") String content_type, @Header("token") String token, @Body HashMap map);

    @GET("connction/friends")
    Call<ConnectionsResponse> getFriendsList(@Header("Content-Type") String content_type, @Header("token") String token, @Query("userId") String s);

    @GET("request/list")
    Call<MyLikesResponse> getRequests(@Header("Content-Type") String content_type, @Header("token") String token, @Query("userId") String s, @Query("requestStatus") String g);


}
