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
import com.byteshaft.locationlogger.utils.AppGlobals;
import com.byteshaft.locationlogger.utils.Helpers;

import static com.byteshaft.locationlogger.MainActivity.fragmentManager;

public class AuthenticationFragmentTwo extends Fragment implements View.OnClickListener {

    View baseViewAuthenticationFragment;
    EditText etAccessCode;
    Button btnAuthenticationFragmentWithdraw;
    Button btnAuthenticationFragmentSubmit;
    Button btnAuthenticationFragmentCancel;
    static boolean isAdversaryTestRequestedForReTaken;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseViewAuthenticationFragment = inflater.inflate(R.layout.fragment_authentication_two, container, false);
        btnAuthenticationFragmentWithdraw = (Button) baseViewAuthenticationFragment.findViewById(R.id.btn_authentication_withdraw_two);
        btnAuthenticationFragmentSubmit = (Button) baseViewAuthenticationFragment.findViewById(R.id.btn_authentication_proceed_two);
        btnAuthenticationFragmentCancel = (Button) baseViewAuthenticationFragment.findViewById(R.id.btn_authentication_cancel_two);
        btnAuthenticationFragmentWithdraw.setOnClickListener(this);
        btnAuthenticationFragmentSubmit.setOnClickListener(this);
        btnAuthenticationFragmentCancel.setOnClickListener(this);
        etAccessCode = (EditText) baseViewAuthenticationFragment.findViewById(R.id.et_authentication_access_code_two);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_authentication_withdraw_two:
                Helpers.AlertDialogWithPositiveFunctionNegativeButton(getActivity(), "Are you sure?",
                        "Proceeding with withdrawal will result in all of your logged data to be lost.",
                        "Yes", "Cancel", Helpers.withdraw);
                break;
            case R.id.btn_authentication_proceed_two:
                if (Helpers.isAccessCodeValid(etAccessCode.getText().toString().trim())) {
                    AppGlobals.putAppStatus(5);
                    Helpers.loadFragment(fragmentManager, new ExitSurveyFragment(), false, null);
                } else {
                    etAccessCode.setError("Code is invalid");
                    Toast.makeText(getActivity(), "Code is invalid", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_authentication_cancel_two:
                Helpers.AlertDialogWithPositiveFunctionNegativeButton(getActivity(), "Are you sure?",
                        "Cancelling will bring you back to the Adversary Retake.",
                        "Continue", "Cancel", Helpers.openAdversaryRetake);
                isAdversaryTestRequestedForReTaken = true;
        }
    }
}
