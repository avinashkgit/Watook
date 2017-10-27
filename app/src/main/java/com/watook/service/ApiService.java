package com.watook.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.watook.application.MyApplication;
import com.watook.manager.DatabaseManager;
import com.watook.model.MyProfile;
import com.watook.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Avinash on 11-09-2017.
 */

public class ApiService extends IntentService {
    public static final String REQUEST_STRING = "REQUEST_STRING";
    public static final String API_PROFILE_PIC = "API_PROFILE_PIC";
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
            if (requestString.equalsIgnoreCase(API_PROFILE_PIC)) {
                apiCallGetAlbum();
            } else if (requestString.equalsIgnoreCase(API_GET_FB_STATUS)) {
                apiCallGetAboutStatus();
            } else if (requestString.equalsIgnoreCase(API_SAVE_LOCATION)) {
                apiCallGetAboutStatus();
            }
        }
    }

    private void apiCallGetAlbum() {
        final MyProfile myProfile = DatabaseManager.getInstance(this).getMyProfile();

        AccessToken token = AccessToken.getCurrentAccessToken();
        final String graphPath = "/" + myProfile.getFbId() + "/albums?" + "access_token=" + token;

        GraphRequest request = new GraphRequest(token, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                JSONObject object = graphResponse.getJSONObject();
                try {
                    JSONArray jsonArray = object.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String albumName = jsonObject.getString("name");
                        if (albumName.equalsIgnoreCase("Profile Pictures")) {
                            String id = jsonObject.getString("id");
                            getProfilePictures(id, myProfile);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle param = new Bundle();
        param.putString("fields", "name");
        request.setParameters(param);
        request.executeAsync();
    }

    private void getProfilePictures(String id, final MyProfile myProfile) {
        Bundle params = new Bundle();
        params.putString("url", "{image-url}");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + id + "/photos",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        JSONObject object = graphResponse.getJSONObject();
                        try {
                            JSONArray jsonArray = object.getJSONArray("data");
                            ArrayList<String> imageIdArray = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                imageIdArray.add(id);

                            }
                            final ArrayList<String> profilePics = new ArrayList<>();
                            for (String id : imageIdArray) {
                                new GraphRequest(
                                        AccessToken.getCurrentAccessToken(),
                                        "/" + id + "?fields=images&type=large",
                                        null,
                                        HttpMethod.GET,
                                        new GraphRequest.Callback() {
                                            public void onCompleted(GraphResponse response) {
                                                JSONObject object = response.getJSONObject();
                                                try {
                                                    JSONArray jsonArray = object.getJSONArray("images");
                                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                    String url = jsonObject.getString("source");
                                                    profilePics.add(url);
                                                    myProfile.setListOfProfilePic(profilePics);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                ).executeAndWait();
                            }
                            DatabaseManager.getInstance(MyApplication.getContext()).insertMyProfile(myProfile);
                            broadcastString = API_PROFILE_PIC;
                            broadcastEvent(broadcastString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).executeAsync();

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
