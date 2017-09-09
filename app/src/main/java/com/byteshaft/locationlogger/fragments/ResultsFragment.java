package com.byteshaft.locationlogger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.byteshaft.locationlogger.R;
import com.byteshaft.locationlogger.utils.AppGlobals;
import com.byteshaft.locationlogger.utils.Helpers;

public class ResultsFragment extends Fragment implements View.OnClickListener {

    View baseViewResultsFragment;
    Button btnResultsWithdraw;
    Button btnResultsFinish;
    TextView tvResultsUsername, tvResultsCorrectAnswers, tvResultsAdversaryCorrectAnswers
            , tvResultsTestTakenByAdversary, tvResultsTimeTakenForTestByUser
            , tvResultsTimeTakenForTestByAdversary;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseViewResultsFragment = inflater.inflate(R.layout.fragment_results, container, false);
        tvResultsUsername = (TextView) baseViewResultsFragment.findViewById(R.id.tv_results_username);
        tvResultsCorrectAnswers = (TextView) baseViewResultsFragment.findViewById(R.id.tv_results_correct_answers);
        tvResultsAdversaryCorrectAnswers = (TextView) baseViewResultsFragment.findViewById(R.id.tv_results_adversary_correct_answers);
        tvResultsTestTakenByAdversary = (TextView) baseViewResultsFragment.findViewById(R.id.tv_results_taken_by_adversary);
        tvResultsTimeTakenForTestByUser = (TextView) baseViewResultsFragment.findViewById(R.id.tv_results_time_taken);
        tvResultsTimeTakenForTestByAdversary = (TextView) baseViewResultsFragment.findViewById(R.id.tv_results_time_taken_by_adversary);

        tvResultsUsername.setText(AppGlobals.getUsername());
        tvResultsCorrectAnswers.setText("Correct Answers: " + AppGlobals.getUserTestResults());

        // if test is taken by adversary then print it's results otherwise leave them
        if (AppGlobals.isTestTakenByAdversary()) {
            tvResultsTestTakenByAdversary.setText("Taken by Adversary : Yes");
            tvResultsAdversaryCorrectAnswers.setText("Adversary Correct Answers: " + AppGlobals.getAdversaryTestResults());
            tvResultsTimeTakenForTestByAdversary.setText("Time Taken: " + AppGlobals.getTimeTakenForTestByAdversary());
        }
        tvResultsTimeTakenForTestByUser.setText("Time taken: " + AppGlobals.getTimeTakenForTestByUser());

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
                AppGlobals.getContext().getSharedPreferences("CREDENTIALS", 0).edit().clear().apply();
                break;
            case R.id.btn_results_finish:
                // setting application status and finishing the current activity
                AppGlobals.putAppStatus(0);
                AppGlobals.putAdversaryAdded(false);
                AppGlobals.testTakenByAdversary(false);
                AppGlobals.putAdversaryTestResults(null);
                AppGlobals.putUserTestResults(null);
                AppGlobals.putAdversaryTestResults(null);
                getActivity().getSharedPreferences("CREDENTIALS", 0).edit().clear().apply();
                QuestionnaireFragment.adversaryMode = false;
                AppGlobals.getContext().getSharedPreferences("CREDENTIALS", 0).edit().clear().apply();
                getActivity().finish();
                break;
        }
    }

}