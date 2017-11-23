package com.watook.fragment;


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
import com.watook.adapter.BlockedAdapter;
import com.watook.adapter.MyLikesAdapter;
import com.watook.application.MyApplication;
import com.watook.manager.ApiManager;
import com.watook.manager.DatabaseManager;
import com.watook.model.response.ConnectionTypeResponse;
import com.watook.util.Constant;
import com.watook.util.DividerItemDecorator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlockedFragment extends Fragment {
    MainActivity activity;
    RecyclerView recyclerView;
    TextView txtNoData;
    BlockedAdapter blockedAdapter;
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
        apiCallGetBlockedList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blocked, container, false);
        inItView(v);
        return v;
    }


    private void apiCallGetBlockedList() {
        Call<ConnectionTypeResponse> codeValue = ApiManager.getApiInstance().getRequests(Constant.CONTENT_TYPE,
                DatabaseManager.getInstance(activity).getRegistrationData().getData(), MyApplication.getInstance().getUserId(), MyApplication.getRequestStatusCode().get(Constant.BLOCKED).toString());
        codeValue.enqueue(new Callback<ConnectionTypeResponse>() {
            @Override
            public void onResponse(@NonNull Call<ConnectionTypeResponse> call, @NonNull Response<ConnectionTypeResponse> response) {
                int statusCode = response.code();
                ConnectionTypeResponse connectionTypeResponse = response.body();
                if (statusCode == 200 && connectionTypeResponse != null && connectionTypeResponse.getStatus() != null && connectionTypeResponse.getStatus().equalsIgnoreCase("success")) {
                    setData(connectionTypeResponse.getData());
                    for(ConnectionTypeResponse.User user : connectionTypeResponse.getData()) {
                        DatabaseManager.getInstance(activity).insertBlocked(user);
                    }

                } else {
                    activity.showAToast(getResources().getString(R.string.oops_something_went_wrong));
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(@NonNull Call<ConnectionTypeResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                activity.showAToast(getResources().getString(R.string.oops_something_went_wrong));
            }
        });
    }

    private void setData(List<ConnectionTypeResponse.User> data) {
        progressBar.setVisibility(View.GONE);
        if (data.size() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new DividerItemDecorator(getResources().getDrawable(R.drawable.divider)));
            blockedAdapter = new BlockedAdapter(activity, data);
            recyclerView.setAdapter(blockedAdapter);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
        }
    }


}
