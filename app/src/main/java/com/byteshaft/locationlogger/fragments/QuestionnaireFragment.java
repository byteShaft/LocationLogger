package com.byteshaft.locationlogger.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.byteshaft.locationlogger.MainActivity;
import com.byteshaft.locationlogger.R;
import com.byteshaft.locationlogger.utils.AppGlobals;
import com.byteshaft.locationlogger.utils.DatabaseHelpers;
import com.byteshaft.locationlogger.utils.Helpers;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuestionnaireFragment extends Fragment implements View.OnClickListener {

    int questionCount;
    public static boolean isQuestionnaireFragmentOpen;
    public static LatLng answerLatLng;
    Marker mapLocationPointMarker;
    Button btnQuestionnaireFragmentWithdraw;
    Button btnQuestionnaireFragmentRemove;
    Button btnQuestionnaireFragmentNext;
    ImageButton btnQuestionnaireFragmentSearch;
    ImageButton btnQuestionnaireFragmentMapType;
    ImageButton btnQuestionnaireFragmentCurrentLocation;
    TextView tvQuestionnaireBottomOverlayOne;
    TextView tvQuestionnaireBottomOverlayTwo;
    TextView tvQuestionnaireBottomOverlayThree;
    LinearLayout llQuestionnaireBottomOverlayThree;
    boolean isSearchEditTextVisible;
    private String inputMapSearch;
    private boolean mapMarkerAdded;
    private boolean cameraAnimatedToCurrentLocation;
    View baseViewQuestionnaireFragment;
    EditText etMapSearch;
    boolean simpleMapView = true;
    public static boolean adversaryMode;
    private Animation animLayoutMapSearchBarFadeOut;
    private Animation animLayoutMapSearchBarFadeIn;
    private static GoogleMap mMap = null;
    private static LatLng currentLatLngAuto;
    private DatabaseHelpers mDatabaseHelpers;
    private static int correctAnswerCounter = 0;
    long systemTimeInMillisBeforeTest;
    long systemTimeInMillisAfterTest;
    long systemTimeInMillisBeforeTestAdversary;
    long systemTimeInMillisAfterTestAdversary;

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
            adversaryMode = true;
            // resetting correct answer counter before adversary retake
            correctAnswerCounter = 0;
            questionCount = 0;
            tvQuestionnaireBottomOverlayOne.setText("1/10");
            tvQuestionnaireBottomOverlayTwo.setText("Where was i on "
            +mDatabaseHelpers.getRandomRecordFromAllRecords().get(0).get("timestamp"));
        }
    };

    public static final Runnable proceedWithoutAdversary = new Runnable() {
        public void run() {
            Helpers.loadFragment(MainActivity.fragmentManager, new ExitSurveyFragment(), false, null);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseViewQuestionnaireFragment = inflater.inflate(R.layout.fragment_questionnaire, container, false);
        // dismiss notification on start of this fragment
        Helpers.dismissNotification();

        // getting current system time before test
        systemTimeInMillisBeforeTest = System.currentTimeMillis();

        animLayoutMapSearchBarFadeOut = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
        animLayoutMapSearchBarFadeIn = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);

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

        btnQuestionnaireFragmentSearch = (ImageButton) baseViewQuestionnaireFragment.findViewById(R.id.btn_map_search);
        btnQuestionnaireFragmentSearch.setOnClickListener(this);
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

        etMapSearch = (EditText) baseViewQuestionnaireFragment.findViewById(R.id.et_map_search);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
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
                                if (isSearchEditTextVisible) {
                                    setSearchBarVisibility(false);
                                }
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
                        });
                    }
                });

        etMapSearch.addTextChangedListener(new TextWatcher() {
            Timer textChangeTimer = new Timer();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textChangeTimer.cancel();
                inputMapSearch = etMapSearch.getText().toString();
                if (inputMapSearch.length() > 2) {
                    textChangeTimer = new Timer();
                    textChangeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (isQuestionnaireFragmentOpen) {
                                Geocoder geocoder = new Geocoder(getActivity());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocationName(inputMapSearch, 3);
                                    if (addresses != null && !addresses.equals("")) {
                                        searchAnimateCamera(addresses);
                                    }
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    }, 1500);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        animLayoutMapSearchBarFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                etMapSearch.setText("");
                isSearchEditTextVisible = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animLayoutMapSearchBarFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isSearchEditTextVisible = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
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
                            AppGlobals.putUserTestResults(correctAnswerCounter + "/10");
                            AppGlobals.testTakenByAdversary(false);
                            correctAnswerCounter = 0;
                        } else {
                            systemTimeInMillisAfterTestAdversary = System.currentTimeMillis();
                            // calculating time taken by the adversary for test
                            long timeTakenForTestInMillisAdv = systemTimeInMillisAfterTestAdversary - systemTimeInMillisBeforeTestAdversary;
                            String timeTakenForTestByAdv = Helpers.getTimeTakenInMinutesAndSeconds(timeTakenForTestInMillisAdv);
                            // saving adversary test time in database
                            AppGlobals.putTimeTakenForTestByAdversary(timeTakenForTestByAdv);
                            AppGlobals.testTakenByAdversary(true);
                            correctAnswerCounter = 0;
                        }
                        if (AppGlobals.isAdversaryAdded() && !adversaryMode) {
                            Helpers.AlertDialogWithPositiveNegativeFunctions(getActivity(), "Adversary Retake",
                                    "You have an adversary added. Do you want the adversary to take the test as well?", "Yes", "No",
                                    adversaryRetake, proceedWithoutAdversary);
                        } else {
                            Helpers.loadFragment(MainActivity.fragmentManager, new ExitSurveyFragment(), false, null);
                            AppGlobals.putAppStatus(5);
                        }
                    }

                    // getting latitude and longitude and converting them to latlng object in order to
                    // compare results
                    LatLng actualLatLng = new LatLng(Double.parseDouble(mDatabaseHelpers.getRandomRecordFromAllRecords().get(0).
                            get("latitude")), Double.parseDouble(mDatabaseHelpers.getRandomRecordFromAllRecords().get(0).
                            get("longitude")));
                    if (Helpers.isAnswerLocationInVicinityOfActualLocation(answerLatLng, actualLatLng)) {
                        correctAnswerCounter++;
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
            case R.id.btn_map_search:
                setSearchBarVisibility(!isSearchEditTextVisible);
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

    private void setSearchBarVisibility(boolean visibility) {
        if (!visibility) {
            etMapSearch.startAnimation(animLayoutMapSearchBarFadeOut);
            etMapSearch.setVisibility(View.INVISIBLE);
        } else {
            etMapSearch.setVisibility(View.VISIBLE);
            etMapSearch.startAnimation(animLayoutMapSearchBarFadeIn);
        }
    }

    protected void searchAnimateCamera(List<Address> addresses) {
        final Address addressForSearch = addresses.get(0);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LatLng latLngSearch = new LatLng(addressForSearch.getLatitude(), addressForSearch.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngSearch, 15.0f));
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
}
