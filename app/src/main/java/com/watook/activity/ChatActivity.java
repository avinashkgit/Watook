package com.watook.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.watook.R;
import com.watook.fragment.ChatFragment;
import com.watook.manager.DatabaseManager;
import com.watook.util.Constant;
import com.watook.util.FirebaseChatMainApp;
import com.watook.util.Utils;


public class ChatActivity extends BaseActivity {
    TextView txtName;
    ImageView imgProfile;
    Long othersId;

    public static void startActivity(Context context, String receiver, String receiverUid, String firebaseToken, String profileImage) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constant.ARG_RECEIVER, receiver);
        intent.putExtra(Constant.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constant.ARG_FIREBASE_TOKEN, firebaseToken);
        intent.putExtra(Constant.PROFILE_IMAGE, profileImage);
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
                        getIntent().getExtras().getString(Constant.ARG_FIREBASE_TOKEN), getIntent().getExtras().getString(Constant.PROFILE_IMAGE)),
                ChatFragment.class.getSimpleName());
        fragmentTransaction.commit();
        imgProfile = (ImageView) findViewById(R.id.profile_pic);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity.this, UserProfileActivity.class);
                i.putExtra(Constant.OTHERS_ID, Long.parseLong(getIntent().getExtras().getString(Constant.ARG_RECEIVER_UID)));
                startActivity(i);
            }
        });
        Glide.with(this)
                .load(Utils.emptyIfNull(DatabaseManager.getInstance(this).getProfilePic(Long.parseLong(getIntent().getExtras().getString(Constant.ARG_RECEIVER_UID)))))
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(imgProfile) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imgProfile.setImageDrawable(circularBitmapDrawable);
            }
        });
        txtName = (TextView) findViewById(R.id.txt_name);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseChatMainApp.setChatActivityOpen(true);
        FirebaseChatMainApp.setReceiverId(getIntent().getExtras().getString(Constant.ARG_RECEIVER_UID));

    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseChatMainApp.setChatActivityOpen(false);
        FirebaseChatMainApp.setReceiverId(null);

    }
}
