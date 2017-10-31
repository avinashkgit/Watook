package com.watook.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MySharedPreferences {
    /**
     * Instance reference of SharedPreferences for given name and mode (MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITEABLE, MODE_MULTI_PROCESS)
     *
     * @param name String
     * @param mode int
     * @return SharedPreferences
     * @see SharedPreferences
     */
    public static SharedPreferences instance(String name, int mode) {
        return MyApplication.getContext().getSharedPreferences(name, mode);
    }

    /**
     * Instance reference of SharedPreferences for package name with default mode as MODE_PRIVATE which is private to current app only.
     *
     * @return SharedPreferences
     * @see SharedPreferences
     */
    public static SharedPreferences instance() {
        System.out.println(">>>>>>>>>>>>>>ApP.ofaShr<<<<<<<<<<<<<<<<<<<");
        return MyApplication.getContext().getSharedPreferences(MyApplication.getContext().getPackageName(), MyApplication.MODE_PRIVATE);
    }

    /**
     * Editor of the SharedPreference created by default instance()
     *
     * @return SharedPreferences
     * @see SharedPreferences.Editor
     */
    public static SharedPreferences.Editor editor() {
        return instance().edit();
    }

    /**
     * Editor of the SharedPreference created by give name and mode (MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITEABLE, MODE_MULTI_PROCESS)
     *
     * @param name String
     * @param mode int
     * @return SharedPreferences
     * @see SharedPreferences
     */
    public static SharedPreferences.Editor editor(String name, int mode) {
        return instance(name, mode).edit();
    }

    /**
     * Put Object tied with provided key in default Shared Preference
     *
     * @param key    String
     * @param object Object
     * @throws IOException If unable to access Stream
     */
    public static void putObject(String key, Object object) throws Exception {
        putObject(key, object, editor());
    }

    /**
     * Put Object tied with provided key in specified Shared Preference
     *
     * @param key    String
     * @param object Object
     * @param name   String
     * @param mode   int
     * @throws IOException If unable to access Stream
     */
    public static void putObject(String key, Object object, String name, int mode) throws Exception {
        putObject(key, object, editor(name, mode));
    }

    private static void putObject(String key, Object object, SharedPreferences.Editor editor) throws Exception {
        if (object != null)
            editor.putString(key, serialize(object)).commit();
    }

    /**
     * Get Object based on key from default Shared Preference
     *
     * @param key String
     * @return Object
     * @throws IOException            If unable to access Stream
     * @throws ClassNotFoundException If unable to find class
     */
    public static Object getObject(String key) throws Exception {
        return getObject(key, instance());
    }

    /**
     * Get Object based on key from specified Shared Preference
     *
     * @param key  String
     * @param name String
     * @param mode int
     * @return Object
     * @throws IOException            If unable to access Stream
     * @throws ClassNotFoundException If unable to find class
     */
    public static Object getObject(String key, String name, int mode) throws Exception {
        return getObject(key, instance(name, mode));
    }

    private static Object getObject(String key, SharedPreferences sharedPreferences) throws Exception {
        String serialized = sharedPreferences.getString(key, null);
        return deserialize(serialized);
    }

    /**
     * clears all preference data
     */
    public static void clear() {
        editor().clear().commit();
    }

    public static void clearObject(String strKey) {
        editor().remove(strKey).commit();
    }

    /**
     * Serialize any object into String
     *
     * @param object Object
     * @return String
     * @throws IOException If unable to access Stream
     */
    public static String serialize(Object object) throws IOException {
        if (object == null)
            return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new Base64OutputStream(byteArrayOutputStream, 0));
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toString();
    }


    /**
     * Deserialize provided string into Object
     *
     * @param serialized String
     * @return Object
     * @throws IOException            If unable to access Stream
     * @throws ClassNotFoundException If unable to find class
     */
    public static Object deserialize(String serialized) throws Exception {
        if (serialized == null)
            return null;
        return new ObjectInputStream(new Base64InputStream(new ByteArrayInputStream(serialized.getBytes()), 0)).readObject();
    }

}
