package com.watook.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.watook.R;
import com.watook.activity.MainActivity;
import com.watook.adapter.NearByAdapter;
import com.watook.application.MyApplication;
import com.watook.manager.ApiManager;
import com.watook.manager.DatabaseManager;
import com.watook.model.response.NearByListResponse;
import com.watook.model.response.UserListResponse;
import com.watook.util.Constant;
import com.watook.util.DividerItemDecorator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NearByFragment extends Fragment {
    RecyclerView recyclerView;
    TextView txtNoData;
    NearByAdapter nearByAdapter;
    MainActivity activity;


    public static NearByFragment newInstance() {
        return new NearByFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_near_by, container, false);
        inItView(v);
        return v;
    }

    private void inItView(View view) {
        activity = (MainActivity) getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_near_by);
        txtNoData = (TextView) view.findViewById(R.id.txt_no_data_found);
    }

    @Override
    public void onResume() {
        super.onResume();
        bindUi();

    }

    private void bindUi() {
        List<NearByListResponse.User> usrList = DatabaseManager.getInstance(getActivity()).getUsersList();
        if (usrList != null) {
//            setData(usrList);
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
                    DatabaseManager.getInstance(getActivity()).insertNearByUsersList(codeValueResponse.getData());
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
        nearByAdapter = new NearByAdapter(activity, data);
        recyclerView.setAdapter(nearByAdapter);
    }
}
