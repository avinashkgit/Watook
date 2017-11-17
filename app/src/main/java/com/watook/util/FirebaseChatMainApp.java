package com.watook.util;



public class FirebaseChatMainApp {
    private static boolean sIsChatActivityOpen = false;
    static String receiverId;

    public static boolean isChatActivityOpen() {
        return sIsChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        FirebaseChatMainApp.sIsChatActivityOpen = isChatActivityOpen;
    }

    public static String getReceiverId() {
        return receiverId;
    }

    public static void setReceiverId(String receiverId) {
        FirebaseChatMainApp.receiverId = receiverId;
    }
}