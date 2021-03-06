package com.watook.core.chat;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.watook.application.MySharedPreferences;
import com.watook.fcm.FcmNotificationBuilder;
import com.watook.model.Chat;
import com.watook.util.Constant;
import com.watook.util.Keys;

/**
 * Author: Kartik Sharma
 * Created on: 9/2/2016 , 10:08 PM
 * Project: FirebaseChat
 */

public class ChatInteractor implements ChatContract.Interactor {
    private static final String TAG = "ChatInteractor";

    private ChatContract.OnSendMessageListener mOnSendMessageListener;
    private ChatContract.OnGetMessagesListener mOnGetMessagesListener;
    private ChatContract.OnNoRoomFoundListener mOnNoRoomFoundListener;


    public ChatInteractor(ChatContract.OnSendMessageListener onSendMessageListener) {
        this.mOnSendMessageListener = onSendMessageListener;
    }

    public ChatInteractor(ChatContract.OnGetMessagesListener onGetMessagesListener) {
        this.mOnGetMessagesListener = onGetMessagesListener;
    }

    public ChatInteractor(ChatContract.OnSendMessageListener onSendMessageListener,
                          ChatContract.OnGetMessagesListener onGetMessagesListener, ChatContract.OnNoRoomFoundListener onNoRoomFoundListener) {
        this.mOnSendMessageListener = onSendMessageListener;
        this.mOnGetMessagesListener = onGetMessagesListener;
        this.mOnNoRoomFoundListener = onNoRoomFoundListener;
    }

    @Override
    public void sendMessageToFirebaseUser(final Context context, final Chat chat, final String receiverFirebaseToken) {
        String room;
        if (Long.parseLong(chat.receiverUid) > Long.parseLong(chat.senderUid)) {
            room = chat.senderUid + "_" + chat.receiverUid;
        } else{
            room = chat.receiverUid + "_" + chat.senderUid;
        }
        final String finalRoom = room;
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(Constant.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(finalRoom)) {
                    Log.e(TAG, "sendMessageToFirebaseUser: " + finalRoom + " exists");
                    databaseReference.child(Constant.ARG_CHAT_ROOMS).child(finalRoom).child(String.valueOf(chat.timestamp)).setValue(chat);
                }

                else {
                    Log.e(TAG, "sendMessageToFirebaseUser: success");
                    databaseReference.child(Constant.ARG_CHAT_ROOMS).child(finalRoom).child(String.valueOf(chat.timestamp)).setValue(chat);
                    if (Long.parseLong(chat.receiverUid) > Long.parseLong(chat.senderUid)) {
                        getMessageFromFirebaseUser(chat.senderUid, chat.receiverUid);
                    } else{
                        getMessageFromFirebaseUser(chat.receiverUid, chat.senderUid);
                    }

                }
                //end push notification to the receiver
                try {
                    sendPushNotificationToReceiver(chat.sender,
                            chat.message,
                            chat.senderUid,
                            (String) MySharedPreferences.getObject(Constant.ARG_FIREBASE_TOKEN),
                            receiverFirebaseToken, String.valueOf(chat.timestamp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mOnSendMessageListener.onSendMessageSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnSendMessageListener.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });
    }

    private void sendPushNotificationToReceiver(String username,
                                                String message,
                                                String uid,
                                                String firebaseToken,
                                                String receiverFirebaseToken, String timeStamp) {
        FcmNotificationBuilder.initialize()
                .title(username)
                .message(message)
                .username(username)
                .uid(uid)
                .firebaseToken(firebaseToken)
                .receiverFirebaseToken(receiverFirebaseToken)
                .time(timeStamp)
                .send();
    }

    @Override
    public void getMessageFromFirebaseUser(String senderUid, String receiverUid) {
        String room;
        if (Long.parseLong(receiverUid) > Long.parseLong(senderUid)) {
            room = senderUid + "_" + receiverUid;
        } else{
            room = receiverUid + "_" + senderUid;
        }
        final String finalRoom = room;

//        final String room_type_1 = senderUid + "_" + receiverUid;
//        final String room_type_2 = receiverUid + "_" + senderUid;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(Constant.ARG_CHAT_ROOMS).child(finalRoom).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + finalRoom + " exists");
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constant.ARG_CHAT_ROOMS)
                            .child(finalRoom).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            mOnGetMessagesListener.onGetMessagesSuccess(chat);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }
                    });
                }
//                else if (dataSnapshot.hasChild(room_type_2)) {
//                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_2 + " exists");
//                    FirebaseDatabase.getInstance()
//                            .getReference()
//                            .child(Constant.ARG_CHAT_ROOMS)
//                            .child(room_type_2).addChildEventListener(new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                            Chat chat = dataSnapshot.getValue(Chat.class);
//                            mOnGetMessagesListener.onGetMessagesSuccess(chat);
//                        }
//
//                        @Override
//                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
//                        }
//                    });
//                }
                else {
                    Log.e(TAG, "getMessageFromFirebaseUser: no such room available");
                    mOnNoRoomFoundListener.onNoRoomFound(Constant.NO_ROOM_FOUND);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });
    }
}
