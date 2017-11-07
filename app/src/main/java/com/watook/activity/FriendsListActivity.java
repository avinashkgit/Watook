package com.watook.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.watook.R;
import com.watook.adapter.FriendsListAdapter;
import com.watook.application.MyApplication;
import com.watook.manager.ApiManager;
import com.watook.manager.DatabaseManager;
import com.watook.model.response.NearByListResponse;
import com.watook.util.Constant;
import com.watook.util.DividerItemDecorator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsListActivity extends BaseActivity {

    RecyclerView recyclerView;
    TextView txtNoData;
    FriendsListAdapter friendsListAdapter;
    FriendsListActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        setUpToolBar();
        getSupportActionBar().setTitle("Friends");
        inItView();
    }

    private void inItView() {
        activity = this;
        recyclerView = (RecyclerView) findViewById(R.id.rv_near_by);
        txtNoData = (TextView) findViewById(R.id.txt_no_data_found);
    }

    @Override
    public void onResume() {
        super.onResume();
        bindUi();
    }

    private void bindUi() {
        List<NearByListResponse.User> usrList = DatabaseManager.getInstance(activity).getUsersList();
        if (usrList != null) {
            setData(usrList);
            apiCallGetUserList();
        } else {
            apiCallGetUserList();
        }
    }

    private void apiCallGetUserList() {
        Call<NearByListResponse> codeValue = ApiManager.getApiInstance().getNearByList(Constant.CONTENT_TYPE,
                DatabaseManager.getInstance(activity).getRegistrationData().getData(), MyApplication.getInstance().getUserId());
        codeValue.enqueue(new Callback<NearByListResponse>() {
            @Override
            public void onResponse(@NonNull Call<NearByListResponse> call, @NonNull Response<NearByListResponse> response) {
                int statusCode = response.code();
                NearByListResponse codeValueResponse = response.body();
                if (statusCode == 200 && codeValueResponse != null && codeValueResponse.getStatus() != null && codeValueResponse.getStatus().equalsIgnoreCase("success")) {
                    DatabaseManager.getInstance(activity).insertNearByUsersList(codeValueResponse.getData());
                    setData(codeValueResponse.getData());
                }
            }

            @Override
            public void onFailure(@NonNull Call<NearByListResponse> call, @NonNull Throwable t) {
                activity.showAToast(getResources().getString(R.string.oops_something_went_wrong));
            }
        });
    }

    private void setData(List<NearByListResponse.User> data) {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecorator(getResources().getDrawable(R.drawable.divider)));
        friendsListAdapter = new FriendsListAdapter(activity, data);
        recyclerView.setAdapter(friendsListAdapter);
    }

}
