package com.byteshaft.locationlogger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.byteshaft.locationlogger.R;
import com.byteshaft.locationlogger.services.LocationService;
import com.byteshaft.locationlogger.utils.AppGlobals;
import com.byteshaft.locationlogger.utils.Helpers;

import static com.byteshaft.locationlogger.MainActivity.fragmentManager;

/**
 * Created by fi8er1 on 08/06/2017.
 */

public class AuthenticationFragment extends Fragment implements View.OnClickListener {

    View baseViewAuthenticationFragment;
    EditText etAccessCode;
    Button btnAuthenticationFragmentWithdraw;
    Button btnAuthenticationFragmentSubmit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseViewAuthenticationFragment = inflater.inflate(R.layout.fragment_authentication, container, false);
        btnAuthenticationFragmentWithdraw = (Button) baseViewAuthenticationFragment.findViewById(R.id.btn_authentication_withdraw);
        btnAuthenticationFragmentSubmit = (Button) baseViewAuthenticationFragment.findViewById(R.id.btn_authentication_proceed);
        btnAuthenticationFragmentWithdraw.setOnClickListener(this);
        btnAuthenticationFragmentSubmit.setOnClickListener(this);
        if (LocationService.repeatNotificationTimer != null) {
            LocationService.repeatNotificationTimer.cancel();
        }
        etAccessCode = (EditText) baseViewAuthenticationFragment.findViewById(R.id.et_authentication_access_code);
        etAccessCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etAccessCode.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        return baseViewAuthenticationFragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_authentication_withdraw:
                Helpers.AlertDialogWithPositiveFunctionNegativeButton(getActivity(), "Are you sure?",
                        "Proceeding with withdrawal will result in all of your logged data to be lost.",
                        "Yes", "Cancel", Helpers.withdraw);
                break;
            case R.id.btn_authentication_proceed:
                if (Helpers.isAccessCodeValid(etAccessCode.getText().toString().trim())) {
                    AppGlobals.putAppStatus(3);
                    Helpers.loadFragment(fragmentManager, new QuestionnaireFragment(), false, null);
                } else {
                    etAccessCode.setError("Code is invalid");
                    Toast.makeText(getActivity(), "Code is invalid", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
