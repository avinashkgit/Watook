package com.watook.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.watook.application.MyApplication;
import com.watook.model.MyProfile;
import com.watook.model.Preferences;
import com.watook.model.UserChat;
import com.watook.model.response.CodeValueResponse;
import com.watook.model.response.ConnectionTypeResponse;
import com.watook.model.response.ConnectionsResponse;
import com.watook.model.response.NearByListResponse;
import com.watook.model.response.RegistrationResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


@SuppressWarnings("ALL")
public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "watook.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseManager instance;
    private static Cipher cipher;
    private final String DATABASE_PATH;
    private SQLiteDatabase database;

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Context mContext = context;
        //DATABASE_PATH = mContext.getFilesDir().getPath() + context.getPackageName() + "/databases/";
        DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
    }


    private void createTable(SQLiteDatabase db) {
        db.execSQL(DatabaseConstants.CREATE_TABLE_USER_PROFILE);
        db.execSQL(DatabaseConstants.CREATE_TABLE_REGISTRATION);
        db.execSQL(DatabaseConstants.CREATE_TABLE_CODE_VALUE);
        db.execSQL(DatabaseConstants.CREATE_TABLE_USERS);
        db.execSQL(DatabaseConstants.CREATE_TABLE_PREFERENCES);
        db.execSQL(DatabaseConstants.CREATE_TABLE_USER_CHAT);
        db.execSQL(DatabaseConstants.CREATE_TABLE_CONNECTIONS);
        db.execSQL(DatabaseConstants.CREATE_BLOCKED_USER);

    }

    public void clearTables() {
        database = this.getWritableDatabase();
        database.execSQL("delete from " + DatabaseConstants.TABLE_USER_PROFILE);
        database.execSQL("delete from " + DatabaseConstants.TABLE_REGISTRATION);
        database.execSQL("delete from " + DatabaseConstants.TABLE_CODE_VALUE);
        database.execSQL("delete from " + DatabaseConstants.TABLE_NEARBY_USERS);
        database.execSQL("delete from " + DatabaseConstants.TABLE_PREFERENCES);
        database.execSQL("delete from " + DatabaseConstants.TABLE_USER_CHAT);
        database.execSQL("delete from " + DatabaseConstants.TABLE_CONNECTIONS);
        database.execSQL("delete from " + DatabaseConstants.TABLE_BLOCKED);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

    }

    public void openDataBase() throws SQLException {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {

        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            createTable(db);
        }

    }

    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File file = new File(myPath);
            if (file.exists() && !file.isDirectory()) {
            }
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException ignored) {
        }
        if (checkDB != null) {
            checkDB.close();

        }
        return checkDB != null ? true : false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            createTable(db);
        }
    }

    public boolean checkIsDataAlreadyInDBorNot(String TableName,
                                               String dbfield, String fieldValue) {
        database = this.getWritableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue;
        Cursor cursor = database.rawQuery(Query, null);
        if (cursor == null) {
            return false;
        } else if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }

        return true;
    }


    public byte[] objToByte(Object tcpPacket) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(tcpPacket);
        //return encrypt(MyApplication.getInstance().getKey(), byteStream.toByteArray());
        return byteStream.toByteArray();
    }


    public Object byteToObj(byte[] bytes) throws Exception {
        //byte[] b = decrypt(MyApplication.getInstance().getKey(), bytes);
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objStream = new ObjectInputStream(byteStream);
        return objStream.readObject();
    }

    private byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }


    public void insertRegistrationData(RegistrationResponse myObject) {
        try {
            database = this.getWritableDatabase();
            database.execSQL("delete from " + DatabaseConstants.TABLE_REGISTRATION);
            final ContentValues values = new ContentValues();
            values.put(DatabaseConstants.REGISTRATION_RESPONSE, objToByte(myObject));
            database.insert(DatabaseConstants.TABLE_REGISTRATION, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public RegistrationResponse getRegistrationData() {
        Cursor cursor = null;
        byte[] blob;
        RegistrationResponse obj = null;
        try {
            cursor = database.query(DatabaseConstants.TABLE_REGISTRATION, new String[]{"*"}, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToLast();
                blob = cursor.getBlob(cursor.getColumnIndex(DatabaseConstants.REGISTRATION_RESPONSE));
                if (blob != null)
                    obj = (RegistrationResponse) byteToObj(blob);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return obj;
    }


    public void insertMyProfile(MyProfile myObject) {
        try {
            database = this.getWritableDatabase();
            database.execSQL("delete from " + DatabaseConstants.TABLE_USER_PROFILE);
            final ContentValues values = new ContentValues();
            values.put(DatabaseConstants.PROFILE_RESPONSE, objToByte(myObject));
            database.insert(DatabaseConstants.TABLE_USER_PROFILE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public MyProfile getMyProfile() {
        Cursor cursor = null;
        byte[] blob;
        MyProfile obj = null;
        try {
            cursor = database.query(DatabaseConstants.TABLE_USER_PROFILE, new String[]{"*"}, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToLast();
                blob = cursor.getBlob(cursor.getColumnIndex(DatabaseConstants.PROFILE_RESPONSE));
                if (blob != null)
                    obj = (MyProfile) byteToObj(blob);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return obj;
    }


    public void insertCodeValue(List<CodeValueResponse.CodeValue> lst) {
        try {
            database = this.getWritableDatabase();
            database.execSQL("delete from " + DatabaseConstants.TABLE_CODE_VALUE);
            final ContentValues values = new ContentValues();
            values.put(DatabaseConstants.CODE_VALUE_RESPONSE, objToByte(lst));
            database.insert(DatabaseConstants.TABLE_CODE_VALUE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public List<CodeValueResponse.CodeValue> getCodeValue() {
        Cursor cursor = null;
        byte[] blob;
        List<CodeValueResponse.CodeValue> obj = null;
        try {
            cursor = database.query(DatabaseConstants.TABLE_CODE_VALUE, new String[]{"*"}, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToLast();
                blob = cursor.getBlob(cursor.getColumnIndex(DatabaseConstants.CODE_VALUE_RESPONSE));
                if (blob != null)
                    obj = (List<CodeValueResponse.CodeValue>) byteToObj(blob);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return obj;
    }


    public void insertNearByUsersList(List<NearByListResponse.User> lst) {
        try {
            database = this.getWritableDatabase();
            database.execSQL("delete from " + DatabaseConstants.TABLE_NEARBY_USERS);
            final ContentValues values = new ContentValues();
            values.put(DatabaseConstants.NEARBY_USERS_RESPONSE, objToByte(lst));
            database.insert(DatabaseConstants.TABLE_NEARBY_USERS, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public List<NearByListResponse.User> getUsersList() {
        Cursor cursor = null;
        byte[] blob;
        List<NearByListResponse.User> obj = null;
        try {
            cursor = database.query(DatabaseConstants.TABLE_NEARBY_USERS, new String[]{"*"}, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToLast();
                blob = cursor.getBlob(cursor.getColumnIndex(DatabaseConstants.NEARBY_USERS_RESPONSE));
                if (blob != null)
                    obj = (List<NearByListResponse.User>) byteToObj(blob);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return obj;
    }


    public void insertPreferences(Preferences myObject) {
        try {
            database = this.getWritableDatabase();
            database.execSQL("delete from " + DatabaseConstants.TABLE_PREFERENCES);
            final ContentValues values = new ContentValues();
            values.put(DatabaseConstants.PREFERENCES_RESPONSE, objToByte(myObject));
            database.insert(DatabaseConstants.TABLE_PREFERENCES, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public Preferences getPreferences() {
        Cursor cursor = null;
        byte[] blob;
        Preferences obj = null;
        try {
            cursor = database.query(DatabaseConstants.TABLE_PREFERENCES, new String[]{"*"}, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToLast();
                blob = cursor.getBlob(cursor.getColumnIndex(DatabaseConstants.PREFERENCES_RESPONSE));
                if (blob != null)
                    obj = (Preferences) byteToObj(blob);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return obj;
    }


    public void insertUserChat(UserChat userChat) {
        try {
            database = this.getWritableDatabase();
            final ContentValues values = new ContentValues();
            values.put(DatabaseConstants.USER_CHAT_USER_ID, userChat.getUserId());
            values.put(DatabaseConstants.USER_CHAT_RESPONSE, objToByte(userChat));
            if (!CheckIsDataAlreadyInUserChat(userChat.getUserId())) {
                database.insert(DatabaseConstants.TABLE_USER_CHAT, null, values);
            } else {
                database.update(DatabaseConstants.TABLE_USER_CHAT, values,
                        DatabaseConstants.USER_CHAT_USER_ID + " = ? ", new String[]{String.valueOf(userChat.getUserId())});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean CheckIsDataAlreadyInUserChat(Long userId) {
        String selectQuery = "select * from " + DatabaseConstants.TABLE_USER_CHAT + " where " +
                DatabaseConstants.USER_CHAT_USER_ID + "='" + userId + "'";
        Cursor cursor = database.rawQuery(selectQuery, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    public List<UserChat> getUserChats() {
        Cursor cursor = null;
        List<UserChat> arrayList = null;
        try {
            String query = "select * from " + DatabaseConstants.TABLE_USER_CHAT;
            cursor = database.rawQuery(query, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                arrayList = new ArrayList<>();
                do {
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex(DatabaseConstants.USER_CHAT_RESPONSE));
                    UserChat obj = (UserChat) byteToObj(blob);
                    if (obj != null) {
                        arrayList.add(obj);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return arrayList;
    }

    public UserChat getUserChat(Long userId) {
        Cursor cursor = null;
        byte[] blob;
        UserChat userChat = new UserChat();

        try {
            String[] columns = new String[]{DatabaseConstants.USER_CHAT_RESPONSE};
            String selection = DatabaseConstants.USER_CHAT_USER_ID + " = ?";
            String[] selectionArgs = new String[]{userId.toString()};

            cursor = database.query(DatabaseConstants.TABLE_USER_CHAT, columns, selection,
                    selectionArgs, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                blob = cursor.getBlob(cursor.getColumnIndex(DatabaseConstants.USER_CHAT_RESPONSE));
                userChat = (UserChat) byteToObj(blob);
            } else {
                userChat = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userChat;
    }


    public List<ConnectionsResponse.User> getConncetions() {
        Cursor cursor = null;
        byte[] blob;
        List<ConnectionsResponse.User> obj = null;
        try {
            cursor = database.query(DatabaseConstants.TABLE_CONNECTIONS, new String[]{"*"}, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToLast();
                blob = cursor.getBlob(cursor.getColumnIndex(DatabaseConstants.CONNECTIONS_RESPONSE));
                if (blob != null)
                    obj = (List<ConnectionsResponse.User>) byteToObj(blob);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return obj;
    }


    public void insertConnections(List<ConnectionsResponse.User> connection) {
        try {
            database = this.getWritableDatabase();
            database.execSQL("delete from " + DatabaseConstants.TABLE_CONNECTIONS);
            final ContentValues values = new ContentValues();
            values.put(DatabaseConstants.CONNECTIONS_RESPONSE, objToByte(connection));
            database.insert(DatabaseConstants.TABLE_CONNECTIONS, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getProfilePic(Long userId) {
        List<ConnectionsResponse.User> userList = getConncetions();
        String s = null;
        if (userList != null) {
            for (ConnectionsResponse.User user : userList) {
                if (user.getUserId().equals(userId)) {
                    s = user.getProfileImage();
                    return s;
                } else
                    s = null;
            }
            return s;
        } else {
            return s;
        }

    }

    public void removeUserFromChatList(Long id) {
        database = this.getWritableDatabase();
        database.execSQL("delete from " + DatabaseConstants.TABLE_USER_CHAT + " where " + DatabaseConstants.USER_CHAT_USER_ID + "=" + id);
    }


    public void insertBlocked(ConnectionTypeResponse.User user) {
        try {
            database = this.getWritableDatabase();
            final ContentValues values = new ContentValues();
            values.put(DatabaseConstants.BLOCKED_USER_ID, user.getUserId());
            values.put(DatabaseConstants.BLOCKED_RESPONSE, objToByte(user));
            if (!CheckIsUserBlocked(user.getUserId())) {
                database.insert(DatabaseConstants.TABLE_BLOCKED, null, values);
            } else {
                database.update(DatabaseConstants.TABLE_BLOCKED, values,
                        DatabaseConstants.BLOCKED_USER_ID + " = ? ", new String[]{String.valueOf(user.getUserId())});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean CheckIsUserBlocked(Long userId) {
        String selectQuery = "select * from " + DatabaseConstants.TABLE_BLOCKED + " where " +
                DatabaseConstants.BLOCKED_USER_ID + "='" + userId + "'";
        Cursor cursor = database.rawQuery(selectQuery, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    public List<ConnectionTypeResponse.User> getBlockedUserList() {
        Cursor cursor = null;
        List<ConnectionTypeResponse.User> arrayList = null;
        try {
            String query = "select * from " + DatabaseConstants.TABLE_BLOCKED;
            cursor = database.rawQuery(query, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                arrayList = new ArrayList<>();
                do {
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex(DatabaseConstants.BLOCKED_RESPONSE));
                    ConnectionTypeResponse.User obj =(ConnectionTypeResponse.User) byteToObj(blob);
                    if (obj != null) {
                        arrayList.add(obj);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return arrayList;
    }

    public void removeUserFromBlocked(Long id) {
        database = this.getWritableDatabase();
        database.execSQL("delete from " + DatabaseConstants.TABLE_BLOCKED + " where " + DatabaseConstants.BLOCKED_USER_ID + "=" + id);
    }

}
