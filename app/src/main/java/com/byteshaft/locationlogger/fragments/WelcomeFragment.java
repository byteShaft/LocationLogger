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

/**
 * Created by fi8er1 on 18/03/2017.
 */

public class WelcomeFragment extends Fragment {

    View baseViewWelcomeFragment;

    EditText etLoginEmail;
    EditText etLoginFullName;
    Button btnLoginStart;
    CheckBox cbLoginTerms;

    String sEmail;
    String sFullName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseViewWelcomeFragment = inflater.inflate(R.layout.fragment_welcome, container, false);

        etLoginEmail = (EditText) baseViewWelcomeFragment.findViewById(R.id.et_login_email);
        etLoginFullName = (EditText) baseViewWelcomeFragment.findViewById(R.id.et_login_full_name);
        btnLoginStart = (Button) baseViewWelcomeFragment.findViewById(R.id.btn_login_start);
        cbLoginTerms = (CheckBox) baseViewWelcomeFragment.findViewById(R.id.cb_terms_of_service_check);

        cbLoginTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (Helpers.hasPermissionsForDevicesAboveMarshmallowIfNotRequestPermissions(getActivity())) {
                        if (!Helpers.isAnyLocationServiceAvailable()) {
                            Helpers.AlertDialogWithPositiveNegativeNeutralFunctions(MainActivity.getInstance(),
                                    "Location Service disabled", "Enable device GPS to continue", "Settings", "ReCheck", "Dismiss",
                                    openLocationServiceSettings, recheckLocationServiceStatus);
                        }
                    }
                }
            }
        });

        btnLoginStart.setOnClickListener(new View.OnClickListener() {
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
                            Helpers.loadFragment(MainActivity.fragmentManager, new InstructionsFragment(), false);
                            AppGlobals.putAppStatus(1);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Location permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return baseViewWelcomeFragment;
    }

    public boolean validateLoginInfo() {
        boolean valid = true;

        if (sEmail.trim().isEmpty()) {
            etLoginEmail.setError("Empty");
            valid = false;
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
            Toast.makeText(getActivity(), "Check terms of service", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

}
