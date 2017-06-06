package com.byteshaft.locationlogger.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.byteshaft.locationlogger.R;
import com.byteshaft.locationlogger.services.LocationService;
import com.byteshaft.locationlogger.utils.AppGlobals;
import com.byteshaft.locationlogger.utils.DatabaseHelpers;
import com.byteshaft.locationlogger.utils.Helpers;

public class WaitingFragment extends Fragment {

    View baseViewWaitingFragment;
    Animation animTextViewFading;
    TextView tvInstructionsTimeLeft;
    TextView tvInstructionsCaution;
    Button btnWithdraw;
    Intent locationServiceIntent;
    DatabaseHelpers mDatabaseHelpers;
    double timeInHours;
    long timeInDays;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseViewWaitingFragment = inflater.inflate(R.layout.fragment_waiting, container, false);
        // making new intent of location service which will be used to start location using this
        // intent
        locationServiceIntent = new Intent(getActivity(), LocationService.class);
        // making object of Database helpers in order to access data from that class
        mDatabaseHelpers = new DatabaseHelpers(getActivity());
        // starting location service for getting location updates in the background
        getActivity().startService(locationServiceIntent);
        // getting remaining time
        // after that location updates will stop
        long remainingTimeInMillis = AppGlobals.getNotificationTime() - System.currentTimeMillis();

        timeInHours = Helpers.getRemainingTimeInHours(remainingTimeInMillis);
        timeInDays = Helpers.getRemainingTimeInDays(remainingTimeInMillis);
        int hours = (int) timeInHours/24;
        // setting and saving status of application in sharedpreferences
        AppGlobals.putAppStatus(1);

        tvInstructionsCaution = (TextView) baseViewWaitingFragment.findViewById(R.id.tv_instructions_caution);
        tvInstructionsTimeLeft = (TextView) baseViewWaitingFragment.findViewById(R.id.tv_time_left);
        // setting text for time left before we stop location updates
        tvInstructionsTimeLeft.setText(timeInDays + "Days - " + hours + "Hours");
        btnWithdraw = (Button) baseViewWaitingFragment.findViewById(R.id.btn_withdraw);
        // inflating textview with animation
        animTextViewFading = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_text_fading);
        tvInstructionsCaution.startAnimation(animTextViewFading);

        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // alerting user if he/she wants to withdraw
                Helpers.AlertDialogWithPositiveFunctionNegativeButton(getActivity(), "Are you sure?",
                        "Proceeding with withdrawal will result in all of your logged data to be lost.",
                        "Yes", "Cancel", Helpers.withdraw);
            }
        });

        return baseViewWaitingFragment;
    }
}
