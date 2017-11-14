package com.watook.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.watook.adapter.UserChatListAdapter;
import com.watook.manager.DatabaseManager;
import com.watook.model.User;
import com.watook.model.UserChat;
import com.watook.util.DividerItemDecorator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatsListFragment extends Fragment {


    public static ChatsListFragment newInstance() {
        return new ChatsListFragment();
    }

    MainActivity activity;
    RecyclerView recyclerView;
    RelativeLayout layNoData;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_users_list, container, false);
        inItView(v);
        return v;
    }

    private void inItView(View v) {
        activity = (MainActivity) getActivity();
        layNoData = (RelativeLayout) v.findViewById(R.id.lay_start);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_chat_list);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        bindView();
    }

    private void bindView() {
        HashMap<Long, UserChat> map = DatabaseManager.getInstance(activity).getUserChats();
        List<UserChat> lstChat = new ArrayList<>();
        if (map != null && map.size() > 0) {
            for (Map.Entry<Long, UserChat> entry : map.entrySet()) {
                UserChat userChat = entry.getValue();
                lstChat.add(userChat);
            }
        }
        if (lstChat.size() > 0) {
            layNoData.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new DividerItemDecorator(getResources().getDrawable(R.drawable.divider)));
            UserChatListAdapter userChatListAdapter = new UserChatListAdapter(activity, lstChat);
            recyclerView.setAdapter(userChatListAdapter);
        } else {
            layNoData.setVisibility(View.VISIBLE);
        }
    }
}
