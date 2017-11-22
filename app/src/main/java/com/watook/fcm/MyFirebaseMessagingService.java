package com.watook.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.watook.R;
import com.watook.activity.ChatActivity;
import com.watook.application.MyApplication;
import com.watook.events.PushNotificationEvent;
import com.watook.manager.ApiManager;
import com.watook.manager.DatabaseManager;
import com.watook.model.UserChat;
import com.watook.model.response.ConnectionsResponse;
import com.watook.util.Constant;
import com.watook.util.FirebaseChatMainApp;
import com.watook.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    RemoteMessage remoteMessage;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        this.remoteMessage = remoteMessage;

        if (remoteMessage.getData().size() > 0) {
            String profilePic = DatabaseManager.getInstance(MyApplication.getContext()).getProfilePic(Long.parseLong(remoteMessage.getData().get("uid")));
            if (profilePic != null) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());

                String title = remoteMessage.getData().get("title");
                String message = remoteMessage.getData().get("text");
                String username = remoteMessage.getData().get("username");
                String uid = remoteMessage.getData().get("uid");
                String fcmToken = remoteMessage.getData().get("fcm_token");
                Long time = Long.parseLong(remoteMessage.getData().get("time"));




                // Don't show notification if chat activity is open.
                if (!Utils.emptyIfNull(FirebaseChatMainApp.getReceiverId()).equals(uid)) {

                    UserChat userChat;
                    userChat = DatabaseManager.getInstance(MyApplication.getContext()).getUserChat(Long.parseLong(uid));
                    if (userChat != null) {
                        userChat.setHasNewMessage(true);
                        userChat.setSentById(Long.parseLong(uid));
                        userChat.setLastMessage(message);
                        userChat.setLastModified(time);
                        userChat.setProfileImage(profilePic);
                        userChat.setHasNewMessage(false);
                        if (userChat.getMessageCount() == null)
                            userChat.setMessageCount(1);
                        else
                            userChat.setMessageCount(userChat.getMessageCount() + 1);
                        DatabaseManager.getInstance(MyApplication.getContext()).insertUserChat(userChat);
                    } else {
                        userChat = new UserChat();
                        userChat.setLastModified(time);
                        userChat.setSentById(Long.parseLong(uid));
                        userChat.setUserId(Long.parseLong(uid));
                        userChat.setName(username);
                        userChat.setLastMessage(message);
                        userChat.setMessageCount(1);
                        userChat.setProfileImage(profilePic);
                        userChat.setFireBaseToken(fcmToken);
                        userChat.setHasNewMessage(true);
                        DatabaseManager.getInstance(MyApplication.getContext()).insertUserChat(userChat);
                    }
                    broadcastEvent();




                    sendNotification(title, message, username, uid, fcmToken);
                } else {
                    EventBus.getDefault().post(new PushNotificationEvent(title,
                            message,
                            username,
                            uid,
                            fcmToken));
                }
            } else {
                apiCallGetFriendsList();
            }
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void sendNotification(String title,
                                  String message,
                                  String receiver,
                                  String receiverUid,
                                  String firebaseToken) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constant.ARG_RECEIVER, receiver);
        intent.putExtra(Constant.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constant.ARG_FIREBASE_TOKEN, firebaseToken);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_messaging)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(m, notificationBuilder.build());
    }


    private void apiCallGetFriendsList() {
        Call<ConnectionsResponse> codeValue = ApiManager.getApiInstance().getFriendsList(Constant.CONTENT_TYPE,
                DatabaseManager.getInstance(MyApplication.getContext()).getRegistrationData().getData(), MyApplication.getInstance().getUserId());
        codeValue.enqueue(new Callback<ConnectionsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ConnectionsResponse> call, @NonNull Response<ConnectionsResponse> response) {
                int statusCode = response.code();
                ConnectionsResponse connectionsResponse = response.body();
                if (statusCode == 200 && connectionsResponse != null && connectionsResponse.getStatus() != null && connectionsResponse.getStatus().equalsIgnoreCase("success")) {
                    DatabaseManager.getInstance(MyApplication.getContext()).insertConnections(connectionsResponse.getData());
                    onMessageReceived(remoteMessage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ConnectionsResponse> call, @NonNull Throwable t) {
            }
        });
    }

    private void broadcastEvent() {
        Intent intent = new Intent(Constant.BROADCAST_RESULT);
        intent.putExtra(Constant.NEW_MESSAGE, Constant.NEW_MESSAGE);
        LocalBroadcastManager.getInstance(MyApplication.getContext()).sendBroadcast(intent);
    }
}