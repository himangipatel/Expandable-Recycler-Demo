package com.expandablerecycleview.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created by Himangi on 28/7/17
 */

public class AddressPresenter implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        ResultCallback<LocationSettingsResult> {

    private Activity context;

    public AddressPresenter(Activity context) {
        this.context = context;
    }

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
   // private LandingScreenView mLandingScreenView;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private GoogleApiClient mGoogleApiClient;
    private Boolean mRequestingLocationUpdates;
    private String TAG = "Address";
    private AddressResultReceiver mResultReceiver;


//    @Override public void attachView(LandingScreenView view) {
//        mLandingScreenView = view;
//        mResultReceiver = new AddressResultReceiver(new Handler());
//    }
//
//    @Override public void detachView() {
//        mLandingScreenView = null;
//    }
//
//

    /**
     * setup location
     */
    public void setUpLocationClient() {
        mRequestingLocationUpdates = false;
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
    }


    /**
     * Setup google client
     */

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void onResume() {
        Log.d(TAG, "onResume  " + !mRequestingLocationUpdates + " client " + mGoogleApiClient.isConnected());
        checkPermission();
    }

    private void checkPermission() {
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            Log.d(TAG, "onResume check Permission");
            //mLandingScreenView.checkLocationPermission();
        }
    }

    public void onPause() {
        Log.d(TAG, "onPause");
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdate();
        }
        mRequestingLocationUpdates = false;
    }


    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        LocationSettingsRequest mLocationSettingsRequest = builder.build();
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        mLocationSettingsRequest);
        result.setResultCallback(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mCurrentLocation = location;
            setUpLocation(mCurrentLocation);
        }else{
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mCurrentLocation!=null){
                setUpLocation(mCurrentLocation);
            }else{
//                mLandingScreenView.hideProgress();
//                mLandingScreenView.showCurrentAddress("FR"); // when not getting location set defaults to france
            }
        }
    }

    private void setUpLocation(Location location){
//        getPrefs(getBaseContext()).save(QuickstartPreferences.LAT,
//                String.valueOf(location.getLatitude()));
//        getPrefs(getBaseContext()).save(QuickstartPreferences.LNG,
//                String.valueOf(location.getLongitude()));
        startService(mCurrentLocation);
        //if (!(getContext() instanceof SplashActivity)) getNearByJobCount();
        stopLocationUpdate();
    }

    private void stopLocationUpdate() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override public void onResult(@NonNull Status status) {
                            mRequestingLocationUpdates = false;
                        }
                    });
        }
    }

    private void startService(Location location) {
        Intent intent = new Intent(context, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        context.startService(intent);
    }



    @Override public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
    }

    @Override public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.d(TAG, "All location settings are satisfied.");
                //mLandingScreenView.checkLocationPermission();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.d(TAG, "Location settings are not satisfied. Show the user a dialog to"
                        + "upgrade location settings ");
                try {
                    status.startResolutionForResult(context, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.d(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.d(TAG,
                        "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");
                break;
        }
    }

    public void onStart() {
        Log.d(TAG, "onStart");
        mGoogleApiClient.connect();
    }

    public void onStop() {
        Log.d(TAG, "onStop");
        mGoogleApiClient.disconnect();
    }

    private class AddressResultReceiver extends ResultReceiver {

        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override protected void onReceiveResult(int resultCode, Bundle resultData) {

//            if (mLandingScreenView == null) {
//                return;
//            }
            // Display the address string or an error message sent from the intent service.
            String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            String mCountryCode = resultData.getString(Constants.COUNTRY_CODE);

//            if (getContext() instanceof SplashActivity) {
//                mLandingScreenView.hideProgress();
//                mLandingScreenView.showCurrentAddress(mCountryCode);
//            } else {
//                mLandingScreenView.showCurrentAddress(mAddressOutput);
//            }

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                //showToast(getString(R.string.address_found));
            }
        }
    }

}

