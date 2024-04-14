/*
 * FILE :            IvyWeather.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 10
 * DESCRIPTION :     Contains the important data used for the program to run correctly
 */


package com.example.mad_final;

import android.app.Application;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class IvyWeather extends Application {

    private static IvyWeather singleton;

    private static String city;
    private static Location userLocation;
    private static JSONObject weatherData;

    public static String getCity() {
        return city;
    }

    public static void setCity(Location location, Context context) {
        city = getCurrentLocationName(location, context);
    }

    public static Location getUserLocation() {
        return userLocation;
    }

    public static void setUserLocation(Location userLocation) {
        IvyWeather.userLocation = userLocation;
    }

    public static JSONObject getWeatherData() {
        return weatherData;
    }

    public static void setWeatherData(JSONObject weatherData) {
        IvyWeather.weatherData = weatherData;
    }

    public static IvyWeather getInstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }


    private static String getCurrentLocationName(Location location, Context context) {

        //Using geocoder and storing addresses in a list
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {

            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                //Got the location, return it
                return address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public interface WeatherUpdateListener {
        void onWeatherUpdateComplete() throws JSONException;
        void onWeatherUpdateFailed(Exception e);
    }
    public void updateWeather(WeatherUpdateListener listener, Context context) {
        // Check permissions using PermissionChecker class
        if (!PermissionChecker.checkPermissions(context)) {
            // If permissions are not granted, notify the user
            listener.onWeatherUpdateFailed(new SecurityException("Required location permissions are not granted"));
            return;
        }

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                setUserLocation(location);
                                APIHandler apiHandler = new APIHandler(new Handler(), context, getUserLocation(), new APIHandler.WeatherDataListener() {
                                    @Override
                                    public void onDataFetched(JSONObject jsonData) {
                                        setWeatherData(jsonData);
                                        setCity(getUserLocation(), context);
                                        try {
                                            listener.onWeatherUpdateComplete();
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                            } else {

                                listener.onWeatherUpdateFailed(new Exception("Failed to obtain location."));
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onWeatherUpdateFailed(e);
                        }
                    });
        } catch (SecurityException e) {
            // if revoke than throw execption
            listener.onWeatherUpdateFailed(new SecurityException("Permission was revoked before obtaining location: " + e.getMessage()));
        }
    }



}


