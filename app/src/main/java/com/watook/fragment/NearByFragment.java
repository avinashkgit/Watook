package com.watook.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.watook.R;
import com.watook.activity.MainActivity;
import com.watook.adapter.NearByAdapter;
import com.watook.application.MyApplication;
import com.watook.application.MySharedPreferences;
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
    ProgressBar progressBar;
    RelativeLayout layStart;


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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindUi();
    }

    private void inItView(View view) {
        activity = (MainActivity) getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_near_by);
        txtNoData = (TextView) view.findViewById(R.id.txt_no_data_found);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        layStart = (RelativeLayout) view.findViewById(R.id.lay_start);
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean prefChanged = false;
        try {
            prefChanged = (boolean) MySharedPreferences.getObject(Constant.IS_PREFERENCES_CHANGED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (prefChanged) {
            apiCallGetUserList();
            try {
                MySharedPreferences.putObject(Constant.IS_PREFERENCES_CHANGED, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

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
        progressBar.setVisibility(View.VISIBLE);
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
                } else {
                    activity.showAToast(getResources().getString(R.string.oops_something_went_wrong));
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NearByListResponse> call, @NonNull Throwable t) {
                activity.showAToast(getResources().getString(R.string.oops_server_response_failure));
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setData(List<NearByListResponse.User> data) {
        if (data.size() <= 0) {
            layStart.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }
        recyclerView.setVisibility(View.VISIBLE);
        layStart.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
//        recyclerView.addItemDecoration(new DividerItemDecorator(getResources().getDrawable(R.drawable.divider)));
        nearByAdapter = new NearByAdapter(activity, data);
        recyclerView.setAdapter(nearByAdapter);
        progressBar.setVisibility(View.GONE);


    }
}
