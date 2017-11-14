package com.watook.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.watook.R;
import com.watook.activity.MainActivity;
import com.watook.adapter.MyLikesAdapter;
import com.watook.adapter.NearByAdapter;
import com.watook.application.MyApplication;
import com.watook.manager.ApiManager;
import com.watook.manager.DatabaseManager;
import com.watook.model.response.ConnectionsResponse;
import com.watook.model.response.MyLikesResponse;
import com.watook.util.Constant;
import com.watook.util.DividerItemDecorator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LikesFragment extends Fragment {
    MainActivity activity;
    RecyclerView recyclerView;
    TextView txtNoData;
    MyLikesAdapter myLikesAdapter;
    ProgressBar progressBar;

    public static LikesFragment newInstance() {
        return new LikesFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    private void inItView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_likes);
        txtNoData = (TextView) view.findViewById(R.id.txt_no_data_found);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        bindUi();

    }

    private void bindUi() {
        apiCallGetFriendsList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_likes, container, false);
        inItView(v);
        return v;
    }


    private void apiCallGetFriendsList() {
        Call<MyLikesResponse> codeValue = ApiManager.getApiInstance().getRequests(Constant.CONTENT_TYPE,
                DatabaseManager.getInstance(activity).getRegistrationData().getData(), MyApplication.getInstance().getUserId());
        codeValue.enqueue(new Callback<MyLikesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyLikesResponse> call, @NonNull Response<MyLikesResponse> response) {
                int statusCode = response.code();
                MyLikesResponse myLikesResponse = response.body();
                if (statusCode == 200 && myLikesResponse != null && myLikesResponse.getStatus() != null && myLikesResponse.getStatus().equalsIgnoreCase("success")) {
                    setData(myLikesResponse.getData());
                } else {
                    activity.showAToast(getResources().getString(R.string.oops_something_went_wrong));
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(@NonNull Call<MyLikesResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                activity.showAToast(getResources().getString(R.string.oops_something_went_wrong));
            }
        });
    }

    private void setData(List<MyLikesResponse.User> data) {
        progressBar.setVisibility(View.GONE);
        if (data.size() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new DividerItemDecorator(getResources().getDrawable(R.drawable.divider)));
            myLikesAdapter = new MyLikesAdapter(activity, data);
            recyclerView.setAdapter(myLikesAdapter);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
        }
    }


}
