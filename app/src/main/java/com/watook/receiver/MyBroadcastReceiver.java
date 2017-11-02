package com.watook.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.watook.application.GPSTracker;

/**
 * Created by Avinash.Kumar on 02-Nov-17.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, GPSTracker.class);
        context.startService(startServiceIntent);
    }
}
