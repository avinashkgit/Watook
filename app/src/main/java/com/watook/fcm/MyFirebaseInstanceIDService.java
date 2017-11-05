package com.watook.fcm;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.watook.R;
import com.watook.activity.LoginActivity;
import com.watook.application.MyApplication;
import com.watook.application.MySharedPreferences;
import com.watook.manager.ApiManager;
import com.watook.manager.DatabaseManager;
import com.watook.model.MyProfile;
import com.watook.model.response.CodeValueResponse;
import com.watook.model.response.ProfileSaveResponse;
import com.watook.model.response.RegistrationResponse;
import com.watook.util.Constant;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        try {
            MySharedPreferences.putObject(Constant.ARG_FIREBASE_TOKEN, token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child(Constant.ARG_USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(Constant.ARG_FIREBASE_TOKEN)
                    .setValue(token);
        }
        apiCallSaveProfile();
    }

    private void apiCallSaveProfile() {
        MyProfile myProfile = DatabaseManager.getInstance(MyApplication.getContext()).getMyProfile();
        List<CodeValueResponse.CodeValue> codeValues = DatabaseManager.getInstance(MyApplication.getContext()).getCodeValue();
        if(myProfile != null && codeValues!= null) {
            long statusCode = 0;
            for (CodeValueResponse.CodeValue cv : codeValues) {
                if (cv.getCodeValue().equalsIgnoreCase("online"))
                    statusCode = cv.getCodeValueID();
            }

            HashMap<String, String> map = new HashMap<>();
            map.put("fbId", myProfile.getFbId());
            map.put("firstName", myProfile.getFirstName());
            map.put("statusInfo", statusCode + "");
            map.put("fireBaseToken", FirebaseInstanceId.getInstance().getToken());


            Call<ProfileSaveResponse> saveProfile = ApiManager.getApiInstance().saveProfile(Constant.CONTENT_TYPE,
                    MyApplication.getInstance().getToken(), map);
            saveProfile.enqueue(new Callback<ProfileSaveResponse>() {
                @Override
                public void onResponse(@NonNull Call<ProfileSaveResponse> call, @NonNull Response<ProfileSaveResponse> response) {

                }

                @Override
                public void onFailure(@NonNull Call<ProfileSaveResponse> call, @NonNull Throwable t) {

                }
            });
        }
    }
}