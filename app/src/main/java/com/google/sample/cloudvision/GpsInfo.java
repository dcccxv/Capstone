package com.google.sample.cloudvision;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class GpsInfo extends AppCompatActivity implements LocationListener {


    private final Context mcontext;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean isGetLocation = false;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;
    Location location;

    double lat;
    double lng;


    @RequiresApi(api = Build.VERSION_CODES.M)
    public GpsInfo(Context mcontext) {
        this.mcontext = mcontext;
        getLocation();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getLocation() {
        Log.i("asdf", "!!!");
        try {

            Log.i("TAG", "2065105101");
            locationManager = (LocationManager) mcontext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.i("TAG", "networknotEnabled");


            } else {

                this.isGetLocation = true;

                if (isNetworkEnabled) {
                    try{
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    }}
                    catch (Exception e){

                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.i("TAG", "ndsaffdsafdsd");

                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null){
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            Log.i("TAG","error : "+e);
            e.printStackTrace();
        }
        return;
    }

    @Override

    public void onLocationChanged(Location location) {

    }



    @Override

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }



    @Override

    public void onProviderEnabled(String provider) {

    }



    @Override

    public void onProviderDisabled(String provider) {

    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}