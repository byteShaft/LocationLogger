package com.byteshaft.locationlogger.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

public class ExitSurveyFragment extends Fragment implements View.OnClickListener {
    
    View baseViewExitSurveyFragment;
    Button btnExitSurveyWithdraw;
    Button btnExitSurveySubmit;
    SubmitDataTask taskSubmitData;
    boolean isSubmitDataTaskRunning;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseViewExitSurveyFragment = inflater.inflate(R.layout.fragment_exit_survey, container, false);

        btnExitSurveyWithdraw = (Button) baseViewExitSurveyFragment.findViewById(R.id.btn_exit_withdraw);
        btnExitSurveyWithdraw.setOnClickListener(this);
        btnExitSurveySubmit = (Button) baseViewExitSurveyFragment.findViewById(R.id.btn_exit_submit);
        btnExitSurveySubmit.setOnClickListener(this);

        return baseViewExitSurveyFragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_exit_withdraw:
                Helpers.AlertDialogWithPositiveFunctionNegativeButton(getActivity(), "Are you sure?",
                        "Proceeding with withdrawal will result in all of your logged data to be lost.",
                        "Yes", "Cancel", Helpers.withdraw);
                break;
            case R.id.btn_exit_submit:
                if (validateExitInfo()) {
                    taskSubmitData = (SubmitDataTask) new SubmitDataTask().execute();
                }
                break;

        }
    }


    private class SubmitDataTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isSubmitDataTaskRunning = true;
            Helpers.showProgressDialog(getActivity(), "Submitting Data");
        }

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isSubmitDataTaskRunning = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Helpers.dismissProgressDialog();
                    Helpers.loadFragment(MainActivity.fragmentManager, new ResultsFragment(), false, null);
                }
            }, 2000);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            isSubmitDataTaskRunning = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isSubmitDataTaskRunning) {
            taskSubmitData.cancel(true);
            Helpers.dismissProgressDialog();
        }
    }

    private boolean validateExitInfo() {
        boolean valid = true;

        return valid;
    }
}
