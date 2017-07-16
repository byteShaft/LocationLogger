package com.byteshaft.locationlogger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
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
    SeekBar sbExitQuestionOne;
    boolean isSubmitDataTaskRunning;
    private HttpRequest mRequest;

    TextView tvExitQuestionOneRating;

    Switch switchExitQuestionTwo;
    EditText etExitQuestionTwo;

    SeekBar sbExitQuestionThreePartOne;
    TextView tvExitQuestionThreePartOneRating;

    SeekBar sbExitQuestionThreePartTwo;
    TextView tvExitQuestionThreePartTwoRating;

    SeekBar sbExitQuestionThreePartThree;
    TextView tvExitQuestionThreePartThreeRating;

    SeekBar sbExitQuestionFourPartOne;
    TextView tvExitQuestionFourPartOneRating;

    SeekBar sbExitQuestionFourPartTwo;
    TextView tvExitQuestionFourPartTwoRating;

    SeekBar sbExitQuestionFourPartThree;
    TextView tvExitQuestionFourPartThreeRating;

    SeekBar sbExitQuestionFivePartOne;
    TextView tvExitQuestionFivePartOneRating;

    SeekBar sbExitQuestionFivePartTwo;
    TextView tvExitQuestionFivePartTwoRating;

    SeekBar sbExitQuestionFivePartThree;
    TextView tvExitQuestionFivePartThreeRating;

    SeekBar sbExitQuestionFivePartFour;
    TextView tvExitQuestionFivePartFourRating;

    SeekBar sbExitQuestionFivePartFive;
    TextView tvExitQuestionFivePartFiveRating;

    SeekBar sbExitQuestionFivePartSix;
    TextView tvExitQuestionFivePartSixRating;

    SeekBar sbExitQuestionFivePartSeven;
    TextView tvExitQuestionFivePartSevenRating;

    SeekBar sbExitQuestionFivePartEight;
    TextView tvExitQuestionFivePartEightRating;

    SeekBar sbExitQuestionSix;
    TextView tvExitQuestionSixRating;

    SeekBar sbExitQuestionSeven;
    TextView tvExitQuestionSevenRating;

    SeekBar sbExitQuestionEight;
    TextView tvExitQuestionEightRating;

    EditText etExitQuestionNine;


    public final Runnable submit = new Runnable() {
        public void run() {
            Helpers.showProgressDialog(getActivity(), "Submitting Data...");
            submitData();
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseViewExitSurveyFragment = inflater.inflate(R.layout.fragment_exit_survey, container, false);

        sbExitQuestionOne = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_one);
        sbExitQuestionOne.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionOneRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionOneRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_one_rating);

        switchExitQuestionTwo = (Switch) baseViewExitSurveyFragment.findViewById(R.id.switch_exit_question_two);
        switchExitQuestionTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    etExitQuestionTwo.setVisibility(View.VISIBLE);
                    switchExitQuestionTwo.setText("Yes");
                } else {
                    etExitQuestionTwo.setVisibility(View.GONE);
                    etExitQuestionTwo.setText("");
                    switchExitQuestionTwo.setText("No");
                }
            }
        });
        etExitQuestionTwo = (EditText) baseViewExitSurveyFragment.findViewById(R.id.et_exit_question_two);

        sbExitQuestionThreePartOne = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_three_part_one);
        sbExitQuestionThreePartOne.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionThreePartOneRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionThreePartOneRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_three_part_one_rating);

        sbExitQuestionThreePartTwo = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_three_part_two);
        sbExitQuestionThreePartTwo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionThreePartTwoRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionThreePartTwoRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_three_part_two_rating);

        sbExitQuestionThreePartThree = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_three_part_three);
        sbExitQuestionThreePartThree.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionThreePartThreeRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionThreePartThreeRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_three_part_three_rating);



        sbExitQuestionFourPartOne = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_four_part_one);
        sbExitQuestionFourPartOne.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionFourPartOneRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionFourPartOneRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_four_part_one_rating);

        sbExitQuestionFourPartTwo = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_four_part_two);
        sbExitQuestionFourPartTwo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionFourPartTwoRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionFourPartTwoRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_four_part_two_rating);

        sbExitQuestionFourPartThree = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_four_part_three);
        sbExitQuestionFourPartThree.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionFourPartThreeRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionFourPartThreeRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_four_part_three_rating);





        sbExitQuestionFivePartOne = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_five_part_one);
        sbExitQuestionFivePartOne.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionFivePartOneRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionFivePartOneRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_five_part_one_rating);

        sbExitQuestionFivePartTwo = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_five_part_two);
        sbExitQuestionFivePartTwo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionFivePartTwoRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionFivePartTwoRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_five_part_two_rating);

        sbExitQuestionFivePartThree = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_five_part_three);
        sbExitQuestionFivePartThree.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionFivePartThreeRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionFivePartThreeRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_five_part_three_rating);

        sbExitQuestionFivePartFour = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_five_part_four);
        sbExitQuestionFivePartFour.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionFivePartFourRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionFivePartFourRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_five_part_four_rating);


        sbExitQuestionFivePartFive = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_five_part_five);
        sbExitQuestionFivePartFive.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionFivePartFiveRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionFivePartFiveRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_five_part_five_rating);


        sbExitQuestionFivePartSix = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_five_part_six);
        sbExitQuestionFivePartSix.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionFivePartSixRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionFivePartSixRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_five_part_six_rating);



        sbExitQuestionFivePartSeven = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_five_part_seven);
        sbExitQuestionFivePartSeven.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionFivePartSevenRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionFivePartSevenRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_five_part_seven_rating);

        sbExitQuestionFivePartEight = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_five_part_eight);
        sbExitQuestionFivePartEight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionFivePartEightRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionFivePartEightRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_five_part_eight_rating);

        sbExitQuestionSix = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_six);
        sbExitQuestionSix.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionSixRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionSixRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_six_rating);

        sbExitQuestionSeven = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_seven);
        sbExitQuestionSeven.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionSevenRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionSevenRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_seven_rating);


        sbExitQuestionEight = (SeekBar) baseViewExitSurveyFragment.findViewById(R.id.sb_exit_question_eight);
        sbExitQuestionEight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekBar.setProgress(1);
                    i = 1;
                }
                tvExitQuestionEightRating.setText(i + "/7");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        tvExitQuestionEightRating = (TextView) baseViewExitSurveyFragment.findViewById(R.id.tv_exit_question_eight_rating);

        etExitQuestionNine = (EditText) baseViewExitSurveyFragment.findViewById(R.id.et_exit_question_nine);

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
                Helpers.AlertDialogWithPositiveFunctionNegativeButton(getActivity(), "Survey filled?",
                        "Are you sure you have supplied survey info and want to proceed with submission?",
                        "Yes", "Cancel", submit);
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
                        Log.e("response", "" + request.getResponseText());
                        switch (request.getStatus()) {
                            case HttpURLConnection.HTTP_CREATED:
                                Helpers.dismissProgressDialog();
                                Toast.makeText(getActivity(), "Data submitted successfully", Toast.LENGTH_SHORT).show();
                                AppGlobals.putAppStatus(6);
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
        mRequest.open("POST", "http://138.68.145.58/api/survey");
        mRequest.send(getDataString(AppGlobals.getUsername(), AppGlobals.getFullName(), "null",
                "null", "null", AppGlobals.getUserTestResults(),
                AppGlobals.getAdversaryTestResults(), AppGlobals.getTimeTakenForTestByUser(), AppGlobals.getTimeTakenForTestByAdversary()));
    }

    public String getDataString(String email, String fullName, String gender,
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
            json.put("time_taken_by_user", timeTakenForTestByUser + "\n" + AppGlobals.getTimeTakenForEachQuestionByUser());
            json.put("time_taken_by_adversary", timeTakenForTestByAdversary + "\n" + AppGlobals.getTimeTakenForEachQuestionByAdversary());
            json.put("answers", getAnswersString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public JSONArray getSurveyString() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Q1", sbExitQuestionOne.getProgress() + "/7");
            jsonObject.put("Q2", "(" + switchExitQuestionTwo.isChecked() + ") " + etExitQuestionTwo.getText().toString());
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    public JSONArray getAnswersString() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("answer", sbExitQuestionOne.getProgress() + "/7");
            jsonObject.put("question", "Scale the system's ease of use");
            jsonArray.put(jsonObject);
            jsonObject = new JSONObject();

            if (switchExitQuestionTwo.isChecked()) {
                jsonObject.put("answer", "Yes: " + etExitQuestionTwo.getText().toString());
            } else {
                jsonObject.put("answer", "No");
            }
            jsonObject.put("question", "Did you write down or record any information that would help you use this system?");
            jsonArray.put(jsonObject);
            jsonObject = new JSONObject();

            jsonObject.put("answer", "Security questions: " + sbExitQuestionThreePartOne.getProgress() + "/7\n"
            + "Email resets: " + sbExitQuestionThreePartTwo.getProgress() + "/7\n"
            + "SMS resets: " + sbExitQuestionThreePartThree.getProgress() + "/7");
            jsonObject.put("question", "For a mobile device’s forgotten password/PIN or failed biometric, to what extent would you prefer to use this system in lieu of any of the following fallback authentication methods?");
            jsonArray.put(jsonObject);
            jsonObject = new JSONObject();

            jsonObject.put("answer", "Security questions: " + sbExitQuestionFourPartOne.getProgress() + "/7\n"
            + "Email resets: " + sbExitQuestionFourPartTwo.getProgress() + "/7\n"
            + "SMS resets: " + sbExitQuestionFourPartThree.getProgress() + "/7");
            jsonObject.put("question", "For an online account’s forgotten password, to what extent would you prefer to use this system in lieu of any of the following fallback authentication methods?");
            jsonArray.put(jsonObject);
            jsonObject = new JSONObject();

            jsonObject.put("answer", "Email account: " + sbExitQuestionFivePartOne.getProgress() + "/7\n"
            +"Online Bank Account: " + sbExitQuestionFivePartTwo.getProgress() + "/7\n"
            +"Social Networking account: " + sbExitQuestionFivePartThree.getProgress() + "/7\n"
            +"E-commerce account: " + sbExitQuestionFivePartFour.getProgress() + "/7\n"
            +"Other infrequently accessed accounts: " + sbExitQuestionFivePartFive.getProgress() + "/7\n"
            +"Mobile device: " + sbExitQuestionFivePartSix.getProgress() + "/7\n"
            +"University account: " + sbExitQuestionFivePartSeven.getProgress() + "/7\n"
            +"Job related account: " + sbExitQuestionFivePartEight.getProgress() + "/7");
            jsonObject.put("question", "Please rate your agreement with the following statements.  I would prefer to use the [GeoSQ/GeoPassHints] system to login to any of the following accounts, instead of entering a password, for this account type:");
            jsonArray.put(jsonObject);
            jsonObject = new JSONObject();

            jsonObject.put("answer", sbExitQuestionSix.getProgress() + "/7");
            jsonObject.put("question", "If you were to use this system, how likely do you think it is that someone you know could access your device/accounts without consent?");
            jsonArray.put(jsonObject);
            jsonObject = new JSONObject();

            jsonObject.put("answer", sbExitQuestionSeven.getProgress() + "/7");
            jsonObject.put("question", "How concerned are you about a privacy leak on any medium (e.g. Facebook, contact information, pictures)?");
            jsonArray.put(jsonObject);
            jsonObject = new JSONObject();

            jsonObject.put("answer", sbExitQuestionEight.getProgress() + "/7");
            jsonObject.put("question", "To what extent are you concerned of a privacy leak as a result of applications utilizing location services (e.g., Google Locations, GeoSQ, etc.)?");
            jsonArray.put(jsonObject);
            jsonObject = new JSONObject();

            jsonObject.put("answer", "." + etExitQuestionNine.getText().toString().trim());
            jsonObject.put("question", "Do you have any additional feedback or comments about the system?");
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
}
