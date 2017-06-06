package com.byteshaft.locationlogger.fragments;

import android.os.Bundle;
import android.os.Handler;
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
import com.byteshaft.locationlogger.utils.AppGlobals;
import com.byteshaft.locationlogger.utils.Helpers;

public class EntrySurveyFragment extends Fragment {

    View baseViewEntryFragment;
    LinearLayout llEntrySpinnerAdversaryRelation;
    Button btnEntryFragmentStart;
    EditText etEntryFragmentAge;
    EditText etEntryFragmentAdversaryName;
    Spinner spinnerEntryFragmentGender;
    Spinner spinnerEntryFragmentRelationWithAdversary;

    // String arrays for populating data in dropdown menu
    String[] spinnerDataGender = {"Male", "Female"};
    String[] spinnerDataRelationWithAdversary = {"Family", "Friend", "Teacher", "Colleague"};
    String sEntryAge;
    String sEntryAdversaryName;
    boolean userIsAddingAdversary;

    // this method gets called whenever a fragment is initialized
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflating fragment layout from xml file
        baseViewEntryFragment = inflater.inflate(R.layout.fragment_entry_survey, container, false);

        llEntrySpinnerAdversaryRelation = (LinearLayout) baseViewEntryFragment.findViewById(R.id.ll_entry_spinner_relation_with_adversary);

        // Assigning the view to edittext using it's ID
        etEntryFragmentAge = (EditText) baseViewEntryFragment.findViewById(R.id.et_entry_age);
        etEntryFragmentAdversaryName = (EditText) baseViewEntryFragment.findViewById(R.id.et_entry_friendly_adversary);
        // declaring listener which continuously listens for text changes in edittext
        etEntryFragmentAdversaryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                // if the length of the text exceeds 2 words then make the relationship field visible
                if (editable.length() > 2) {
                    llEntrySpinnerAdversaryRelation.setVisibility(View.VISIBLE);
                    userIsAddingAdversary = true;
                } else {
                    // if not make it disappear
                    llEntrySpinnerAdversaryRelation.setVisibility(View.GONE);
                    userIsAddingAdversary = false;
                }
            }
        });

        spinnerEntryFragmentGender = (Spinner) baseViewEntryFragment.findViewById(R.id.spinner_entry_gender);
        ArrayAdapter<CharSequence> genderDataAdapter = new ArrayAdapter<CharSequence>(getActivity(), R.layout.spinner_text, spinnerDataGender);
        genderDataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        // setting custom adapter for dropdown menu in which we select gender
        spinnerEntryFragmentGender.setAdapter(genderDataAdapter);

        spinnerEntryFragmentRelationWithAdversary = (Spinner) baseViewEntryFragment.findViewById(R.id.spinner_entry_relation_with_adversary);
        ArrayAdapter<CharSequence> adversaryRelationDataAdapter = new ArrayAdapter<CharSequence>(getActivity(), R.layout.spinner_text, spinnerDataRelationWithAdversary);
        adversaryRelationDataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinnerEntryFragmentRelationWithAdversary.setAdapter(adversaryRelationDataAdapter);

        btnEntryFragmentStart = (Button) baseViewEntryFragment.findViewById(R.id.btn_entry_start);
        // setting onClick listener for start button
        btnEntryFragmentStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting text from edittext and trimming the blank spaces
                sEntryAge = etEntryFragmentAge.getText().toString().trim();
                sEntryAdversaryName = etEntryFragmentAdversaryName.getText().toString();
                // validating information from all text fields
                if (validateEntryInfo()) {
                    // if validated then putting all the given data in sharedpreferences
                    AppGlobals.putAge(sEntryAge);
                    AppGlobals.putGender(spinnerEntryFragmentGender.getSelectedItem().toString());
                    if (userIsAddingAdversary) {
                        AppGlobals.putAdversaryName(etEntryFragmentAdversaryName.getText().toString());
                        AppGlobals.putRelationWithAdversary(spinnerEntryFragmentRelationWithAdversary.getSelectedItem().toString());
                        AppGlobals.putAdversaryAdded(true);
                    }
                    Helpers.loadFragment(MainActivity.fragmentManager, new WaitingFragment(), false, null);
                    // getting system time and adding the time of two weeks in milliseconds in order
                    // to send notification after two weeks
                    long notificationTime = System.currentTimeMillis() + 1209600000;
                    AppGlobals.putNotificationTime(notificationTime);
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

    // this method is checking for the valid entries in all text fields
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
