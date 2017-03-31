package com.byteshaft.locationlogger.services;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

public class LocationProviderChangedListener extends BroadcastReceiver {

    boolean isGpsEnabled;
    boolean isNetworkEnabled;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.location.PROVIDERS_CHANGED")) {

            Log.i("LocationLogger ", "Location Providers changed");

            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGpsEnabled || isNetworkEnabled) {

            }
        }
    }
}
