package com.byteshaft.locationlogger.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.byteshaft.locationlogger.utils.AppGlobals;


public class BootStateListenerService extends BroadcastReceiver {

    // this class gets called when user reboots the device
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")
                && AppGlobals.isLocationServiceEnabled()) {
            Intent locationServiceIntent = new Intent(context, LocationService.class);
            context.startService(locationServiceIntent);
        }
    }
}
