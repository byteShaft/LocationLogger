package com.byteshaft.locationlogger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.byteshaft.locationlogger.R;

/**
 * Created by fi8er1 on 18/03/2017.
 */

public class SurveyFragment extends Fragment {

    View baseViewSurveyFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseViewSurveyFragment = inflater.inflate(R.layout.fragment_survey, container, false);

        return baseViewSurveyFragment;
    }


}
