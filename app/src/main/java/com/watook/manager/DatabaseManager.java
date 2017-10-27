package com.watook.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.watook.model.MyProfile;
import com.watook.model.response.CodeValueResponse;
import com.watook.model.response.RegistrationResponse;
import com.watook.model.response.UserListResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



@SuppressWarnings("ALL")
public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "watook.db";
    private static final int DATABASE_VERSION = 2;
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

    }

    public void clearTables() {
        database = this.getWritableDatabase();
        database.execSQL("delete from " + DatabaseConstants.TABLE_USER_PROFILE);
        database.execSQL("delete from " + DatabaseConstants.TABLE_REGISTRATION);
        database.execSQL("delete from " + DatabaseConstants.TABLE_CODE_VALUE);
        database.execSQL("delete from " + DatabaseConstants.TABLE_USERS);
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


    public void insertUsersList(List<UserListResponse.UserList> lst) {
        try {
            database = this.getWritableDatabase();
            database.execSQL("delete from " + DatabaseConstants.TABLE_USERS);
            final ContentValues values = new ContentValues();
            values.put(DatabaseConstants.USERS_RESPONSE, objToByte(lst));
            database.insert(DatabaseConstants.TABLE_USERS, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public List<UserListResponse.UserList> getUsersList() {
        Cursor cursor = null;
        byte[] blob;
        List<UserListResponse.UserList> obj = null;
        try {
            cursor = database.query(DatabaseConstants.TABLE_USERS, new String[]{"*"}, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToLast();
                blob = cursor.getBlob(cursor.getColumnIndex(DatabaseConstants.USERS_RESPONSE));
                if (blob != null)
                    obj = (List<UserListResponse.UserList>) byteToObj(blob);
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
}