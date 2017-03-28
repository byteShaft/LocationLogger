package com.byteshaft.locationlogger;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.byteshaft.locationlogger.fragments.ExitSurveyFragment;
import com.byteshaft.locationlogger.fragments.QuestionnaireFragment;
import com.byteshaft.locationlogger.fragments.ResultsFragment;
import com.byteshaft.locationlogger.fragments.WelcomeFragment;
import com.byteshaft.locationlogger.utils.AppGlobals;
import com.byteshaft.locationlogger.utils.Helpers;

public class MainActivity extends FragmentActivity {

    private static MainActivity sInstance;
    public static FragmentManager fragmentManager;
    public static boolean isMainActivityRunning;

    public static MainActivity getInstance() {
        return sInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sInstance = this;
        fragmentManager = getSupportFragmentManager();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadRespectiveFragment();
            }
        }, 250);
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void loadRespectiveFragment() {
        if (AppGlobals.getAppStatus() == 0) {
            Helpers.loadFragment(fragmentManager, new WelcomeFragment(), false, null);
        } else if (AppGlobals.getAppStatus() == 1) {
            Helpers.loadFragment(fragmentManager, new QuestionnaireFragment(), false, null);
        } else if (AppGlobals.getAppStatus() == 2) {
            Helpers.loadFragment(fragmentManager, new QuestionnaireFragment(), false, null);
        } else if (AppGlobals.getAppStatus() == 3) {
            Helpers.loadFragment(fragmentManager, new QuestionnaireFragment(), false, null);
        } else if (AppGlobals.getAppStatus() == 4) {
            Helpers.loadFragment(fragmentManager, new ExitSurveyFragment(), false, null);
        } else if (AppGlobals.getAppStatus() == 5) {
            Helpers.loadFragment(fragmentManager, new ResultsFragment(), false, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isMainActivityRunning = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isMainActivityRunning = false;
    }
}
