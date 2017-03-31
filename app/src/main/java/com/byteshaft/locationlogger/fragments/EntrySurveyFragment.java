package com.byteshaft.locationlogger.fragments;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.byteshaft.locationlogger.MainActivity;
import com.byteshaft.locationlogger.R;
import com.byteshaft.locationlogger.receivers.AlarmReceiver;
import com.byteshaft.locationlogger.utils.AppGlobals;
import com.byteshaft.locationlogger.utils.Helpers;

/**
 * Created by fi8er1 on 27/03/2017.
 */

public class EntrySurveyFragment extends Fragment {

    View baseViewEntryFragment;
    LinearLayout llEntrySpinnerAdversaryRelation;
    Button btnEntryFragmentStart;
    EditText etEntryFragmentAge;
    EditText etEntryFragmentAdversaryName;
    Spinner spinnerEntryFragmentGender;
    Spinner spinnerEntryFragmentRelationWithAdversary;
    String[] spinnerDataGender = {"Male", "Female"};
    String[] spinnerDataRelationWithAdversary = {"Family", "Friend", "Teacher", "Colleague"};
    String sEntryAge;
    String sEntryAdversaryName;
    boolean userIsAddingAdversary;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseViewEntryFragment = inflater.inflate(R.layout.fragment_entry_survey, container, false);

        llEntrySpinnerAdversaryRelation = (LinearLayout) baseViewEntryFragment.findViewById(R.id.ll_entry_spinner_relation_with_adversary);
        etEntryFragmentAge = (EditText) baseViewEntryFragment.findViewById(R.id.et_entry_age);
        etEntryFragmentAdversaryName = (EditText) baseViewEntryFragment.findViewById(R.id.et_entry_friendly_adversary);
        etEntryFragmentAdversaryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 2) {
                    llEntrySpinnerAdversaryRelation.setVisibility(View.VISIBLE);
                    userIsAddingAdversary = true;
                } else {
                    llEntrySpinnerAdversaryRelation.setVisibility(View.GONE);
                    userIsAddingAdversary = false;
                }
            }
        });

        spinnerEntryFragmentGender = (Spinner) baseViewEntryFragment.findViewById(R.id.spinner_entry_gender);
        ArrayAdapter<CharSequence> genderDataAdapter = new ArrayAdapter<CharSequence>(getActivity(), R.layout.spinner_text, spinnerDataGender);
        genderDataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinnerEntryFragmentGender.setAdapter(genderDataAdapter);

        spinnerEntryFragmentRelationWithAdversary = (Spinner) baseViewEntryFragment.findViewById(R.id.spinner_entry_relation_with_adversary);
        ArrayAdapter<CharSequence> adversaryRelationDataAdapter = new ArrayAdapter<CharSequence>(getActivity(), R.layout.spinner_text, spinnerDataRelationWithAdversary);
        adversaryRelationDataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinnerEntryFragmentRelationWithAdversary.setAdapter(adversaryRelationDataAdapter);

        btnEntryFragmentStart = (Button) baseViewEntryFragment.findViewById(R.id.btn_entry_start);
        btnEntryFragmentStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sEntryAge = etEntryFragmentAge.getText().toString().trim();
                sEntryAdversaryName = etEntryFragmentAdversaryName.getText().toString();
                if (validateEntryInfo()) {
                    AppGlobals.putAge(sEntryAge);
                    AppGlobals.putGender(spinnerEntryFragmentGender.getSelectedItem().toString());
                    if (userIsAddingAdversary) {
                        AppGlobals.putAdversaryName(etEntryFragmentAdversaryName.getText().toString());
                        AppGlobals.putRelationWithAdversary(spinnerEntryFragmentRelationWithAdversary.getSelectedItem().toString());
                        AppGlobals.putAdversaryAdded(true);
                    }
                    Helpers.loadFragment(MainActivity.fragmentManager, new WaitingFragment(), false, null);
                    long notificationTime = System.currentTimeMillis() + 30000;
                    AppGlobals.putAlarmTime(notificationTime);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.fragmentManager.popBackStack(null, MainActivity.fragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    }, 500);
                }
            }
        });
        return baseViewEntryFragment;
    }

    private boolean validateEntryInfo() {
        boolean valid = true;

        if (sEntryAge.isEmpty()) {
            etEntryFragmentAge.setError("Empty");
            valid = false;
        } else if (!sEntryAge.trim().isEmpty() && sEntryAge.length() < 2) {
            etEntryFragmentAge.setError("at least 2 Characters");
            valid = false;
        } else if (Integer.parseInt(sEntryAge) < 13) {
            etEntryFragmentAge.setError("minimum age is 13");
            valid = false;
        } else {
            etEntryFragmentAge.setError(null);
        }

        if (sEntryAdversaryName.trim().isEmpty()) {
            etEntryFragmentAdversaryName.setError(null);
        } else if (sEntryAdversaryName.length() < 3) {
            etEntryFragmentAdversaryName.setError("At least 3 characters");
            valid = false;
        } else {
            etEntryFragmentAdversaryName.setError(null);
        }
        return valid;
    }

}
