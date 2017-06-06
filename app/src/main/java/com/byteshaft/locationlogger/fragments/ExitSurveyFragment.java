package com.byteshaft.locationlogger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.byteshaft.locationlogger.R;
import com.byteshaft.locationlogger.utils.AppGlobals;
import com.byteshaft.locationlogger.utils.Helpers;
import com.byteshaft.requests.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import static com.byteshaft.locationlogger.MainActivity.fragmentManager;


public class ExitSurveyFragment extends Fragment implements View.OnClickListener {

    View baseViewExitSurveyFragment;
    Button btnExitSurveyWithdraw;
    Button btnExitSurveySubmit;
//    SubmitDataTask taskSubmitData;
    boolean isSubmitDataTaskRunning;
    private HttpRequest mRequest;

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
//                    taskSubmitData = (SubmitDataTask) new SubmitDataTask().execute();
                    Helpers.showProgressDialog(getActivity(), "Submitting Data...");
                    submitData();
                }
                break;

        }
    }

    private void submitData() {
        // making new httpRequest
        mRequest = new HttpRequest(getActivity());
        mRequest.setOnReadyStateChangeListener(new HttpRequest.OnReadyStateChangeListener() {

            @Override
            public void onReadyStateChange(HttpRequest request, int readyState) {
                switch (readyState) {
                    case HttpRequest.STATE_DONE:
                        switch (request.getStatus()) {
                            case HttpURLConnection.HTTP_CREATED:
                                Helpers.dismissProgressDialog();
                                Toast.makeText(getActivity(), "Data submitted successfully", Toast.LENGTH_SHORT).show();
                                AppGlobals.putAppStatus(5);
                                Helpers.loadFragment(fragmentManager, new ResultsFragment(), false, null);
                                break;
                            case HttpURLConnection.HTTP_BAD_REQUEST:
                                Helpers.dismissProgressDialog();
                                Toast.makeText(getActivity(), "Something is wrong, try again later", Toast.LENGTH_LONG).show();
                                break;
                            case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                                Helpers.dismissProgressDialog();
                                Toast.makeText(getActivity(), "Your internet is not working, make sure you're properly connected to the internet", Toast.LENGTH_LONG).show();
                                break;
                        }
                }
            }
        });
        String data = getDataString(AppGlobals.getUsername(), AppGlobals.getFullName(), AppGlobals.getGender()
                , AppGlobals.getAdversaryName(), AppGlobals.getRelationWithAdversary(), AppGlobals.getUserTestResults()
                , AppGlobals.getAdversaryTestResults(), AppGlobals.getTimeTakenForTestByUser(), AppGlobals.getTimeTakenForTestByAdversary());
        mRequest.open("POST", "http://138.68.145.58/api/survey");
        mRequest.send(data);
        System.out.println("Data: " + data);
    }

    public static String getDataString(String email, String fullName, String gender,
                                       String adversaryName, String adversaryRelation,
                                       String userTestResults, String adversaryTestResults,
                                       String timeTakenForTestByUser, String timeTakenForTestByAdversary) {
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("full_name", fullName);
            json.put("gender", gender);
            json.put("adversary_name", adversaryName);
            json.put("adversary_relation", adversaryRelation);
            json.put("test_results", userTestResults);
            json.put("adversary_test_results", adversaryTestResults);
            json.put("time_taken_by_user", timeTakenForTestByUser);
            json.put("time_taken_by_adversary", timeTakenForTestByAdversary);
            json.put("answers", getAnswersString("Test", "Test", "Test", "Test", "Test", "Test"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static JSONArray getAnswersString(String answer1, String question1, String answer2,
                                          String question2, String answer3, String question3) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("answer", answer1);
            jsonObject.put("question", question1);
            jsonObject.put("answer", answer2);
            jsonObject.put("question", question2);
            jsonObject.put("answer", answer3);
            jsonObject.put("question", question3);

            jsonArray.put(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isSubmitDataTaskRunning) {
//            taskSubmitData.cancel(true);
            Helpers.dismissProgressDialog();
        }
    }

    private boolean validateExitInfo() {
        boolean valid = true;

        return valid;
    }
}
