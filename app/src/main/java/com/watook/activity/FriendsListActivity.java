package com.watook.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.watook.R;
import com.watook.adapter.NearByAdapter;
import com.watook.manager.ApiManager;
import com.watook.manager.DatabaseManager;
import com.watook.model.response.UserListResponse;
import com.watook.util.Constant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsListActivity extends BaseActivity {

    RecyclerView recyclerView;
    TextView txtNoData;
    NearByAdapter nearByAdapter;
    FriendsListActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        setUpToolBar();
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
        List<UserListResponse.UserList> usrList = DatabaseManager.getInstance(activity).getUsersList();
        if (usrList != null) {
            setData(usrList);
            apiCallGetUserList();
        } else {
            apiCallGetUserList();
        }
    }

    private void apiCallGetUserList() {
        Call<UserListResponse> codeValue = ApiManager.getApiInstance().getUserList(Constant.CONTENT_TYPE,
                DatabaseManager.getInstance(activity).getRegistrationData().getData());
        codeValue.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserListResponse> call, @NonNull Response<UserListResponse> response) {
                int statusCode = response.code();
                UserListResponse codeValueResponse = response.body();
                if (statusCode == 200 && codeValueResponse != null && codeValueResponse.getStatus() != null && codeValueResponse.getStatus().equalsIgnoreCase("success")) {
                    DatabaseManager.getInstance(activity).insertUsersList(codeValueResponse.getData());
                    setData(codeValueResponse.getData());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserListResponse> call, @NonNull Throwable t) {
                activity.showAToast(getResources().getString(R.string.oops_something_went_wrong));
            }
        });
    }

    private void setData(List<UserListResponse.UserList> data) {
        DividerItemDecoration itemDecorator = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(getResources().getDrawable(R.drawable.divider));

        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(activity,
                DividerItemDecoration.VERTICAL));
        nearByAdapter = new NearByAdapter(activity, data);
        recyclerView.setAdapter(nearByAdapter);
    }

}
