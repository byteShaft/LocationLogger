package com.byteshaft.locationlogger.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootStateListenerService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent locationServiceIntent = new Intent(context, LocationService.class);
            context.startService(locationServiceIntent);
        }
    }
}
