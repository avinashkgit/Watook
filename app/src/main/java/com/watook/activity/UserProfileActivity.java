package com.watook.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.watook.R;
import com.watook.adapter.UserProfileImageAdapter;
import com.watook.application.MyApplication;
import com.watook.manager.ApiManager;
import com.watook.manager.DatabaseManager;
import com.watook.model.response.CodeValueResponse;
import com.watook.model.response.NearByListResponse;
import com.watook.model.response.RequestSaveResponse;
import com.watook.model.response.UserResponse;
import com.watook.util.Constant;
import com.watook.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends BaseActivity implements View.OnClickListener {

    static final int LIKE = 1, ACCEPT = 2, REJECT = 3, BLOCK = 4, UNLIKE = 5;
    Context context;
    FloatingActionButton actionButton;
    LinearLayout layRequest;
    Button btnLike, btnAccept, btnReject;
    UserResponse.User user;
    TextView txtNameAge, txtBio, txtInfo, txtDist;
    ViewPager mPager;
    CirclePageIndicator indicator;
    Long othersID;
    boolean isLiked = false;
    ImageView ivMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        context = this;
        setUpToolBar();
        inItView();
//        bindView();
    }


    private void inItView() {
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mPager = (ViewPager) findViewById(R.id.pager);
        actionButton = (FloatingActionButton) findViewById(R.id.fab);
        actionButton.setOnClickListener(this);
        txtNameAge = (TextView) findViewById(R.id.tv_name_age);
        txtBio = (TextView) findViewById(R.id.txt_bio);
        txtInfo = (TextView) findViewById(R.id.txt_work);
        txtDist = (TextView) findViewById(R.id.txt_dist);
        ivMenu = (ImageView) findViewById(R.id.iv_menu);
        ivMenu.setOnClickListener(this);
        layRequest = (LinearLayout) findViewById(R.id.lay_has_request);
        btnLike = (Button) findViewById(R.id.btn_like);
        btnLike.setOnClickListener(this);
        btnAccept = (Button) findViewById(R.id.btn_accept);
        btnAccept.setOnClickListener(this);
        btnReject = (Button) findViewById(R.id.btn_reject);
        btnReject.setOnClickListener(this);

        othersID = getIntent().getLongExtra(Constant.OTHERS_ID, -1);
        apiCallGetUser();
    }

    private void bindView() {
        if (user != null) {

            if (user.getRequest().getReqstatus().equals(MyApplication.getRequestStatusCode().get(Constant.ACCEPTED))) {
                btnLike.setVisibility(View.GONE);
                layRequest.setVisibility(View.GONE);
                actionButton.setVisibility(View.VISIBLE);
                ivMenu.setVisibility(View.VISIBLE);
//                setFriends();

            } else if (user.getRequest().getReqstatus().equals(MyApplication.getRequestStatusCode().get(Constant.REJECTED))) {
                btnLike.setVisibility(View.VISIBLE);
                layRequest.setVisibility(View.GONE);
                actionButton.setVisibility(View.GONE);
                ivMenu.setVisibility(View.GONE);

            } else if (user.getRequest().getReqstatus().equals(MyApplication.getRequestStatusCode().get(Constant.LIKED))) {
                ivMenu.setVisibility(View.GONE);
                if (user.getRequest().getRequestBy() == Long.parseLong(MyApplication.getInstance().getUserId())) {
                    isLiked = true;
//                    setLiked();
                    btnLike.setVisibility(View.VISIBLE);
                    layRequest.setVisibility(View.GONE);
                    actionButton.setVisibility(View.GONE);
                } else {
                    btnLike.setVisibility(View.GONE);
                    layRequest.setVisibility(View.VISIBLE);
                    actionButton.setVisibility(View.GONE);
                }

            } else if (user.getRequest().getReqstatus().equals(MyApplication.getRequestStatusCode().get(Constant.BLOCKED))) {
                ivMenu.setVisibility(View.GONE);
                btnLike.setVisibility(View.GONE);
                layRequest.setVisibility(View.GONE);
                actionButton.setVisibility(View.GONE);
            } else {
                ivMenu.setVisibility(View.GONE);
                setUnLiked();
                btnLike.setVisibility(View.VISIBLE);
                layRequest.setVisibility(View.GONE);
                actionButton.setVisibility(View.GONE);
            }

            if (!Utils.isEmpty(String.valueOf(user.getLocation().getLatitude()))
                    && !Utils.isEmpty(String.valueOf(user.getLocation().getLongitude()))) {
                txtDist.setText(Utils.getDistance(this, user.getLocation().getLatitude(), user.getLocation().getLongitude()));
            } else{
                txtDist.setVisibility(View.GONE);
            }


            String nameAge = Utils.emptyIfNull(user.getFirstName()) + " " + Utils.emptyIfNull(user.getLastName());
            txtNameAge.setText(nameAge);
            if (!Utils.isEmpty(user.getAboutYou()))
                txtBio.setText(user.getAboutYou());
            else
                txtBio.setVisibility(View.GONE);
            String workInfo = "";
            if (!Utils.isEmpty(user.getWorkPosition()))
                workInfo = user.getWorkPosition() + ",";
            if (!Utils.isEmpty(user.getWorkEmployer()))
                workInfo = workInfo + " " + user.getWorkEmployer() + ",";
            if (!Utils.isEmpty(user.getWorkPosition()))
                workInfo = workInfo + " " + user.getWorkPosition() + ",";
            txtInfo.setText(workInfo);


            String s = "";
            if (!Utils.isEmpty(user.getWorkPosition()))
                s = user.getWorkPosition();
            if (!Utils.isEmpty(user.getWorkEmployer()))
                s = s + ",\n" + user.getWorkEmployer();
            if (!Utils.isEmpty(user.getWorkLocation()))
                s = s + ",\n" + user.getWorkLocation();
            txtInfo.setText(s);


            // view pager
            List<String> items = Arrays.asList(Utils.emptyIfNull(user.getFbImages()).split("\\s*,\\s*"));
            List<String> images = new ArrayList<>();
            for (String s1 : items) {
                if (!Utils.isEmpty(s1))
                    images.add(s1);
            }
            if (images.size() == 0) {
                images.add(user.getProfileImage());
            }
            mPager.setAdapter(new UserProfileImageAdapter(this, images));
            CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
            indicator.setViewPager(mPager);
            final float density = getResources().getDisplayMetrics().density;
            indicator.setRadius(3 * density);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                ChatActivity.startActivity(this,
                        user.getFirstName() + " " + user.getLastName(),
                        String.valueOf(user.getUserId()),
                        user.getFireBaseToken(), user.getProfileImage());
                break;
            case R.id.btn_like:
                if (!isLiked)
                    apiCallSetRequest(LIKE);
                else
                    apiCallSetRequest(UNLIKE);
                break;
            case R.id.btn_accept:
                acceptClicked();
                break;
            case R.id.btn_reject:
                rejectClicked();
                break;
            case R.id.iv_menu:
                showPopUpMenu();
                break;
        }

    }

    private void showPopUpMenu(){
        PopupMenu popupMenu = new PopupMenu(this, ivMenu);
        popupMenu.inflate(R.menu.menu_user_profile);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_unmatch:
                        apiCallSetRequest(UNLIKE);
                        DatabaseManager.getInstance(UserProfileActivity.this).removeUserFromChatList(othersID);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }



    private void setLiked() {
        btnLike.setText("Unlike");
        btnLike.setTextColor(getResources().getColor(R.color.colorTextWhite));
        btnLike.setBackground(getResources().getDrawable(R.drawable.btn_round_accent));
    }

    private void setUnLiked() {
        isLiked = false;
        btnLike.setText("Like");
        btnLike.setTextColor(getResources().getColor(R.color.colorTextPrimary));
        btnLike.setBackground(getResources().getDrawable(R.drawable.btn_round_outline_primary));
    }
    private void setFriends() {
        btnLike.setEnabled(false);
        btnLike.setText("You both like each other!");
        btnLike.setTextColor(getResources().getColor(R.color.colorTextWhite));
        btnLike.setBackground(getResources().getDrawable(R.drawable.btn_round_primary));
    }

    private void acceptClicked() {
        apiCallSetRequest(ACCEPT);
    }

    private void rejectClicked() {
        apiCallSetRequest(REJECT);
    }

    private void apiCallSetRequest(final int code) {
        HashMap<String, String> map = new HashMap<>();
        map.put("requestBy", MyApplication.getInstance().getUserId());
        map.put("requestTo", othersID.toString());
        if (code == LIKE)
            map.put("reqstatus", MyApplication.getRequestStatusCode().get(Constant.LIKED) + "");
        if (code == ACCEPT)
            map.put("reqstatus", MyApplication.getRequestStatusCode().get(Constant.ACCEPTED) + "");
        if (code == REJECT)
            map.put("reqstatus", MyApplication.getRequestStatusCode().get(Constant.REJECTED) + "");
        if (code == BLOCK)
            map.put("reqstatus", MyApplication.getRequestStatusCode().get(Constant.BLOCKED) + "");
        if (code == UNLIKE)
            map.put("reqstatus", 0 + "");

        Call<RequestSaveResponse> request = ApiManager.getApiInstance().saveRequest(Constant.CONTENT_TYPE,
                MyApplication.getInstance().getToken(), map);
        request.enqueue(new Callback<RequestSaveResponse>() {
            @Override
            public void onResponse(@NonNull Call<RequestSaveResponse> call, @NonNull Response<RequestSaveResponse> response) {
                int statusCode = response.code();
                RequestSaveResponse saveResponse = response.body();
                if (statusCode == 200 && saveResponse != null && saveResponse.getStatus() != null && saveResponse.getStatus().equalsIgnoreCase("success")) {
                    apiCallGetUser();
                } else
                    showAToast(getResources().getString(R.string.oops_something_went_wrong));
            }

            @Override
            public void onFailure(@NonNull Call<RequestSaveResponse> call, @NonNull Throwable t) {
                showAToast(getResources().getString(R.string.oops_server_response_failure));
            }
        });

    }

    private void apiCallGetUser() {
        Call<UserResponse> codeValue = ApiManager.getApiInstance().getUser(Constant.CONTENT_TYPE,
                DatabaseManager.getInstance(UserProfileActivity.this).getRegistrationData().getData(), MyApplication.getInstance().getUserId(), othersID.toString());
        codeValue.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                int statusCode = response.code();
                UserResponse userResponse = response.body();
                if (statusCode == 200 && userResponse != null && userResponse.getStatus() != null && userResponse.getStatus().equalsIgnoreCase("success")) {
                    user = userResponse.getData();
                    bindView();
                } else
                    showAToast(getResources().getString(R.string.oops_something_went_wrong));
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                showAToast(getResources().getString(R.string.oops_server_response_failure));
            }
        });
    }


}
