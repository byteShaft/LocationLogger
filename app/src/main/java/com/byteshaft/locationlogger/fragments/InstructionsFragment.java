package com.byteshaft.locationlogger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.byteshaft.locationlogger.MainActivity;
import com.byteshaft.locationlogger.R;
import com.byteshaft.locationlogger.utils.Helpers;

/**
 * Created by fi8er1 on 27/03/2017.
 */

public class InstructionsFragment extends Fragment {

    View baseViewInstructionsFragment;
    Button btnInstructionsFragmentNext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseViewInstructionsFragment = inflater.inflate(R.layout.fragment_instructions, container, false);

        btnInstructionsFragmentNext = (Button) baseViewInstructionsFragment.findViewById(R.id.btn_instructions_next);
        btnInstructionsFragmentNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helpers.loadFragment(MainActivity.fragmentManager, new EntrySurveyFragment(), false, "Entry");
            }
        });
        return baseViewInstructionsFragment;
    }
}
