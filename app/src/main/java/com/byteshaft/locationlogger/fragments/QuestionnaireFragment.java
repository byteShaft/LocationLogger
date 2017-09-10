package com.byteshaft.locationlogger.fragments;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.byteshaft.locationlogger.MainActivity;
import com.byteshaft.locationlogger.R;
import com.byteshaft.locationlogger.utils.AppGlobals;
import com.byteshaft.locationlogger.utils.DatabaseHelpers;
import com.byteshaft.locationlogger.utils.Helpers;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class QuestionnaireFragment extends Fragment implements View.OnClickListener {

    int questionCount;
    public static boolean isQuestionnaireFragmentOpen;
    public static LatLng answerLatLng;
    Marker mapLocationPointMarker;
    Button btnQuestionnaireFragmentWithdraw;
    Button btnQuestionnaireFragmentRemove;
    Button btnQuestionnaireFragmentNext;
    ImageButton btnQuestionnaireFragmentMapType;
    ImageButton btnQuestionnaireFragmentCurrentLocation;
    TextView tvQuestionnaireBottomOverlayOne;
    TextView tvQuestionnaireBottomOverlayTwo;
    TextView tvQuestionnaireBottomOverlayThree;
    LinearLayout llQuestionnaireBottomOverlayThree;
    private boolean mapMarkerAdded;
    SupportMapFragment mapFragment;
    private boolean cameraAnimatedToCurrentLocation;
    View baseViewQuestionnaireFragment = null;
    boolean simpleMapView = true;
    public static boolean adversaryMode;
    private static GoogleMap mMap = null;
    private static LatLng currentLatLngAuto;
    private DatabaseHelpers mDatabaseHelpers;
    private static int correctAnswerCounter = 0;
    long systemTimeInMillisBeforeTest;
    long systemTimeInMillisAfterTest;
    long systemTimeInMillisBeforeTestAdversary;
    long systemTimeInMillisAfterTestAdversary;
    public static int ANSWER_COUNTER = 0;
    public static int ANSWER_COUNTER_ADVERSARY = 0;
    public static StringBuilder sbTimeTakenForEveryQuestionByUser;
    public static StringBuilder sbTimeTakenForEveryQuestionByAdversary;
    public long startQuestionTimeInMillis;
    public long endQuestionTimeInMillis;
    public long startQuestionTimeInMillisAdversary;
    public long endQuestionTimeInMillisAdversary;
    public long timeTakenForAQuestionInMillis;
    public long timeTakenForAQuestionInMillisAdversary;
    LatLng actualLatLng;
    PlaceAutocompleteFragment autocompleteFragment;

    // getting user's location from GoogleMapsApi
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            currentLatLngAuto = new LatLng(location.getLatitude(), location.getLongitude());
            if (!cameraAnimatedToCurrentLocation) {
                cameraAnimatedToCurrentLocation = true;
                // animating camera by giving it the coordinates
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLngAuto, 15.0f));
            }
        }
    };

    public final Runnable adversaryRetake = new Runnable() {
        public void run() {
            AppGlobals.putAppStatus(4);
            systemTimeInMillisBeforeTestAdversary = System.currentTimeMillis();
            startQuestionTimeInMillisAdversary = System.currentTimeMillis();
            adversaryMode = true;
            AppGlobals.putAdversaryAdded(true);
            // resetting correct answer counter before adversary retake
            System.out.println("adversary Retake");
            correctAnswerCounter = 0;
            questionCount = 0;
            tvQuestionnaireBottomOverlayOne.setText("1/10");
            tvQuestionnaireBottomOverlayTwo.setText("Where was i on "
            +mDatabaseHelpers.getRandomRecordFromAllRecords().get(0).get("timestamp"));
        }
    };

    public static final Runnable proceedWithoutAdversary = new Runnable() {
        public void run() {
            Helpers.loadFragment(MainActivity.fragmentManager, new AuthenticationFragmentTwo(), false, null);
            AppGlobals.putAppStatus(7);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (baseViewQuestionnaireFragment != null) {
            ViewGroup parent = (ViewGroup) baseViewQuestionnaireFragment.getParent();
            if (parent != null)
                parent.removeView(baseViewQuestionnaireFragment);
        }
        try {
            baseViewQuestionnaireFragment = inflater.inflate(R.layout.fragment_questionnaire, container, false);
        } catch (InflateException e) {
            Log.e("inflate ", "" + e);
        /* map is already there, just return view as it is */
        }



        // dismiss notification on start of this fragment
        Helpers.dismissNotification();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete);
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                if (mapMarkerAdded) {
                    btnQuestionnaireFragmentRemove.callOnClick();
                }
                searchAnimateCamera(place.getLatLng());
            }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i("place", "An error occurred: " + status);
                }
            });

        // getting current system time before test

        systemTimeInMillisBeforeTest = System.currentTimeMillis();
        startQuestionTimeInMillis = System.currentTimeMillis();
        sbTimeTakenForEveryQuestionByUser = new StringBuilder();
        sbTimeTakenForEveryQuestionByAdversary = new StringBuilder();

        mDatabaseHelpers = new DatabaseHelpers(getActivity());

        tvQuestionnaireBottomOverlayOne = (TextView) baseViewQuestionnaireFragment.findViewById(R.id.tv_map_bottom_overlay_one);
        tvQuestionnaireBottomOverlayTwo = (TextView) baseViewQuestionnaireFragment.findViewById(R.id.tv_map_bottom_overlay_two);

        // setting texts according to the screen
        if (adversaryMode) {
            tvQuestionnaireBottomOverlayTwo.setText("Where was I on " + mDatabaseHelpers.getRandomRecordFromAllRecords().get(0).get("timestamp"));
        } else {
            tvQuestionnaireBottomOverlayTwo.setText("Where were you on " + mDatabaseHelpers.getRandomRecordFromAllRecords().get(0).get("timestamp"));
        }

        tvQuestionnaireBottomOverlayThree = (TextView) baseViewQuestionnaireFragment.findViewById(R.id.tv_map_bottom_overlay_three);

        llQuestionnaireBottomOverlayThree = (LinearLayout) baseViewQuestionnaireFragment.findViewById(R.id.ll_map_bottom_overlay_three);

        btnQuestionnaireFragmentMapType = (ImageButton) baseViewQuestionnaireFragment.findViewById(R.id.btn_map_type);
        btnQuestionnaireFragmentMapType.setOnClickListener(this);
        btnQuestionnaireFragmentCurrentLocation = (ImageButton) baseViewQuestionnaireFragment.findViewById(R.id.btn_map_current_location);
        btnQuestionnaireFragmentCurrentLocation.setOnClickListener(this);

        btnQuestionnaireFragmentWithdraw = (Button) baseViewQuestionnaireFragment.findViewById(R.id.btn_map_bottom_overlay_withdraw);
        btnQuestionnaireFragmentWithdraw.setOnClickListener(this);

        btnQuestionnaireFragmentNext = (Button) baseViewQuestionnaireFragment.findViewById(R.id.btn_map_bottom_overlay_next);
        btnQuestionnaireFragmentNext.setOnClickListener(this);

        // disabling this button for now so that user cannot click on it without marking his location
        // on google maps
        btnQuestionnaireFragmentNext.setEnabled(false);
        btnQuestionnaireFragmentNext.setAlpha(0.5f);

        btnQuestionnaireFragmentRemove = (Button) baseViewQuestionnaireFragment.findViewById(R.id.btn_map_bottom_overlay_remove);
        btnQuestionnaireFragmentRemove.setOnClickListener(this);
        btnQuestionnaireFragmentRemove.setEnabled(false);
        btnQuestionnaireFragmentRemove.setAlpha(0.5f);

        if (AuthenticationFragmentTwo.isAdversaryTestRequestedForReTaken) {
            adversaryRetake.run();
        }

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_for_questionnaire);

        // getting google maps from map fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // if map is ready assign google maps object mMap with the parameters of this override
                // method
                mMap = googleMap;

                // move camera to a pre-defined location
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.653109, -79.388366), 4.0f));
//                    Helpers.showProgressDialog(MainActivity.getInstance(), "Acquiring current location");

                // this method enables users to get his/her location on google maps
                    mMap.setMyLocationEnabled(true);
                // removing the pre-defined current location button from google maps
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    mMap.getUiSettings().setCompassEnabled(true);
                    mMap.setOnMyLocationChangeListener(myLocationChangeListener);
                    mMap.getUiSettings().setMapToolbarEnabled(false);

                // this method detects whenever user long presses on maps
                        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                            @Override
                            public void onMapLongClick(final LatLng latLng) {
                                addAnswerMarkerOnMap(latLng);
                            }
                        });
                    }
                });
        return baseViewQuestionnaireFragment;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_map_bottom_overlay_withdraw:
                Helpers.AlertDialogWithPositiveFunctionNegativeButton(getActivity(), "Are you sure?",
                        "Proceeding with withdrawal will result in all of your logged data to be lost.",
                        "Yes", "Cancel", Helpers.withdraw);
                break;
            case R.id.btn_map_bottom_overlay_next:
                if (!adversaryMode) {
                    ANSWER_COUNTER++;
                    if (ANSWER_COUNTER == 10) {
                        endQuestionTimeInMillis = System.currentTimeMillis();
                        timeTakenForAQuestionInMillis = endQuestionTimeInMillis - startQuestionTimeInMillis;
                        sbTimeTakenForEveryQuestionByUser.append("Q" + ANSWER_COUNTER + ": " + Helpers.getTimeTakenInMinutesAndSeconds(timeTakenForAQuestionInMillis) + "\n");
                        ANSWER_COUNTER = 0;
                        AppGlobals.saveTimeTakenForEachQuestionByUser(sbTimeTakenForEveryQuestionByUser);
                    } else {
                        endQuestionTimeInMillis = System.currentTimeMillis();
                        timeTakenForAQuestionInMillis = endQuestionTimeInMillis - startQuestionTimeInMillis;
                        sbTimeTakenForEveryQuestionByUser.append("Q" + ANSWER_COUNTER + ": " + Helpers.getTimeTakenInMinutesAndSeconds(timeTakenForAQuestionInMillis) + "\n");
                        startQuestionTimeInMillis = System.currentTimeMillis();
                    }

                }

                // for adversary
                if (adversaryMode) {
                    ANSWER_COUNTER_ADVERSARY++;
                    if (ANSWER_COUNTER_ADVERSARY == 10) {
                        endQuestionTimeInMillisAdversary = System.currentTimeMillis();
                        timeTakenForAQuestionInMillisAdversary = endQuestionTimeInMillisAdversary - startQuestionTimeInMillisAdversary;
                        sbTimeTakenForEveryQuestionByAdversary.append("Q" + ANSWER_COUNTER_ADVERSARY + ": " + Helpers.getTimeTakenInMinutesAndSeconds(timeTakenForAQuestionInMillisAdversary) + "\n");
                        ANSWER_COUNTER_ADVERSARY = 0;
                        AppGlobals.saveTimeTakenForEachQuestionByAdversary(sbTimeTakenForEveryQuestionByAdversary);
                    } else {
                        endQuestionTimeInMillisAdversary = System.currentTimeMillis();
                        timeTakenForAQuestionInMillisAdversary = endQuestionTimeInMillisAdversary - startQuestionTimeInMillisAdversary;
                        sbTimeTakenForEveryQuestionByAdversary.append("Q" + ANSWER_COUNTER_ADVERSARY + ": " + Helpers.getTimeTakenInMinutesAndSeconds(timeTakenForAQuestionInMillisAdversary) + "\n");
                        startQuestionTimeInMillisAdversary = System.currentTimeMillis();
                    }
                }

                if (mapMarkerAdded) {
                    mapLocationPointMarker.remove();
                    mapMarkerAdded = false;
                    if (questionCount < 9) {
                        // incrementing question count
                        questionCount++;
                        llQuestionnaireBottomOverlayThree.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tvQuestionnaireBottomOverlayThree.setText("Tap and hold to set a point");
                                llQuestionnaireBottomOverlayThree.setVisibility(View.VISIBLE);
                            }
                        }, 750);
                        btnQuestionnaireFragmentNext.setEnabled(false);
                        btnQuestionnaireFragmentNext.setAlpha(0.5f);
                        btnQuestionnaireFragmentRemove.setEnabled(false);
                        btnQuestionnaireFragmentRemove.setAlpha(0.5f);
                        tvQuestionnaireBottomOverlayOne.setText(questionCount + 1 + "/10");
                        if (adversaryMode) {
                            tvQuestionnaireBottomOverlayTwo.setText("Where was I on "
                                    + mDatabaseHelpers.getRandomRecordFromAllRecords().get(0).get("timestamp"));
                        } else {
                            tvQuestionnaireBottomOverlayTwo.setText("Where were you on "
                                    + mDatabaseHelpers.getRandomRecordFromAllRecords().get(0).get("timestamp"));
                        }

                    } else {

                        if (!adversaryMode) {
                            systemTimeInMillisAfterTest = System.currentTimeMillis();
                            // calculating test time taken by the user
                            long timeTakenForTestInMillis = systemTimeInMillisAfterTest - systemTimeInMillisBeforeTest;
                            String timeTakenForTestByUser = Helpers.getTimeTakenInMinutesAndSeconds(timeTakenForTestInMillis);
                            // saving test time taken by the user in database
                            AppGlobals.putTimeTakenForTestByUser(timeTakenForTestByUser);
                            // saving correct answers in database
                            correctAnswerCounter ++;
                            AppGlobals.putUserTestResults(correctAnswerCounter + "/10");
                            AppGlobals.testTakenByAdversary(false);
                            System.out.println("Condition true");
                            correctAnswerCounter = 0;
                        } else {
                            systemTimeInMillisAfterTestAdversary = System.currentTimeMillis();
                            // calculating time taken by the adversary for test
                            long timeTakenForTestInMillisAdv = systemTimeInMillisAfterTestAdversary - systemTimeInMillisBeforeTestAdversary;
                            String timeTakenForTestByAdv = Helpers.getTimeTakenInMinutesAndSeconds(timeTakenForTestInMillisAdv);
                            // saving adversary test time in database
                            if (Helpers.isAnswerLocationInVicinityOfActualLocation(answerLatLng, actualLatLng)) {
                                correctAnswerCounter++;
                                System.out.println("Correct Answer Counter: " + correctAnswerCounter);
                            }
                            AppGlobals.putTimeTakenForTestByAdversary(timeTakenForTestByAdv);
                            AppGlobals.putAdversaryTestResults(correctAnswerCounter + "/10");
                            AppGlobals.testTakenByAdversary(true);
                            correctAnswerCounter = 0;
                        }
                        if (!adversaryMode) {
                            Helpers.AlertDialogWithPositiveNegativeFunctions(getActivity(), "Adversary Retake",
                                    "Do you want any friendly adversary to guess your location?", "Yes", "No",
                                    adversaryRetake, proceedWithoutAdversary);
                        } else {
                            Helpers.loadFragment(MainActivity.fragmentManager, new ExitSurveyFragment(), false, null);
                            AppGlobals.putAppStatus(5);
                        }
                    }

                    // getting latitude and longitude and converting them to latlng object in order to
                    // compare results
                    actualLatLng = new LatLng(Double.parseDouble(mDatabaseHelpers.getRandomRecordFromAllRecords().get(0).
                            get("latitude")), Double.parseDouble(mDatabaseHelpers.getRandomRecordFromAllRecords().get(0).
                            get("longitude")));
                    if (Helpers.isAnswerLocationInVicinityOfActualLocation(answerLatLng, actualLatLng)) {
                        correctAnswerCounter++;
                        System.out.println("Correct Answer Counter: " + correctAnswerCounter);
                    }
                }
                break;
            case R.id.btn_map_bottom_overlay_remove:
                mapLocationPointMarker.remove();
                mapMarkerAdded = false;
                llQuestionnaireBottomOverlayThree.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvQuestionnaireBottomOverlayThree.setText("Tap and hold to set a point");
                        llQuestionnaireBottomOverlayThree.setVisibility(View.VISIBLE);
                    }
                }, 750);
                btnQuestionnaireFragmentNext.setEnabled(false);
                btnQuestionnaireFragmentNext.setAlpha(0.5f);
                btnQuestionnaireFragmentRemove.setEnabled(false);
                btnQuestionnaireFragmentRemove.setAlpha(0.5f);
                break;
            case R.id.btn_map_current_location:
                if (Helpers.isAnyLocationServiceAvailable()) {
                    if (mMap != null) {
                        if (currentLatLngAuto != null) {
                            CameraPosition cameraPosition =
                                    new CameraPosition.Builder()
                                            .target(currentLatLngAuto)
                                            .zoom(16.0f)
                                            .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        } else {
                            Toast.makeText(getActivity(), "Error: Location not available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error: Map not ready", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error: Location Service disabled", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_map_type:
                if (simpleMapView) {
                    // simple map type
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    btnQuestionnaireFragmentMapType.setImageResource(R.drawable.selector_map_type_simple);
                    simpleMapView = false;
                } else {
                    // satellite map type
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    btnQuestionnaireFragmentMapType.setImageResource(R.drawable.selector_map_type_satellite);
                    simpleMapView = true;
                }
                break;
        }
    }

    protected void searchAnimateCamera(final LatLng address) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(address, 15.0f), new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        addAnswerMarkerOnMap(address);
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getActivity(), "Search interrupted", Toast.LENGTH_SHORT).show();
                    }
                });
                mMap.clear();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        isQuestionnaireFragmentOpen = false;
        correctAnswerCounter = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        isQuestionnaireFragmentOpen = true;
    }

    private void addAnswerMarkerOnMap(LatLng latLng) {
        if (!mapMarkerAdded) {
            answerLatLng = latLng;
            mapLocationPointMarker = mMap.addMarker(new MarkerOptions().position(latLng));
            mapMarkerAdded = true;
            llQuestionnaireBottomOverlayThree.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvQuestionnaireBottomOverlayThree.setText("Location Marked - Continue");
                    llQuestionnaireBottomOverlayThree.setVisibility(View.VISIBLE);
                }
            }, 750);
            btnQuestionnaireFragmentNext.setEnabled(true);
            btnQuestionnaireFragmentNext.setAlpha(1.0f);
            btnQuestionnaireFragmentRemove.setEnabled(true);
            btnQuestionnaireFragmentRemove.setAlpha(1.0f);
        }
    }
}
