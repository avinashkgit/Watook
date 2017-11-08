package com.watook.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.BuildConfig;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.firebase.database.FirebaseDatabase;
import com.watook.R;
import com.watook.manager.DatabaseManager;
import com.watook.util.AppLog;
import com.watook.util.Constant;
import com.watook.util.TypefaceUtil;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MyApplication extends MultiDexApplication {
    private static Context context;
    public static String resolution = "";
    private static MyApplication myApplication;
    public boolean isAppBackground = true;
    public static int density;
    DisplayMetrics metrics;
    private String userId;
    private String token;
    private int height, width;


    private byte[] key;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }


    public static MyApplication getInstance() {
        if (myApplication == null) {
            myApplication = new MyApplication();
        }
        return myApplication;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public static Context getContext() {
        return MyApplication.context;

    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();

//        TypefaceUtil.overrideFont(getApplicationContext(), "NORMAL", getResources().getString(R.string.FONT_MY_RIADPRO_REGULAR));
//        TypefaceUtil.overrideFont(getApplicationContext(), "MONOSPACE", getResources().getString(R.string.FONT_CAMBAYREGULAR));
//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", getResources().getString(R.string.FONT_CAMBAYREGULAR));
//        TypefaceUtil.overrideFont(getApplicationContext(), "SANS", getResources().getString(R.string.FONT_MYRIADPRO_REGULAR));
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        metrics = new DisplayMetrics();
        density = (int) context.getResources().getDisplayMetrics().density;
        wm.getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_HIGH:
                resolution = "hdpi";
                setResolution(resolution);
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                resolution = "xhdpi";
                setResolution(resolution);
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                resolution = "xxhdpi";
                setResolution(resolution);
                break;
            case DisplayMetrics.DENSITY_420:
                resolution = "xxhdpi";
                setResolution(resolution);
                break;
            case DisplayMetrics.DENSITY_560:
                resolution = "xxxhdpi";
                setResolution(resolution);
                break;
            default:

        }

        initDatabase();

        try {
            generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Constant.RESOLUTION_TYPE = getResolution();
        if (BuildConfig.DEBUG) {
        } else {
        }
        try {
            userId = (String) MySharedPreferences.getObject(Constant.USER_ID);
            token = (String) MySharedPreferences.getObject(Constant.TOKEN);
            MyApplication.getInstance().setUserId(userId);
            MyApplication.getInstance().setToken(token);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void generateKey() throws Exception {
        if (MySharedPreferences.getObject(Constant.KEY) != null) {
            MyApplication.getInstance().setKey((byte[]) MySharedPreferences.getObject(Constant.KEY));
        } else {
            byte[] keyStart = "this is a key".getBytes();
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(keyStart);
            kgen.init(128, sr); // 192 and 256 bits may not be available
            SecretKey skey = kgen.generateKey();
            MySharedPreferences.putObject(Constant.KEY, skey.getEncoded());
            AppLog.i("Key::" + skey.getEncoded());
            MyApplication.getInstance().setKey(skey.getEncoded());
        }
    }

    /**
     * Initialize the database
     *
     * @throws Error
     */
    private void initDatabase() {
        try {
            DatabaseManager.getInstance(getApplicationContext()).createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            DatabaseManager.getInstance(getApplicationContext()).openDataBase();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        DatabaseManager.getInstance(getApplicationContext()).close();
    }


    public boolean isAppBackground() {
        return isAppBackground;
    }

    public void setIsAppBackground(boolean isAppBackground) {
        this.isAppBackground = isAppBackground;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static HashMap<Long, String> getRequestStatusCode() {
        HashMap<Long, String> set = new HashMap<>();
        set.put(Long.parseLong("501"), Constant.ACCEPT);
        set.put(Long.parseLong("502"), Constant.REJECT);
        set.put(Long.parseLong("503"), Constant.REQUEST_SENT);
        set.put(Long.parseLong("504"), Constant.BLOCKED);
        return set;
    }


    public static HashMap<String, Long> getGenderCode() {
        HashMap<String, Long> set = new HashMap<>();
        set.put(Constant.FEMALE, Long.parseLong("201"));
        set.put(Constant.MALE, Long.parseLong("202"));
        return set;
    }

    public static HashMap<String, Long> getDistanceCode() {
        HashMap<String, Long> set = new HashMap<>();
        set.put(Constant.KILO_METER, Long.parseLong("601"));
        set.put(Constant.MILES, Long.parseLong("602"));
        set.put(Constant.METER, Long.parseLong("603"));
        return set;
    }
}
