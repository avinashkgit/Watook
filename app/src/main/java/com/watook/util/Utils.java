package com.watook.util;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.watook.application.GPSTracker;
import com.watook.application.MyApplication;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {

    public static String emptyIfNull(String s) {
        if (s == null)
            return "";
        else
            return s;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.equals("");
    }

    public static boolean isNetworkAvailable() {
        final Context appContext = MyApplication.getContext();
        ConnectivityManager cm =
                (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static int getAge(String date, String format) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar dob = Calendar.getInstance();
        dob.setTime(sdf.parse(date));

        Calendar today = Calendar.getInstance();
        int curYear = today.get(Calendar.YEAR);
        int dobYear = dob.get(Calendar.YEAR);
        int age = curYear - dobYear;


        // if dob is month or day is behind today's month or day
        // reduce age by 1

        int curMonth = today.get(Calendar.MONTH);
        int dobMonth = dob.get(Calendar.MONTH);
        if (dobMonth > curMonth) { // this year can't be counted!
            age--;
        } else if (dobMonth == curMonth) { // same month? check for day
            int curDay = today.get(Calendar.DAY_OF_MONTH);
            int dobDay = dob.get(Calendar.DAY_OF_MONTH);
            if (dobDay > curDay) { // this year can't be counted!
                age--;
            }
        }
        return age;
    }

    public static void showSoftKeyboard(Context context, View view) {
        if (context instanceof Activity) {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(view, 0);

        }

    }

    public static void hideSoftKeyboard(Context context) {
        View focused = null;
        if (context instanceof Activity) {
            focused = ((Activity) context).getCurrentFocus();
            if (focused != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) context
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(
                            focused.getWindowToken(), 0);
                }
            }
        }
    }

    public static String getDistance(Context context, Double userLat, Double userLong) {
        GPSTracker gpsTracker = new GPSTracker(context);

        Location locationA = new Location("point A");
        locationA.setLatitude(gpsTracker.getLatitude());
        locationA.setLongitude(gpsTracker.getLongitude());

        Location locationB = new Location("point B");
        locationB.setLatitude(userLat);
        locationB.setLongitude(userLong);

        float distance = locationA.distanceTo(locationB) / 1000; // in meters
        String setDist = "";

        if (distance < 1000) {
            NumberFormat formatter = NumberFormat.getNumberInstance();
            formatter.setMaximumFractionDigits(0);
            setDist = formatter.format(distance) + " mts";
        }
        if (distance >= 1000) {
            if (true) {
                distance = distance / 1000;
                NumberFormat formatter = NumberFormat.getNumberInstance();
                formatter.setMaximumFractionDigits(1);
                setDist = formatter.format(distance) + " Kms";
            } else {
                distance = (float) (distance * 0.000621371192);
                NumberFormat formatter = NumberFormat.getNumberInstance();
                formatter.setMaximumFractionDigits(1);
                setDist = formatter.format(distance) + " Miles";
            }
        }
        return setDist;

    }


    public static int milesToKm(int n) {
        Double s = ((n) / 0.621371);
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(0);
        s = Double.valueOf(formatter.format(s));
        return s.intValue();
    }

}
