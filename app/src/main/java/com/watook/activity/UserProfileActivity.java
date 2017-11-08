package com.watook.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.watook.R;
import com.watook.adapter.UserProfileImageAdapter;
import com.watook.application.MyApplication;
import com.watook.manager.DatabaseManager;
import com.watook.model.response.CodeValueResponse;
import com.watook.model.response.NearByListResponse;
import com.watook.util.Constant;
import com.watook.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserProfileActivity extends BaseActivity implements View.OnClickListener {

    static final int LIKE = 1, ACCEPT = 2, REJECT = 3, BLOCK = 4;
    Context context;
    FloatingActionButton actionButton;
    LinearLayout layLike, layRequest;
    Button btnLike, btnAccept, btnReject;
    NearByListResponse.User user;
    TextView txtNameAge, txtBio, txtInfo;
    ViewPager mPager;
    CirclePageIndicator indicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        context = this;
        setUpToolBar();
        inItView();
        bindView();
    }


    private void inItView() {
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mPager = (ViewPager) findViewById(R.id.pager);
        actionButton = (FloatingActionButton) findViewById(R.id.fab);
        actionButton.setOnClickListener(this);
        txtNameAge = (TextView) findViewById(R.id.tv_name_age);
        txtBio = (TextView) findViewById(R.id.txt_bio);
        txtInfo = (TextView) findViewById(R.id.txt_work);
        layLike = (LinearLayout) findViewById(R.id.lay_like);
        layRequest = (LinearLayout) findViewById(R.id.lay_has_request);
        btnLike = (Button) findViewById(R.id.btn_like);
        btnLike.setOnClickListener(this);
        btnAccept = (Button) findViewById(R.id.btn_accept);
        btnAccept.setOnClickListener(this);
        btnReject = (Button) findViewById(R.id.btn_reject);
        btnReject.setOnClickListener(this);
    }

    private void bindView() {
        user = (NearByListResponse.User) getIntent().getSerializableExtra(Constant.ARG_USERS);
        List<CodeValueResponse.CodeValue> codeValues = DatabaseManager.getInstance(this).getCodeValue();

        if (user != null) {
            for (CodeValueResponse.CodeValue cv : codeValues) {
//                if (MyApplication.getRequestStatusCode().containsKey(cv.getCodeTypeID())) {
                String code = MyApplication.getRequestStatusCode().get(cv.getCodeTypeID());
                switch (Utils.emptyIfNull(code)) {
                    case Constant.ACCEPT:
                        layLike.setVisibility(View.GONE);
                        layRequest.setVisibility(View.GONE);
                        actionButton.setVisibility(View.VISIBLE);
                        break;
                    case Constant.REJECT:
                        layLike.setVisibility(View.VISIBLE);
                        layRequest.setVisibility(View.GONE);
                        actionButton.setVisibility(View.GONE);
                        break;
                    case Constant.REQUEST_SENT:
                        btnLike.setText("Liked");
                        btnLike.setEnabled(false);
                        layLike.setVisibility(View.VISIBLE);
                        layRequest.setVisibility(View.GONE);
                        actionButton.setVisibility(View.GONE);
                        break;
                    case Constant.BLOCKED:
                        break;

                    default:
                        layLike.setVisibility(View.VISIBLE);
                        layRequest.setVisibility(View.GONE);
                        actionButton.setVisibility(View.GONE);
                }
//                }
            }


            String nameAge = Utils.emptyIfNull(user.getFirstName()) + " " + Utils.emptyIfNull(user.getLastName());
            if (!Utils.isEmpty(user.getAge()))
                nameAge = nameAge + ", " + Utils.emptyIfNull(user.getAge());
            txtNameAge.setText(nameAge);


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
                        user.getFireBaseToken());
                break;
            case R.id.btn_like:
                likeClicked();
                apiCallSetRequest(LIKE);
                break;
            case R.id.btn_accept:
                acceptClicked();
                break;
            case R.id.btn_reject:
                rejectClicked();
                break;
        }

    }

    private void likeClicked() {
        btnLike.setEnabled(false);
        btnLike.setText("Liked");
        btnLike.setTextColor(getResources().getColor(R.color.colorTextWhite));
        layLike.setBackground(getResources().getDrawable(R.drawable.btn_round_accent));
    }

    private void acceptClicked() {
        apiCallSetRequest(ACCEPT);
    }

    private void rejectClicked() {
        apiCallSetRequest(REJECT);
    }

    private void apiCallSetRequest(int code) {

    }
}
