package com.watook.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.watook.R;
import com.watook.application.GPSTracker;
import com.watook.application.MyApplication;
import com.watook.application.MySharedPreferences;
import com.watook.fcm.MyFirebaseInstanceIDService;
import com.watook.manager.ApiManager;
import com.watook.manager.DatabaseManager;
import com.watook.model.Preferences;
import com.watook.model.response.ApplicationIdResponse;
import com.watook.model.MyProfile;
import com.watook.model.response.CodeValueResponse;
import com.watook.model.response.ProfileSaveResponse;
import com.watook.model.response.RegistrationResponse;
import com.watook.model.response.SaveLocationResponse;
import com.watook.util.Constant;
import com.watook.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String TAG = "Permissions";
    Button btnLogin;

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Glide.with(this).load(R.drawable.logo).asBitmap().into((ImageView) findViewById(R.id.img_txt_logo));
//        getHashCode();
        inItUi();

    }

    private void getHashCode() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.watook",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void inItUi() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        btnLogin = (Button) findViewById(R.id.custom_login_button);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends, user_likes, user_photos, user_about_me, user_relationships, user_work_history"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getFacebookData(loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login attempt canceled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(LoginActivity.this, "Login attempt failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getFriendList(String userID) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + userID + "/friendlists", null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                    }
                }
        ).executeAsync();

    }

    private void myNewGraphReq(String friendlistId) {
        final String graphPath = "/" + friendlistId + "/members/";
        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest request = new GraphRequest(token, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                JSONObject object = graphResponse.getJSONObject();
                try {
                    JSONArray arrayOfUsersInFriendList = object.getJSONArray("data");
                /* Do something with the user list */
                /* ex: get first user in list, "name" */
                    JSONObject user = arrayOfUsersInFriendList.getJSONObject(0);
                    String usersName = user.getString("name");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }

    }


    private boolean checkAndRequestPermissions() {
//        int permissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
//        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
//        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
//                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (/*perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                            &&*/ perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showDialogOK("SMS and Location Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    showAToast("Cannot proceed without location permission.");
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    private void getFacebookData(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            Log.i("Response", response.toString());
                            String id = "", email="", firstName="", lastName="", gender="", picture="", birthday="";
                            try {
                                id = response.getJSONObject().getString("id");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                email = response.getJSONObject().getString("email");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                firstName = response.getJSONObject().getString("first_name");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                lastName = response.getJSONObject().getString("last_name");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                gender = response.getJSONObject().getString("gender");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                birthday = response.getJSONObject().getString("birthday");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                picture = "https://graph.facebook.com/" + id + "/picture?type=large";
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            MyProfile myProfile = new MyProfile();
                            myProfile.setFbId(Utils.emptyIfNull(id));
                            myProfile.setEmail(Utils.emptyIfNull(email));
                            myProfile.setFirstName(Utils.emptyIfNull(firstName));
                            myProfile.setLastName(Utils.emptyIfNull(lastName));
                            myProfile.setGender(Utils.emptyIfNull(gender));
                            myProfile.setBirthday(Utils.emptyIfNull(birthday));
                            myProfile.setProfilePicture(Utils.emptyIfNull(picture));

                            getApplicationId(myProfile, loginResult.getAccessToken().getToken());
                            getFriendList(id);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, email, first_name, last_name, gender, link, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getApplicationId(final MyProfile myProfile, final String accessToken) {
        Call<ApplicationIdResponse> registrationResponseCall = ApiManager.getApiInstance().getApplicationId("https://graph.facebook.com/app", accessToken);
        registrationResponseCall.enqueue(new Callback<ApplicationIdResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApplicationIdResponse> call, @NonNull Response<ApplicationIdResponse> response) {
                int statusCode = response.code();
                ApplicationIdResponse registrationResponse = response.body();
                if (statusCode == 200 && registrationResponse != null && registrationResponse.getId() != null) {
                    if (!Utils.isEmpty(registrationResponse.getId())) {
                        registerUser(registrationResponse.getId(), accessToken, myProfile);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApplicationIdResponse> call, @NonNull Throwable t) {
                showAToast(getResources().getString(R.string.oops_something_went_wrong));
            }
        });

    }

    private void registerUser(String AppID, String accessToken, final MyProfile myProfile) {
        showProgressDialog("Loading...", null);

        Call<RegistrationResponse> registrationResponseCall = ApiManager.getApiInstance().authenticate(
                AppID, accessToken);
        registrationResponseCall.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponse> call, @NonNull Response<RegistrationResponse> response) {
                int statusCode = response.code();
                RegistrationResponse registrationResponse = response.body();
                if (statusCode == 200 && registrationResponse != null && registrationResponse.getStatus() != null && registrationResponse.getStatus().equalsIgnoreCase("success")) {
                    DatabaseManager.getInstance(LoginActivity.this).insertRegistrationData(registrationResponse);
                    try {
                        MySharedPreferences.putObject(Constant.TOKEN, registrationResponse.getData());
                        MyApplication.getInstance().setToken(registrationResponse.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    apiCallGetCodeValue(myProfile, registrationResponse);
                } else {
                    dismissProgressDialog();
                    showAToast(getResources().getString(R.string.oops_something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegistrationResponse> call, @NonNull Throwable t) {
                showAToast(getResources().getString(R.string.oops_something_went_wrong));
                dismissProgressDialog();
            }
        });
    }

    private void navigateView() {
        dismissProgressDialog();
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }


    private void apiCallGetCodeValue(final MyProfile myProfile, final RegistrationResponse registrationResponse) {
        Call<CodeValueResponse> codeValue = ApiManager.getApiInstance().getCodeValue(Constant.CONTENT_TYPE,
                registrationResponse.getData());
        codeValue.enqueue(new Callback<CodeValueResponse>() {
            @Override
            public void onResponse(@NonNull Call<CodeValueResponse> call, @NonNull Response<CodeValueResponse> response) {
                int statusCode = response.code();
                CodeValueResponse codeValueResponse = response.body();
                if (statusCode == 200 && codeValueResponse != null && codeValueResponse.getStatus() != null && codeValueResponse.getStatus().equalsIgnoreCase("success")) {
                    DatabaseManager.getInstance(LoginActivity.this).insertCodeValue(codeValueResponse.getData());
                    apiCallSaveProfile(myProfile, codeValueResponse.getData(), registrationResponse);
                } else {
                    dismissProgressDialog();
                    showAToast(getResources().getString(R.string.oops_something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CodeValueResponse> call, @NonNull Throwable t) {
                dismissProgressDialog();
                showAToast(getResources().getString(R.string.oops_something_went_wrong));
            }
        });
    }

    private void apiCallSaveProfile(final MyProfile myProfile, List<CodeValueResponse.CodeValue> codeValues, final RegistrationResponse rp) {
        long genderId = 0;
        long statusCode = 0;
        for (CodeValueResponse.CodeValue cv : codeValues) {
            if (cv.getCodeValue().equalsIgnoreCase(myProfile.getGender()))
                genderId = cv.getCodeValueID();
        }

        for (CodeValueResponse.CodeValue cv : codeValues) {
            if (cv.getCodeValue().equalsIgnoreCase("online"))
                statusCode = cv.getCodeValueID();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("fbId", myProfile.getFbId());
        map.put("firstName", myProfile.getFirstName());
        map.put("lastName", myProfile.getLastName());
        map.put("isActive", 1 + "");
        map.put("statusInfo", statusCode + "");
        map.put("genderId", genderId + "");
        map.put("birthday", "15/02/1991");
        map.put("emailId", myProfile.getEmail());
        map.put("advertiseId", myProfile.getFbId());
        map.put("aboutYou", myProfile.getBio());
        map.put("workEmployer", myProfile.getWorkEmployer());
        map.put("workPosition", myProfile.getWorkPosition());
        map.put("workLocation", myProfile.getWorkLocation());
        map.put("profileImage", myProfile.getProfilePicture());
        map.put("fireBaseToken", FirebaseInstanceId.getInstance().getToken());


        Call<ProfileSaveResponse> saveProfile = ApiManager.getApiInstance().saveProfile(Constant.CONTENT_TYPE,
                rp.getData(), map);
        saveProfile.enqueue(new Callback<ProfileSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProfileSaveResponse> call, @NonNull Response<ProfileSaveResponse> response) {
                int statusCode = response.code();
                ProfileSaveResponse saveResponse = response.body();
                if (statusCode == 200 && saveResponse != null && saveResponse.getStatus() != null && saveResponse.getStatus().equalsIgnoreCase("success")) {
                    myProfile.setUserId(saveResponse.getData().getUserId());
                    try {
                        MyApplication.getInstance().setUserId(saveResponse.getData().getUserId());
                        MySharedPreferences.putObject(Constant.USER_ID, saveResponse.getData().getUserId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DatabaseManager.getInstance(LoginActivity.this).insertMyProfile(myProfile);
                    saveLocation();

                } else {
                    dismissProgressDialog();
                    showAToast(getResources().getString(R.string.oops_something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileSaveResponse> call, @NonNull Throwable t) {
                dismissProgressDialog();
                showAToast(getResources().getString(R.string.oops_something_went_wrong));
            }
        });
    }

    private void saveLocation() {
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            apiCallSaveLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            Intent i = new Intent(this, GPSTracker.class);
            startService(i);

//            setPreferences();
//            try {
//                MySharedPreferences.putObject(Constant.IS_LOGGED_IN, true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            navigateView();

        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    private void apiCallSaveLocation(Double latitude, Double longitude) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", DatabaseManager.getInstance(this).getMyProfile().getUserId());
        map.put("latitude", latitude + "");
        map.put("longitude", longitude + "");


        Call<SaveLocationResponse> saveProfile = ApiManager.getApiInstance().saveLocation(Constant.CONTENT_TYPE,
                DatabaseManager.getInstance(this).getRegistrationData().getData(), map);
        saveProfile.enqueue(new Callback<SaveLocationResponse>() {
            @Override
            public void onResponse(@NonNull Call<SaveLocationResponse> call, @NonNull Response<SaveLocationResponse> response) {
                int statusCode = response.code();
                SaveLocationResponse saveResponse = response.body();
                if (statusCode == 200 && saveResponse != null && saveResponse.getStatus() != null && saveResponse.getStatus().equalsIgnoreCase("success")) {
                    setPreferences();
                    try {
                        MySharedPreferences.putObject(Constant.IS_LOGGED_IN, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    navigateView();
                } else {
                    dismissProgressDialog();
                    showAToast(getResources().getString(R.string.oops_something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SaveLocationResponse> call, @NonNull Throwable t) {
                showAToast(getResources().getString(R.string.oops_something_went_wrong));
                dismissProgressDialog();
            }
        });

    }


//    private void apiCallSavePreferences() {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("userId", DatabaseManager.getInstance(this).getMyProfile().getUserId());
//        map.put("latitude", latitude + "");
//        map.put("longitude", longitude + "");
//
//
//        Call<PreferencesSaveResponse> saveProfile = ApiManager.getApiInstance().setPreferences(Constant.CONTENT_TYPE,
//                DatabaseManager.getInstance(this).getRegistrationData().getData(), map);
//        saveProfile.enqueue(new Callback<PreferencesSaveResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<PreferencesSaveResponse> call, @NonNull Response<PreferencesSaveResponse> response) {
//                int statusCode = response.code();
//                PreferencesSaveResponse saveResponse = response.body();
//                if (statusCode == 200 && saveResponse != null && saveResponse.getStatus() != null && saveResponse.getStatus().equalsIgnoreCase("success")) {
//                    navigateView();
//                } else {
//                    dismissProgressDialog();
//                    showAToast(getResources().getString(R.string.oops_something_went_wrong));
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<PreferencesSaveResponse> call, @NonNull Throwable t) {
//                showAToast(getResources().getString(R.string.oops_something_went_wrong));
//                dismissProgressDialog();
//            }
//        });
//
//    }

    private void setPreferences() {
        Preferences pref = new Preferences();
        pref.setDistanceUnitKm(false);
        pref.setAgeMin(18);
        pref.setAgeMax(40);
        pref.setDistanceRange(10);
        pref.setFemaleInterest(true);
        pref.setMaleInterest(true);
        pref.setDiscoverable(true);

        DatabaseManager.getInstance(LoginActivity.this).insertPreferences(pref);
        try {
            MySharedPreferences.putObject(Constant.DISTANCE_UNIT_KM, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            showAToast("Authentication success.");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            showAToast("Authentication failed.");
                        }

                        // ...
                    }
                });
    }


}

