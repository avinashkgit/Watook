package com.watook.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.watook.R;
import com.watook.application.MyApplication;
import com.watook.manager.ApiManager;
import com.watook.manager.DatabaseManager;
import com.watook.model.MyProfile;
import com.watook.model.response.ConnectionTypeResponse;
import com.watook.util.Constant;
import com.watook.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Avinash on 11-09-2017.
 */

public class ApiService extends IntentService {
    public static final String REQUEST_STRING = "REQUEST_STRING";

    public static final String API_BLOCKED_LIST = "API_BLOCKED_LIST";
    public static final String API_GET_FB_STATUS = "API_GET_FB_STATUS";
    public static final String API_SAVE_LOCATION = "API_SAVE_LOCATION";


    LocalBroadcastManager broadcaster;
    public static final String BROADCAST_RESULT = "BROADCAST_RESULT";
    public static final String BROADCAST_MESSAGE = "BROADCAST_MESSAGE";
    String broadcastString;


    public ApiService() {
        super("ApiService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String requestString = intent.getStringExtra(REQUEST_STRING);

        if (Utils.isNetworkAvailable()) {
            if (requestString.equalsIgnoreCase(API_BLOCKED_LIST)) {
                apiCallGetBlockedList();
            } else if (requestString.equalsIgnoreCase(API_GET_FB_STATUS)) {
                apiCallGetAboutStatus();
            } else if (requestString.equalsIgnoreCase(API_SAVE_LOCATION)) {
                apiCallGetAboutStatus();
            }
        }
    }

    private void apiCallGetBlockedList() {
        Call<ConnectionTypeResponse> codeValue = ApiManager.getApiInstance().getRequests(Constant.CONTENT_TYPE,
               MyApplication.getInstance().getToken(), MyApplication.getInstance().getUserId(), MyApplication.getRequestStatusCode().get(Constant.BLOCKED).toString());
        codeValue.enqueue(new Callback<ConnectionTypeResponse>() {
            @Override
            public void onResponse(@NonNull Call<ConnectionTypeResponse> call, @NonNull Response<ConnectionTypeResponse> response) {
                int statusCode = response.code();
                ConnectionTypeResponse connectionTypeResponse = response.body();
                if (statusCode == 200 && connectionTypeResponse != null && connectionTypeResponse.getStatus() != null && connectionTypeResponse.getStatus().equalsIgnoreCase("success")) {
                    for(ConnectionTypeResponse.User user : connectionTypeResponse.getData()) {
                        DatabaseManager.getInstance(MyApplication.getInstance()).insertBlocked(user);
                    }
                    broadcastString = API_BLOCKED_LIST;
                    broadcastEvent(broadcastString);
                }

            }

            @Override
            public void onFailure(@NonNull Call<ConnectionTypeResponse> call, @NonNull Throwable t) {

        }
        });
    }




    private void apiCallGetAboutStatus() {
        final MyProfile myProfile = DatabaseManager.getInstance(this).getMyProfile();
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + myProfile.getFbId() + "?fields=about",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject object = response.getJSONObject();
                        try {
                            String bio = object.getString("about");
                            myProfile.setBio(bio);
                            DatabaseManager.getInstance(MyApplication.getContext()).insertMyProfile(myProfile);
                            broadcastString = API_GET_FB_STATUS;
                            broadcastEvent(broadcastString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void broadcastEvent(String broadcastString) {
        Intent intent = new Intent(BROADCAST_RESULT);
        if (broadcastString != null)
            intent.putExtra(BROADCAST_MESSAGE, broadcastString);
        broadcaster.sendBroadcast(intent);
    }

}
