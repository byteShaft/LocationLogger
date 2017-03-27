package com.byteshaft.locationlogger.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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

/**
 * Created by fi8er1 on 18/03/2017.
 */

public class QuestionnaireFragment extends Fragment implements View.OnClickListener {

    int questionCount;
    public static boolean isQuestionnaireFragmentOpen;
    public static String mapLocationPoint;
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
    private Animation animLayoutMapSearchBarFadeOut;
    private Animation animLayoutMapSearchBarFadeIn;
    private static GoogleMap mMap = null;
    private static LatLng currentLatLngAuto;
    Runnable withdrawInQuestionnaireFragment = new Runnable() {
        public void run() {
            AppGlobals.putAppStatus(0);
            Helpers.loadFragment(MainActivity.fragmentManager, new WelcomeFragment(), true, null);
        }
    };
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            currentLatLngAuto = new LatLng(location.getLatitude(), location.getLongitude());
            if (!cameraAnimatedToCurrentLocation) {
                cameraAnimatedToCurrentLocation = true;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLngAuto, 15.0f));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseViewQuestionnaireFragment = inflater.inflate(R.layout.fragment_questionnaire, container, false);

        animLayoutMapSearchBarFadeOut = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
        animLayoutMapSearchBarFadeIn = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);

        tvQuestionnaireBottomOverlayOne = (TextView) baseViewQuestionnaireFragment.findViewById(R.id.tv_map_bottom_overlay_one);
        tvQuestionnaireBottomOverlayTwo = (TextView) baseViewQuestionnaireFragment.findViewById(R.id.tv_map_bottom_overlay_two);
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
        btnQuestionnaireFragmentNext.setEnabled(false);
        btnQuestionnaireFragmentNext.setAlpha(0.5f);

        btnQuestionnaireFragmentRemove = (Button) baseViewQuestionnaireFragment.findViewById(R.id.btn_map_bottom_overlay_remove);
        btnQuestionnaireFragmentRemove.setOnClickListener(this);
        btnQuestionnaireFragmentRemove.setEnabled(false);
        btnQuestionnaireFragmentRemove.setAlpha(0.5f);

        etMapSearch = (EditText) baseViewQuestionnaireFragment.findViewById(R.id.et_map_search);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_for_questionnaire);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.653109, -79.388366), 4.0f));
//                    Helpers.showProgressDialog(MainActivity.getInstance(), "Acquiring current location");
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    mMap.getUiSettings().setCompassEnabled(true);
                    mMap.setOnMyLocationChangeListener(myLocationChangeListener);
                    mMap.getUiSettings().setMapToolbarEnabled(false);

                        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                            @Override
                            public void onMapLongClick(final LatLng latLng) {
                                if (isSearchEditTextVisible) {
                                    setSearchBarVisibility(false);
                                }
                                if (!mapMarkerAdded) {
                                    double latitude = latLng.latitude;
                                    double longitude = latLng.longitude;
                                    mapLocationPoint = latitude + "," + longitude;
                                    mapLocationPointMarker = mMap.addMarker(new MarkerOptions().position(latLng));
                                    mapMarkerAdded = true;
                                    llQuestionnaireBottomOverlayThree.setVisibility(View.GONE);
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
                        "Yes", "Cancel", withdrawInQuestionnaireFragment);
                break;
            case R.id.btn_map_bottom_overlay_next:
                if (mapMarkerAdded) {
                    questionCount++;
                    mapLocationPointMarker.remove();
                    mapMarkerAdded = false;
                    llQuestionnaireBottomOverlayThree.setVisibility(View.VISIBLE);
                    btnQuestionnaireFragmentNext.setEnabled(false);
                    btnQuestionnaireFragmentNext.setAlpha(0.5f);
                    btnQuestionnaireFragmentRemove.setEnabled(false);
                    btnQuestionnaireFragmentRemove.setAlpha(0.5f);
                    tvQuestionnaireBottomOverlayOne.setText(questionCount + 1 + "/10");
                    if (questionCount > 8) {
                        btnQuestionnaireFragmentNext.setText("Done");
                    }
                }
                break;
            case R.id.btn_map_bottom_overlay_remove:
                mapLocationPointMarker.remove();
                mapMarkerAdded = false;
                llQuestionnaireBottomOverlayThree.setVisibility(View.VISIBLE);
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
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    btnQuestionnaireFragmentMapType.setImageResource(R.drawable.selector_map_type_simple);
                    simpleMapView = false;
                } else {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        isQuestionnaireFragmentOpen = true;
    }
}
