package com.byteshaft.locationlogger.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.byteshaft.locationlogger.MainActivity;
import com.byteshaft.locationlogger.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by fi8er1 on 18/03/2017.
 */

public class Helpers {

    public static final Runnable exitApp = new Runnable() {
        public void run() {
            MainActivity.getInstance().finish();
            System.exit(0);
        }
    };
    public static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static Runnable openPlayServicesInstallation = new Runnable() {
        public void run() {
            Helpers.openInstallationActivityForPlayServices(MainActivity.getInstance());
        }
    };

    public static void openInstallationActivityForPlayServices(final Activity context) {
        if (context == null) {
            return;
        }
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_BROWSABLE);
        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms&hl=en"));
        context.startActivity(i);
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

    public static boolean checkPlayServicesAvailability(Context context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            return false;
        } else {
            return true;
        }
    }

    public static void loadFragment(FragmentManager fragmentManager, android.support.v4.app.Fragment fragment, boolean withdraw) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (withdraw) {
            transaction.setCustomAnimations(R.anim.anim_transition_fragment_slide_left_enter, R.anim.anim_transition_fragment_slide_right_exit,
                    R.anim.anim_transition_fragment_slide_right_enter, R.anim.anim_transition_fragment_slide_left_exit);
        } else {
            transaction.setCustomAnimations(R.anim.anim_transition_fragment_slide_right_enter, R.anim.anim_transition_fragment_slide_left_exit,
                    R.anim.anim_transition_fragment_slide_left_enter, R.anim.anim_transition_fragment_slide_right_exit);
        }
        transaction.replace(R.id.container_main, fragment);
        transaction.commit();
    }

    public static boolean isAnyLocationServiceAvailable() {
        LocationManager locationManager = getLocationManager();
        return isGpsEnabled(locationManager) || isNetworkBasedGpsEnabled(locationManager);
    }

    private static LocationManager getLocationManager() {
        return (LocationManager) MainActivity.getInstance().getSystemService(Context.LOCATION_SERVICE);
    }

    private static boolean isGpsEnabled(LocationManager locationManager) {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private static boolean isNetworkBasedGpsEnabled(LocationManager locationManager) {
        return locationManager.isProviderEnabled((LocationManager.NETWORK_PROVIDER));
    }

    public static void AlertDialogMessage(Context context, String title, String message, String neutralButtonText) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(neutralButtonText, null)
                .show();
    }

    public static void AlertDialogWithPositiveNegativeFunctions(
            Context context, String title, String message, String positiveButtonText,
            String negativeButtonText, final Runnable listenerYes, final Runnable listenerNo) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listenerYes.run();
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listenerNo.run();
                    }
                })
                .show();
    }

    public static void AlertDialogWithPositiveNegativeNeutralFunctions(
            Context context, String title, String message, String positiveButtonText,
            String negativeButtonText, String neutralButtonText, final Runnable listenerYes,
            final Runnable listenerNo) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listenerYes.run();
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listenerNo != null) {
                            listenerNo.run();
                        }
                    }
                })
                .setNeutralButton(neutralButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static void AlertDialogWithPositiveFunctionNegativeButton(
            Context context, String title, String message, String positiveButtonText,
            String negativeButtonText, final Runnable listenerYes) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listenerYes.run();
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static boolean hasPermissionsForDevicesAboveMarshmallowIfNotRequestPermissions(Activity activity) {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null && PERMISSIONS != null) {
            for (String permission : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL);
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isAnswerLocationInVicinityOfActualLocation(LatLng answerLatLng, LatLng actualLatLng) {
        float vicinityThreshold = 500;
        Location answerLocation = new Location(answerLatLng.toString());
        Location actualLocation = new Location(actualLatLng.toString());
        float distanceInMeters = answerLocation.distanceTo(actualLocation);
        if (distanceInMeters < vicinityThreshold) {
            return true;
        } else {
            return false;
        }
    }
}
