package com.byteshaft.locationlogger.services;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.byteshaft.locationlogger.MainActivity;
import com.byteshaft.locationlogger.utils.AppGlobals;
import com.byteshaft.locationlogger.utils.Helpers;

import static com.byteshaft.locationlogger.utils.Helpers.openLocationServiceSettings;
import static com.byteshaft.locationlogger.utils.Helpers.recheckLocationServiceStatus;

public class LocationProviderChangedListener extends BroadcastReceiver {

    Intent locationServiceIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO make alert dialog here to inform user that he/she has turned off his/her location
        if (intent.getAction().equals("android.location.PROVIDERS_CHANGED")) {

            // this condition gets called whenever location providers are changed
            if (!Helpers.isAnyLocationServiceAvailable() && AppGlobals.isLocationServiceEnabled()) {
                Helpers.AlertDialogWithPositiveNegativeNeutralFunctions(MainActivity.getInstance(),
                        "Location logging has been paused", "Enable device's location service to continue", "Settings", "ReCheck", "Dismiss",
                        openLocationServiceSettings, recheckLocationServiceStatus);
                locationServiceIntent = new Intent(context, LocationService.class);
                context.stopService(locationServiceIntent);
                AppGlobals.setLocationServicePaused(true);
            } else if (Helpers.isAnyLocationServiceAvailable() && AppGlobals.isLocationServicePaused()) {
                context.startService(locationServiceIntent);
                AppGlobals.setLocationServicePaused(false);
            }
        }

    }

    public static final Runnable recheckLocationServiceStatus = new Runnable() {
        public void run() {
            if (!Helpers.isAnyLocationServiceAvailable()) {
                Helpers.AlertDialogWithPositiveNegativeNeutralFunctions(MainActivity.getInstance(),
                        "Location Service disabled", "Enable device GPS to continue", "Settings", "ReCheck", "Dismiss",
                        openLocationServiceSettings, recheckLocationServiceStatus);
            }
        }
    };

    public static final Runnable openLocationServiceSettings = new Runnable() {
        public void run() {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            MainActivity.getInstance().startActivity(intent);
        }
    };
}
