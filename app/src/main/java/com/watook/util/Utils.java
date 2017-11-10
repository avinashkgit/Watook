package com.watook.util;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.watook.application.GPSTracker;
import com.watook.application.MyApplication;
import com.watook.application.MySharedPreferences;
import com.watook.manager.DatabaseManager;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String emptyIfNull(String s) {
        if (s == null)
            return "";
        else
            return s;
    }

    public static String emptyIfNull(Integer s) {
        if (s == null)
            return "";
        else
            return s + "";
    }

    public static boolean isEmpty(String s) {
        return s == null || s.equals("") ||s.equals("null");
    }

    public static boolean isEmpty(Integer s) {
        return s == null;
    }

    public static boolean isNetworkAvailable() {
        final Context appContext = MyApplication.getContext();
        ConnectivityManager cm =
                (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static int getAge(String date, String format) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
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

        float distance = locationA.distanceTo(locationB); // in meters
        String setDist = "";

        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(0);

        if (distance < 1000) {
//            setDist = "Less then " + formatter.format(distance) + " mts";
            if (distance > 100) {
                setDist = "Less then " + formatter.format((int) Math.ceil(distance / 100) * 100) + " mts";
            }
            if (distance <= 100 && distance > 10) {
                setDist = "Less then " + formatter.format((int) Math.ceil(distance / 10) * 10) + " mts";
            }
            if (distance <= 10) {
                setDist = "Less then 10 mts";
            }


        } else {
            boolean isDistUnitKm = false;
            try {
                isDistUnitKm = (boolean) MySharedPreferences.getObject(Constant.DISTANCE_UNIT_KM);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (isDistUnitKm) {
                distance = distance / 1000;
//                NumberFormat formatter1 = NumberFormat.getNumberInstance();
//                formatter1.setMaximumFractionDigits(1);
//                setDist = formatter.format(distance) + " Kms";
                setDist = (int) Math.ceil(distance) + " Kms";
            } else {
                distance = (float) (distance * 0.000621371192);
//                NumberFormat formatter2 = NumberFormat.getNumberInstance();
//                formatter2.setMaximumFractionDigits(1);
//                setDist = formatter.format(distance) + " Miles";
                setDist = (int) Math.ceil(distance) + " Miles";
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

    public static String getDistanceBasedOnUnit(int n) {
        String s;
        boolean isDistUnitKm = false;
        try {
            isDistUnitKm = (boolean) MySharedPreferences.getObject(Constant.DISTANCE_UNIT_KM);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isDistUnitKm) {
            s = milesToKm(n) + " Km";
        } else {
            s = n + " Miles";
        }
        return s;
    }

    public static String timeInMillsToTime(long timeInMills) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date resultDate = new Date(timeInMills);
        return (sdf.format(resultDate));
    }

    public static int compareDate(long time1, long time2) {
        Date d1 = new Date(time1);
        Date d2 = new Date(time2);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return compare(c1, c2);

    }

    private static int compare(Calendar c1, Calendar c2) {

        int result = compare(c1, c2, Calendar.YEAR);
        if (result == 0) {
            result = compare(c1, c2, Calendar.DAY_OF_YEAR);
        }
        return result;
    }

    private static int compare(Calendar c1, Calendar c2, int field) {
        Integer i1 = c1.get(field);
        Integer i2 = c2.get(field);
        return i1.compareTo(i2);
    }

    public static String getUserFriendlyDate(long time) {
        Date d1 = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d1);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "Today";
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday";
        } else {
            return new SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(d1);
        }
    }

}
