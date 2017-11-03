package com.watook.fragment;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.watook.R;
import com.watook.activity.ChatActivity;
import com.watook.adapter.ChatRecyclerAdapter;
import com.watook.application.MyApplication;
import com.watook.core.chat.ChatContract;
import com.watook.core.chat.ChatPresenter;
import com.watook.events.PushNotificationEvent;
import com.watook.model.Chat;
import com.watook.util.Constant;
import com.watook.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;


public class ChatFragment extends Fragment implements ChatContract.View, TextView.OnEditorActionListener {
    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;

    private ProgressBar progressBar;
    private FloatingActionButton fabSend;

    private ChatRecyclerAdapter mChatRecyclerAdapter;

    private ChatPresenter mChatPresenter;
    private RelativeLayout layStart;
    ChatActivity activity;

    public static ChatFragment newInstance(String receiver,
                                           String receiverUid,
                                           String firebaseToken) {
        Bundle args = new Bundle();
        args.putString(Constant.ARG_RECEIVER, receiver);
        args.putString(Constant.ARG_RECEIVER_UID, receiverUid);
        args.putString(Constant.ARG_FIREBASE_TOKEN, firebaseToken);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_chat, container, false);
        bindViews(fragmentView);
        activity = (ChatActivity) getActivity();
        return fragmentView;
    }

    private void bindViews(View view) {
        layStart = (RelativeLayout) view.findViewById(R.id.lay_start);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mRecyclerViewChat = (RecyclerView) view.findViewById(R.id.recycler_view_chat);
        mETxtMessage = (EditText) view.findViewById(R.id.edit_text_message);
        mETxtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Utils.isEmpty(mETxtMessage.getText().toString().trim())) {
                    fabSend.setEnabled(true);
                    fabSend.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorFab)));

                } else {
                    fabSend.setEnabled(false);
                    fabSend.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ColorFabDisabled)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fabSend = (FloatingActionButton) view.findViewById(R.id.action_button_send);
        fabSend.setEnabled(false);
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkAvailable()) {
                    if (!Utils.isEmpty(mETxtMessage.getText().toString()))
                        sendMessage();
                } else
                    activity.showAToast("Internet Connection not available!");

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mETxtMessage.setOnEditorActionListener(this);
        mChatPresenter = new ChatPresenter(this);
        mChatPresenter.getMessage(MyApplication.getInstance().getUserId(),
                getArguments().getString(Constant.ARG_RECEIVER_UID));


    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if (actionId == EditorInfo.IME_ACTION_SEND) {
//            sendMessage();
//            return true;
//        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_chat, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_clear_all) {
            deleteConversation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteConversation() {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constant.ARG_CHAT_ROOMS)
                .child(getArguments().getString(Constant.ARG_RECEIVER_UID) + "_" + MyApplication.getInstance().getUserId()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                mChatRecyclerAdapter.deleteAll();
                layStart.setVisibility(View.VISIBLE);
            }
        });

        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constant.ARG_CHAT_ROOMS)
                .child(MyApplication.getInstance().getUserId() + "_" + getArguments().getString(Constant.ARG_RECEIVER_UID)).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                mChatRecyclerAdapter.deleteAll();
                layStart.setVisibility(View.VISIBLE);
            }
        });
    }

    private void sendMessage() {
        String message = mETxtMessage.getText().toString();
        String receiver = getArguments().getString(Constant.ARG_RECEIVER);
        String receiverUid = getArguments().getString(Constant.ARG_RECEIVER_UID);
        String sender = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
//        String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String senderUid = MyApplication.getInstance().getUserId();
        String receiverFirebaseToken = getArguments().getString(Constant.ARG_FIREBASE_TOKEN);
        Chat chat = new Chat(sender,
                receiver,
                senderUid,
                receiverUid,
                message,
                System.currentTimeMillis());
        mChatPresenter.sendMessage(getActivity().getApplicationContext(),
                chat,
                receiverFirebaseToken);
    }

    @Override
    public void onSendMessageSuccess() {
        mETxtMessage.setText("");
//        Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendMessageFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onGetMessagesSuccess(Chat chat) {
        progressBar.setVisibility(View.GONE);
        layStart.setVisibility(View.GONE);
        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new ChatRecyclerAdapter(new ArrayList<Chat>());
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
        }
        mChatRecyclerAdapter.add(chat);
        mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
    }

    @Override
    public void onNoRoomFound(String s) {
        if (s.equals(Constant.NO_ROOM_FOUND)) {
            progressBar.setVisibility(View.GONE);
            layStart.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetMessagesFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onPushNotificationEvent(PushNotificationEvent pushNotificationEvent) {
        if (mChatRecyclerAdapter == null || mChatRecyclerAdapter.getItemCount() == 0) {
//            mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
            mChatPresenter.getMessage(MyApplication.getInstance().getUserId(),
                    pushNotificationEvent.getUid());
        }
    }
}
