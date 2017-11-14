package com.watook.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.iid.FirebaseInstanceId;
import com.viewpagerindicator.CirclePageIndicator;
import com.watook.R;
import com.watook.adapter.UserProfileImageAdapter;
import com.watook.application.MyApplication;
import com.watook.application.MySharedPreferences;
import com.watook.manager.ApiManager;
import com.watook.manager.DatabaseManager;
import com.watook.model.MyProfile;
import com.watook.model.response.CodeValueResponse;
import com.watook.model.response.ProfileSaveResponse;
import com.watook.service.ApiService;
import com.watook.util.Constant;
import com.watook.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends BaseActivity implements View.OnClickListener {

    String TAG = getClass().getSimpleName();
    MyProfile myProfile;
    private ArrayList<String> ImagesIdArray = new ArrayList<>();
    private ArrayList<String> ImagesURLArray = new ArrayList<>();
    TextView tvNameAge;
    EditText etStatus;
    EditText etInfo;
    TextView tvAddress;
    TextView tvRelationStatus;
    ImageView ivEditStatus, ivEditInfo;
    Boolean editStatus = false, editInfo=false;
    BroadcastReceiver broadcastReceiver;
    Context context;
    ImageView ivProfileSelection;
    RadioButton rbMale, rbFemale;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        context = this;
        setUpToolBar();
        getSupportActionBar().setTitle("");
        myProfile = DatabaseManager.getInstance(MyProfileActivity.this).getMyProfile();

        inIt();

        if (myProfile.getBio() == null)
            getAboutStatus();
        if (myProfile.getRelationshipStatus() == null)
            getRelationshipStatus();
        if (myProfile.getWorkPosition() == null)
            getWorkInfo();
        if (myProfile.getListOfProfilePic() == null)
            getAlbum();
        else {
            ImagesURLArray = myProfile.getListOfProfilePic();
            setData();
        }
        bindView();

    }

    private void bindView() {

        // bind bio
        etStatus.setText(myProfile.getBio());


        // bind relation status
        tvRelationStatus.setText(myProfile.getRelationshipStatus());

        // bind work info
        String s = "";
        if (!Utils.isEmpty(myProfile.getWorkPosition()))
            s = myProfile.getWorkPosition();
        if (!Utils.isEmpty(myProfile.getWorkEmployer()))
            s = s + ",\n" + myProfile.getWorkEmployer();
        if (!Utils.isEmpty(myProfile.getWorkLocation()))
            s = s + ",\n" + myProfile.getWorkLocation();

        etInfo.setText(s);

        // bind name age
        Integer age = 0;
        try {
            age = Utils.getAge(myProfile.getBirthday(), "MM/dd/yyyy");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String nameAge = Utils.emptyIfNull(myProfile.getFirstName()) + " " + Utils.emptyIfNull(myProfile.getLastName());
        if (age > 18)
            nameAge = nameAge + ", " + age;
        tvNameAge.setText(nameAge);

        // bind gender
        if (myProfile.getGender().startsWith("m"))
            rbMale.setChecked(true);
        if (myProfile.getGender().startsWith("f"))
            rbFemale.setChecked(true);
    }

    private void setData() {
        ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new UserProfileImageAdapter(MyProfileActivity.this, ImagesURLArray));
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(3 * density);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int pos) {
                Log.e(TAG, "onPageSelected " + pos + "");
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
                Log.e(TAG, "onPageScrolled " + pos + "");
                position = pos;
                if (myProfile.getProfilePicture().equals(ImagesURLArray.get(pos)))
                    ivProfileSelection.setBackground(getResources().getDrawable(R.drawable.shape_profile_selected));
                else
                    ivProfileSelection.setBackground(getResources().getDrawable(R.drawable.shape_profile_not_selected));
            }

            @Override
            public void onPageScrollStateChanged(int pos) {
                Log.e(TAG, "onPageScrollStateChanged " + pos + "");
            }
        });
    }

    private void inIt() {

        tvNameAge = (TextView) findViewById(R.id.tv_name_age);
        etStatus = (EditText) findViewById(R.id.et_status);
        etStatus.setEnabled(false);

        etInfo = (EditText) findViewById(R.id.tv_work);
//        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvRelationStatus = (TextView) findViewById(R.id.tv_relation_status);
        ivEditStatus = (ImageView) findViewById(R.id.iv_edit_status);
        ivEditStatus.setOnClickListener(this);
        ivEditInfo = (ImageView) findViewById(R.id.iv_edit_info);
        ivEditInfo.setOnClickListener(this);
        rbMale = (RadioButton) findViewById(R.id.rb_male);
        rbFemale = (RadioButton) findViewById(R.id.rb_female);
        ivProfileSelection = (ImageView) findViewById(R.id.iv_profile);
        ivProfileSelection.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
                new IntentFilter(ApiService.BROADCAST_RESULT)
        );
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onStop();

    }


    public void getAlbum() {
        String token = AccessToken.getCurrentAccessToken().getToken();
        final String graphPath = "/" + myProfile.getFbId() + "/albums?" + "access_token=" + token;

        GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(), graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                JSONObject object = graphResponse.getJSONObject();
                try {
                    JSONArray jsonArray = object.getJSONArray("data");
                    if (jsonArray.length() > 1) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String albumName = jsonObject.getString("name");
                            if (albumName.equalsIgnoreCase("Profile Pictures")) {
                                String id = jsonObject.getString("id");
                                getProfilePictures(id);
                            }
                        }
                    } else {
                        ImagesURLArray.add(myProfile.getProfilePicture());
                        setData();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    ImagesURLArray.add(myProfile.getProfilePicture());
                    setData();
                }
            }
        });
        Bundle param = new Bundle();
        param.putString("fields", "name");
        request.setParameters(param);
        request.executeAsync();
    }

    private void getProfilePictures(String id) {
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
                            if (jsonArray.length() > 1) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String id = jsonObject.getString("id");
                                    ImagesIdArray.add(id);

                                }
                            } else {
                                ImagesURLArray.add(myProfile.getProfilePicture());
                                setData();
                            }
                            for (String id : ImagesIdArray) {
                                fetchPhoto(id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ImagesURLArray.add(myProfile.getProfilePicture());
                            setData();
                        }
                    }
                }).executeAsync();

    }

    public void fetchPhoto(String id) {
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
                            ImagesURLArray.add(url);
                            setData();
                            myProfile.setListOfProfilePic(ImagesURLArray);
                            DatabaseManager.getInstance(MyProfileActivity.this).insertMyProfile(myProfile);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_edit_status: {
                if (!editStatus) {
                    editStatus = true;
                    ivEditStatus.setImageResource(R.drawable.ic_check_primary_24dp);
                    etStatus.setEnabled(true);
                    Utils.showSoftKeyboard(context, etStatus);
                } else {
                    editStatus = false;
                    ivEditStatus.setImageResource(R.drawable.ic_mode_edit_primary_18dp);
                    etStatus.setEnabled(false);
                    myProfile.setBio(etStatus.getText().toString());
                    Utils.hideSoftKeyboard(context);
                    apiCallSaveProfile();

                }

            }
            break;

            case R.id.iv_edit_info: {
                if (!editInfo) {
                    editInfo = true;
                    ivEditInfo.setImageResource(R.drawable.ic_check_primary_24dp);
                    etInfo.setEnabled(true);
                    Utils.showSoftKeyboard(context, etInfo);
                } else {
                    editInfo = false;
                    ivEditInfo.setImageResource(R.drawable.ic_mode_edit_primary_18dp);
                    etInfo.setEnabled(false);
                    myProfile.setWorkPosition(etInfo.getText().toString());
                    Utils.hideSoftKeyboard(context);
                    apiCallSaveProfile();
                }

            }
            break;

            case R.id.iv_profile: {

                if (ImagesURLArray != null && ImagesURLArray.size() > 0)
                    if (!myProfile.getProfilePicture().equals(ImagesURLArray.get(position)))
                        showAToast("Profile Picture Changed.");

                if (ImagesURLArray != null && ImagesURLArray.size() > 0)
                    myProfile.setProfilePicture(ImagesURLArray.get(position));
                ivProfileSelection.setBackground(getResources().getDrawable(R.drawable.shape_profile_selected));
                DatabaseManager.getInstance(MyProfileActivity.this).insertMyProfile(myProfile);
            }
            break;
        }
    }

    private void getAboutStatus() {
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
                            bindView();
                            DatabaseManager.getInstance(MyProfileActivity.this).insertMyProfile(myProfile);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void getRelationshipStatus() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + myProfile.getFbId() + "?fields=relationship_status",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject object = response.getJSONObject();
                        try {
                            String status = object.getString("relationship_status");
                            myProfile.setRelationshipStatus(status);
                            bindView();
                            DatabaseManager.getInstance(MyProfileActivity.this).insertMyProfile(myProfile);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void getWorkInfo() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + myProfile.getFbId() + "?fields=work",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject object = response.getJSONObject();
                        try {
                            JSONArray work = object.getJSONArray("work");
                            JSONObject employer = work.getJSONObject(0).getJSONObject("employer");
                            JSONObject location = work.getJSONObject(0).getJSONObject("location");
                            JSONObject position = work.getJSONObject(0).getJSONObject("position");

                            myProfile.setWorkEmployer(employer.getString("name"));
                            myProfile.setWorkLocation(location.getString("name"));
                            myProfile.setWorkPosition(position.getString("name"));

                            bindView();

                            DatabaseManager.getInstance(MyProfileActivity.this).insertMyProfile(myProfile);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void apiCallSaveProfile() {
        List<CodeValueResponse.CodeValue> codeValues = DatabaseManager.getInstance(this).getCodeValue();
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
        map.put("isActive", 1 + "");
        map.put("statusInfo", statusCode + "");
        map.put("genderId", genderId + "");
        map.put("aboutYou", myProfile.getBio());
        map.put("workEmployer", myProfile.getWorkEmployer());
        map.put("workPosition", myProfile.getWorkPosition());
        map.put("workLocation", myProfile.getWorkLocation());
        System.out.println(TAG + map.toString());
        Call<ProfileSaveResponse> saveProfile = ApiManager.getApiInstance().saveProfile(Constant.CONTENT_TYPE,
                MyApplication.getInstance().getToken(), map);
        saveProfile.enqueue(new Callback<ProfileSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProfileSaveResponse> call, @NonNull Response<ProfileSaveResponse> response) {
                int statusCode = response.code();
                ProfileSaveResponse saveResponse = response.body();
                if (statusCode == 200 && saveResponse != null && saveResponse.getStatus() != null && saveResponse.getStatus().equalsIgnoreCase("success")) {

                } else {
                    dismissProgressDialog();
                    showAToast(getResources().getString(R.string.oops_something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileSaveResponse> call, @NonNull Throwable t) {
                dismissProgressDialog();
                showAToast(getResources().getString(R.string.oops_server_response_failure));
            }
        });
    }


}
