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

import com.byteshaft.locationlogger.MainActivity;
import com.byteshaft.locationlogger.R;
import com.byteshaft.locationlogger.services.LocationService;
import com.byteshaft.locationlogger.utils.AppGlobals;
import com.byteshaft.locationlogger.utils.DatabaseHelpers;
import com.byteshaft.locationlogger.utils.Helpers;

/**
 * Created by fi8er1 on 18/03/2017.
 */

public class WaitingFragment extends Fragment {

    View baseViewWaitingFragment;
    Animation animTextViewFading;
    TextView tvInstructionsTimeLeft;
    TextView tvInstructionsCaution;
    Button btnWithdraw;
    Intent locationServiceIntent;
    DatabaseHelpers mDatabaseHelpers;

    Runnable withdraw = new Runnable() {
        public void run() {
            AppGlobals.putAppStatus(0);
            Helpers.loadFragment(MainActivity.fragmentManager, new WelcomeFragment(), true, null);
//            getActivity().stopService(locationServiceIntent);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseViewWaitingFragment = inflater.inflate(R.layout.fragment_waiting, container, false);
        locationServiceIntent = new Intent(getActivity(), LocationService.class);
        mDatabaseHelpers = new DatabaseHelpers(getActivity());
        getActivity().startService(locationServiceIntent);
        AppGlobals.putAppStatus(1);

        tvInstructionsCaution = (TextView) baseViewWaitingFragment.findViewById(R.id.tv_instructions_caution);
        tvInstructionsTimeLeft = (TextView) baseViewWaitingFragment.findViewById(R.id.tv_time_left);
        btnWithdraw = (Button) baseViewWaitingFragment.findViewById(R.id.btn_withdraw);
        animTextViewFading = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_text_fading);
        tvInstructionsCaution.startAnimation(animTextViewFading);

        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helpers.AlertDialogWithPositiveFunctionNegativeButton(getActivity(), "Are you sure?",
                        "Proceeding with withdrawal will result in all of your logged data to be lost.",
                        "Yes", "Cancel", withdraw);
            }
        });

        return baseViewWaitingFragment;
    }
}
