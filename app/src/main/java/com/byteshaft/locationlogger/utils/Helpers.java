package com.byteshaft.locationlogger.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import java.text.SimpleDateFormat;

import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;

import com.byteshaft.locationlogger.MainActivity;
import com.byteshaft.locationlogger.R;
import com.byteshaft.locationlogger.fragments.AuthenticationFragment;
import com.byteshaft.locationlogger.fragments.QuestionnaireFragment;
import com.byteshaft.locationlogger.fragments.WelcomeFragment;
import com.byteshaft.locationlogger.services.LocationService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.byteshaft.locationlogger.MainActivity.fragmentManager;

public class Helpers {

    public static String[] accessCodes = {"123456", "abcdef", "yolo"};

    private static NotificationManager mNotifyMgr;

    public static final Runnable exitApp = new Runnable() {
        public void run() {
            MainActivity.getInstance().finish();
            System.exit(0);
        }
    };

    // setting up the app status and stopping location services
    public static final Runnable withdraw = new Runnable() {
        public void run() {
            AppGlobals.putAppStatus(0);
            AppGlobals.putAdversaryAdded(false);
            Helpers.loadFragment(fragmentManager, new WelcomeFragment(), true, null);
            Intent locationServiceIntent = new Intent(MainActivity.getInstance(), LocationService.class);
            MainActivity.getInstance().stopService(locationServiceIntent);
            Helpers.dismissNotification();
            AppGlobals.putUserTestResults(null);
            AppGlobals.putAdversaryTestResults(null);
            if (LocationService.repeatNotificationTimer != null) {
                LocationService.repeatNotificationTimer.cancel();
            }
            AppGlobals.getContext().getSharedPreferences("CREDENTIALS", 0).edit().clear().apply();
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

    // heads up notification
    public static void buildNotification(String content) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(AppGlobals.getContext())
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Location Logger")
                        .setContentText(content);

        // defining notification's on click
        Intent resultIntent = new Intent(AppGlobals.getContext(), MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        AppGlobals.getContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mNotifyMgr = (NotificationManager) AppGlobals.getContext().getSystemService(NOTIFICATION_SERVICE);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        mNotifyMgr.notify(1, mBuilder.build());
        AppGlobals.putAppStatus(2);

        if (MainActivity.isMainActivityRunning) {
            Helpers.loadFragment(fragmentManager, new AuthenticationFragment(), false, null);
        }
    }

    public static void dismissNotification() {
        if (mNotifyMgr != null) {
            mNotifyMgr.cancel(1);
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

    public static void loadFragment(FragmentManager fragmentManager, android.support.v4.app.Fragment fragment, boolean withdraw,
                                    String fragmentName) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (withdraw) {
            // loading framgment with custom animation
            transaction.setCustomAnimations(R.anim.anim_transition_fragment_slide_left_enter, R.anim.anim_transition_fragment_slide_right_exit,
                    R.anim.anim_transition_fragment_slide_right_enter, R.anim.anim_transition_fragment_slide_left_exit);
        } else {
            transaction.setCustomAnimations(R.anim.anim_transition_fragment_slide_right_enter, R.anim.anim_transition_fragment_slide_left_exit,
                    R.anim.anim_transition_fragment_slide_left_enter, R.anim.anim_transition_fragment_slide_right_exit);
        }
        if (fragmentName != null) {
            transaction.replace(R.id.container_main, fragment).addToBackStack(fragmentName);
        } else {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                MainActivity.fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            transaction.replace(R.id.container_main, fragment);
        }
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
        double distance = SphericalUtil.computeDistanceBetween(answerLatLng, actualLatLng);
        int distanceInMeters = (int) distance;
        return distanceInMeters < vicinityThreshold;
    }

    public static long getRemainingTimeInDays(long remainingTimeInMillis) {
        long seconds = remainingTimeInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return days;
    }

    public static String getRemainingTimeInDaysAndHours(long timeInMillis) {
        long seconds = timeInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long actualHours = hours - days * 24;
        return String.valueOf(days + "Days - " + actualHours + "Hours");
    }

    public static String getTimeTakenInMinutesAndSeconds(long timeInMillis) {
        return (new SimpleDateFormat("mm:ss")).format(new Date(timeInMillis));
    }

    public static boolean isAccessCodeValid(String input) {
        for (String accessCode : accessCodes) {
            if (input.equals(accessCode)) {
                return true;
            }
        }
        return false;
    }
}
