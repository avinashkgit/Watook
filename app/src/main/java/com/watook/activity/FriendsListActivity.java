package com.watook.activity;

import android.os.Bundle;

import com.watook.R;

public class FriendsListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        setUpToolBar();
    }

}
