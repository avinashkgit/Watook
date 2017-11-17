package com.watook.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;


import com.watook.util.Constant;

/**
 * Created by Avinash.Kumar on 14-Nov-17.
 */

public class GpsLocationReceiver extends BroadcastReceiver implements LocationListener {

    @Override
    public void onReceive(Context context, Intent intent) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            broadcastEvent(context, Constant.GPS_ENABLED);
        else
            broadcastEvent(context, Constant.GPS_DISABLED);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void broadcastEvent(Context context, String broadcastString) {
        Intent intent = new Intent(Constant.BROADCAST_RESULT);
        if (broadcastString != null)
            intent.putExtra(Constant.GPS, broadcastString);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}