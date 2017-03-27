package com.byteshaft.locationlogger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.byteshaft.locationlogger.R;

/**
 * Created by fi8er1 on 27/03/2017.
 */

public class ExitSurveyFragment extends Fragment {
    
    View baseViewExitSurveyFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseViewExitSurveyFragment = inflater.inflate(R.layout.fragment_exit_survey, container, false);

        return baseViewExitSurveyFragment;
    }
}
