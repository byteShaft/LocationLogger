package com.byteshaft.locationlogger.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootStateListenerService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // will start the service and alarm here
        }
    }
}
