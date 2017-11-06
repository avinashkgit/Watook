package com.watook.manager;

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


}
