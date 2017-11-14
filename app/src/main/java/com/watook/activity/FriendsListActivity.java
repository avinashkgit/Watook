package com.watook.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.watook.R;
import com.watook.adapter.FriendsListAdapter;
import com.watook.application.MyApplication;
import com.watook.manager.ApiManager;
import com.watook.manager.DatabaseManager;
import com.watook.model.response.ConnectionsResponse;
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
        List<ConnectionsResponse.User> usrList = DatabaseManager.getInstance(activity).getConncetions();
        if (usrList != null) {
            setData(usrList);
            apiCallGetFriendsList();
        } else {
            apiCallGetFriendsList();
        }
    }

    private void apiCallGetFriendsList() {
        Call<ConnectionsResponse> codeValue = ApiManager.getApiInstance().getFriendsList(Constant.CONTENT_TYPE,
                DatabaseManager.getInstance(activity).getRegistrationData().getData(), MyApplication.getInstance().getUserId());
        codeValue.enqueue(new Callback<ConnectionsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ConnectionsResponse> call, @NonNull Response<ConnectionsResponse> response) {
                int statusCode = response.code();
                ConnectionsResponse connectionsResponse = response.body();
                if (statusCode == 200 && connectionsResponse != null && connectionsResponse.getStatus() != null && connectionsResponse.getStatus().equalsIgnoreCase("success")) {
                    DatabaseManager.getInstance(activity).insertConnections(connectionsResponse.getData());
                    setData(connectionsResponse.getData());
                }  else {
                    activity.showAToast(getResources().getString(R.string.oops_something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ConnectionsResponse> call, @NonNull Throwable t) {
                activity.showAToast(getResources().getString(R.string.oops_something_went_wrong));
            }
        });
    }

    private void setData(List<ConnectionsResponse.User> data) {
        if(data.size() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new DividerItemDecorator(getResources().getDrawable(R.drawable.divider)));
            friendsListAdapter = new FriendsListAdapter(activity, data);
            recyclerView.setAdapter(friendsListAdapter);
        } else{
            txtNoData.setVisibility(View.VISIBLE);
        }
    }

}
