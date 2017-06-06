package com.byteshaft.locationlogger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.byteshaft.locationlogger.MainActivity;
import com.byteshaft.locationlogger.R;
import com.byteshaft.locationlogger.utils.AppGlobals;
import com.byteshaft.locationlogger.utils.Helpers;

import static com.byteshaft.locationlogger.utils.Helpers.openLocationServiceSettings;
import static com.byteshaft.locationlogger.utils.Helpers.recheckLocationServiceStatus;

public class WelcomeFragment extends Fragment {

    View baseViewWelcomeFragment;

    EditText etLoginEmail;
    EditText etLoginFullName;
    Button btnLoginNext;
    CheckBox cbLoginTerms;

    String sEmail;
    String sFullName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseViewWelcomeFragment = inflater.inflate(R.layout.fragment_welcome, container, false);

        etLoginEmail = (EditText) baseViewWelcomeFragment.findViewById(R.id.et_login_email);
        etLoginFullName = (EditText) baseViewWelcomeFragment.findViewById(R.id.et_login_full_name);
        btnLoginNext = (Button) baseViewWelcomeFragment.findViewById(R.id.btn_login_next);
        cbLoginTerms = (CheckBox) baseViewWelcomeFragment.findViewById(R.id.cb_terms_of_service_check);

        cbLoginTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // checking permission for devices on Marshmallow or greater and requesting them
                    // if not already given
                    if (Helpers.hasPermissionsForDevicesAboveMarshmallowIfNotRequestPermissions(getActivity())) {
                        // checking if any service of location is turned on
                        if (!Helpers.isAnyLocationServiceAvailable()) {
                            // if not then show an alert
                            Helpers.AlertDialogWithPositiveNegativeNeutralFunctions(MainActivity.getInstance(),
                                    "Location Service disabled", "Enable device GPS to continue", "Settings", "ReCheck", "Dismiss",
                                    openLocationServiceSettings, recheckLocationServiceStatus);
                        }
                    }
                }
            }
        });

        btnLoginNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sEmail = etLoginEmail.getText().toString();
                sFullName = etLoginFullName.getText().toString();
                if (validateLoginInfo()) {
                    if (Helpers.hasPermissionsForDevicesAboveMarshmallowIfNotRequestPermissions(getActivity())) {
                        if (!Helpers.isAnyLocationServiceAvailable()) {
                            Helpers.AlertDialogWithPositiveNegativeNeutralFunctions(MainActivity.getInstance(),
                                    "Location Service disabled", "Enable device GPS to continue", "Settings", "ReCheck", "Dismiss",
                                    openLocationServiceSettings, recheckLocationServiceStatus);
                        } else {
                            Helpers.loadFragment(MainActivity.fragmentManager, new InstructionsFragment(), false, "Instructions");
                            AppGlobals.putUserName(sEmail);
                            AppGlobals.putFullName(sFullName);
                        }
                    } else {
                        // if permission's denied make a toast
                        Toast.makeText(getActivity(), "Location permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return baseViewWelcomeFragment;
    }

    // this method checks for valid user input
    private boolean validateLoginInfo() {
        boolean valid = true;

        // trim blank space and check if it's still empty
        if (sEmail.trim().isEmpty()) {
            etLoginEmail.setError("Empty");
            valid = false;

            // if email address is not empty then check if it's valid or not
        } else if (!sEmail.trim().isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()) {
            etLoginEmail.setError("Invalid E-Mail");
            valid = false;
        } else {
            etLoginEmail.setError(null);
        }

        if (sFullName.trim().isEmpty()) {
            etLoginFullName.setError("Empty");
            valid = false;
        } else if (sFullName.length() < 3) {
            etLoginFullName.setError("At least 3 characters");
            valid = false;
        } else {
            etLoginFullName.setError(null);
        }

        if (valid && !cbLoginTerms.isChecked()) {
            Toast.makeText(getActivity(), "Please Agree to our Terms of Service before continuing", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

}
