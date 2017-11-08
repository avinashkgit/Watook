package com.watook.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.watook.R;
import com.watook.fragment.ChatFragment;
import com.watook.util.Constant;
import com.watook.util.FirebaseChatMainApp;


public class ChatActivity extends BaseActivity {
    TextView txtName;
    ImageView imgProgile;

    public static void startActivity(Context context,
                                     String receiver,
                                     String receiverUid,
                                     String firebaseToken) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constant.ARG_RECEIVER, receiver);
        intent.putExtra(Constant.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constant.ARG_FIREBASE_TOKEN, firebaseToken);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setUpToolBar();
        init();
        bindView();
    }

    private void bindView() {
        txtName.setText(getIntent().getExtras().getString(Constant.ARG_RECEIVER));
    }

    private void init() {
        // set the register screen fragment
//        getSupportActionBar().setTitle(getIntent().getExtras().getString(Constant.ARG_RECEIVER));
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_chat,
                ChatFragment.newInstance(getIntent().getExtras().getString(Constant.ARG_RECEIVER),
                        getIntent().getExtras().getString(Constant.ARG_RECEIVER_UID),
                        getIntent().getExtras().getString(Constant.ARG_FIREBASE_TOKEN)),
                ChatFragment.class.getSimpleName());
        fragmentTransaction.commit();
        imgProgile = (ImageView) findViewById(R.id.profile_pic);
        txtName = (TextView) findViewById(R.id.txt_name);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseChatMainApp.setChatActivityOpen(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseChatMainApp.setChatActivityOpen(false);
    }
}
