package com.watook.manager;

import com.watook.util.Constant;

/**
 * Created by Avinash kumar on 10/01/2017.
 */
public class DatabaseConstants {


    static final String TABLE_USER_PROFILE = "user_profile";
    static final String PROFILE_RESPONSE = "profile_response";

    static final String TABLE_REGISTRATION = "registration";
    static final String REGISTRATION_RESPONSE = "registration_response";

    static final String TABLE_CODE_VALUE = "code_value";
    static final String CODE_VALUE_RESPONSE = "code_value_response";

    static final String TABLE_NEARBY_USERS = "nearby_users";
    static final String NEARBY_USERS_RESPONSE = "nearby_users_response";

    static final String TABLE_PREFERENCES = "preferences";
    static final String PREFERENCES_RESPONSE = "preferences_response";

    static final String TABLE_USER_CHAT = "user_chat";
    static final String USER_CHAT_RESPONSE = "user_chat_response";

    static final String TABLE_CONNECTIONS = "connections";
    static final String CONNECTIONS_RESPONSE = "connections_response";

    ////////////////

    static final String USER_CHAT_ID = "USER_CHAT_ID";
    static final String USER_CHAT_USER_ID = "USER_ID";
    static final String USER_CHAT_NAME = "NAME";
    static final String USER_CHAT_STATUS_INFO = "STATUS_INFO";
    static final String USER_CHAT_PROFILE_IMAGE = "PROFILE_IMAGE";
    static final String USER_CHAT_FIREBASE_TOKEN = "FIREBASE_TOKEN";
    static final String USER_CHAT_MESSAGE_COUNT = "MESSAGE_COUNT";
    static final String USER_CHAT_LAST_MESSAGE = "LAST_MESSAGE";
    static final String USER_CHAT_LAST_MODIFIED = "LAST_MODIFIED";
    static final String USER_CHAT_SENT_BY_ID = "SENT_BY_ID";
    static final String USER_CHAT_HAS_NEW_MESSAGE = "HAS_NEW_MESSAGE";

    /////////////////////////////////////////////////////////////////

    static final String CREATE_TABLE_USER_PROFILE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_PROFILE
            + " (" + PROFILE_RESPONSE + " BLOB" + " )";


    static final String CREATE_TABLE_REGISTRATION = "CREATE TABLE IF NOT EXISTS " + TABLE_REGISTRATION
            + " (" + REGISTRATION_RESPONSE + " BLOB" + " )";


    static final String CREATE_TABLE_CODE_VALUE = "CREATE TABLE IF NOT EXISTS " + TABLE_CODE_VALUE
            + " (" + CODE_VALUE_RESPONSE + " BLOB" + " )";


    static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + TABLE_NEARBY_USERS
            + " (" + NEARBY_USERS_RESPONSE + " BLOB" + " )";


    static final String CREATE_TABLE_PREFERENCES = "CREATE TABLE IF NOT EXISTS " + TABLE_PREFERENCES
            + " (" + PREFERENCES_RESPONSE + " BLOB" + " )";


//    static final String CREATE_TABLE_USER_CHAT = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_CHAT
//            + " (" +
//            USER_CHAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            USER_CHAT_USER_ID + " INTEGER, " +
//            USER_CHAT_NAME + " TEXT, " +
//            USER_CHAT_STATUS_INFO + " INTEGER, " +
//            USER_CHAT_PROFILE_IMAGE + " TEXT, " +
//            USER_CHAT_FIREBASE_TOKEN + " TEXT, " +
//            USER_CHAT_MESSAGE_COUNT + " INTEGER " +
//            USER_CHAT_LAST_MESSAGE + " TEXT, " +
//            USER_CHAT_LAST_MODIFIED + " INTEGER " +
//            USER_CHAT_SENT_BY_ID + " INTEGER " +
//            USER_CHAT_HAS_NEW_MESSAGE + " INTEGER " +
//            "UNIQUE KEY" + USER_CHAT_USER_ID + "( " + USER_CHAT_USER_ID +" )" +
//            ") ";


    static final String CREATE_TABLE_USER_CHAT = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_CHAT
            + " (" +
            USER_CHAT_USER_ID + " INTEGER, " +
            USER_CHAT_RESPONSE + " BLOB" +
            " )";


    static final String CREATE_TABLE_CONNECTIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_CONNECTIONS
            + " (" + CONNECTIONS_RESPONSE + " BLOB" + " )";


}
