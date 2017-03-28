package com.byteshaft.locationlogger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.byteshaft.locationlogger.R;
import com.byteshaft.locationlogger.utils.AppGlobals;
import com.byteshaft.locationlogger.utils.Helpers;

/**
 * Created by fi8er1 on 27/03/2017.
 */

public class ResultsFragment extends Fragment implements View.OnClickListener {

    View baseViewResultsFragment;
    Button btnResultsWithdraw;
    Button btnResultsFinish;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseViewResultsFragment = inflater.inflate(R.layout.fragment_results, container, false);

        btnResultsWithdraw = (Button) baseViewResultsFragment.findViewById(R.id.btn_results_withdraw);
        btnResultsWithdraw.setOnClickListener(this);

        btnResultsFinish = (Button) baseViewResultsFragment.findViewById(R.id.btn_results_finish);
        btnResultsFinish.setOnClickListener(this);
        return baseViewResultsFragment;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_results_withdraw:
                Helpers.withdraw.run();
                break;
            case R.id.btn_results_finish:
                AppGlobals.putAppStatus(0);
                AppGlobals.putAdversaryAdded(false);
                getActivity().finish();
                break;

        }
    }

}
