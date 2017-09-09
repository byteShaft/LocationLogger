package com.byteshaft.locationlogger.services;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.byteshaft.locationlogger.utils.AppGlobals;
import com.byteshaft.locationlogger.utils.DatabaseHelpers;
import com.byteshaft.locationlogger.utils.Helpers;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;

    public class LocationService extends Service implements
            GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private DatabaseHelpers mDatabaseHelpers;
    private Location mPreviousLocation;
    private long timeStayedAtOnePlace;
    private double latitude;
    private double longitude;
    private static final int FIVE_HOURS_IN_MILLIS = 3600000;
    public static CountDownTimer repeatNotificationTimer;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        mDatabaseHelpers = new DatabaseHelpers(LocationService.this);
    }

    public void buildGoogleApiClient() {
        createLocationRequest();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); // in milliseconds
        mLocationRequest.setFastestInterval(5000);// in milliseconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private String getCurrentTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        String format = simpleDateFormat.format(new Date());
        return format;
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Location Service", "Started");
        buildGoogleApiClient();
        AppGlobals.setLocationServiceStarted(true);
        // if user kills the application start the service again
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
        stopLocationUpdates();
        AppGlobals.setLocationServiceStarted(false);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("OnLocationChanged ", "Called");
        long systemTimeInMillis = System.currentTimeMillis();
        // if system time is greater than the notification time show the notification
        if (systemTimeInMillis > AppGlobals.getNotificationTime()) {
            Helpers.buildNotification("We have now 1 week of your data");
            repeatNotificationTimer = new CountDownTimer(86400000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    Helpers.buildNotification("We have now 1 week of your data");
                    start();
                }
            }.start();
            // stop the service after notification
            stopSelf();

            // TODO skip the entry if user has stayed on a location for more than one hour
        } else if (mPreviousLocation != null) {

            long prevTime = mPreviousLocation.getTime();
            long currentTime = location.getTime();

            timeStayedAtOnePlace = currentTime - prevTime;

            latitude = mPreviousLocation.getLatitude();
            longitude = mPreviousLocation.getLongitude();

            if (timeStayedAtOnePlace < FIVE_HOURS_IN_MILLIS) {

                String latitudeAsString = String.valueOf(latitude);
                String longitudeAsString = String.valueOf(longitude);
                String timeAtOnePlaceAsString = String.valueOf(timeStayedAtOnePlace);

                mDatabaseHelpers.createNewEntry(latitudeAsString, longitudeAsString, getCurrentTimeStamp(),
                        timeAtOnePlaceAsString);
            }
        }
        mPreviousLocation = location;

    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
