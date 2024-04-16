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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class IvyWeather extends Application {

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

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //Gets the city location
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

    //  METHOD NAME: private boolean CheckWifiConnection()
    //  DESCRIPTION: This method aims to find out if user has wifi or cellular data on so JSON methods can be done without errors
    //  PARAMETERS:  Context context
    //  RETURNS:     true if there is connected, false if its offline
    public static  boolean CheckWifiConnection(Context context){

        //get the current application context for which wifi service they are using
        WifiManager currcon = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // Check if Wi-fi adapter is on
        if (currcon.isWifiEnabled()) {

            //if its on then check if its connected to any wifi
            WifiInfo userWifi = currcon.getConnectionInfo();

            //if yes, return true which means its connected else continue to check for cellular
            if(userWifi.getNetworkId() != -1 ) {
                return true;
            }
        }
        else {
            // get the type of cellular service being used
            ConnectivityManager currcelular = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);


            //get the info of that service
            NetworkInfo userCelular = currcelular.getActiveNetworkInfo();

            //check if its null or if the cellular service is on
            if (userCelular != null && userCelular.isConnected()) {

                // if yes then get the type of service
                boolean isMobile = userCelular.getType() == ConnectivityManager.TYPE_MOBILE;

                //if its a mobile then return true
                if (isMobile) {
                    return true;
                }
            }
        }

        // if no wifi was connected or cellular service return false
        return false;
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

            // using the variable created attempt to get the last known location
            fusedLocationClient.getLastLocation()

                    // in case of a success
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            //check if its null
                            if (location != null) {

                                // set the user's current location
                                setUserLocation(location);

                                //now with the location set call for the API handler
                                APIHandler apiHandler = new APIHandler(new Handler(), context, getUserLocation(), new APIHandler.WeatherDataListener() {
                                    @Override
                                    public void onDataFetched(JSONObject jsonData) {

                                        // using the data fetched from the API, set the weather and city
                                        setWeatherData(jsonData);
                                        setCity(getUserLocation(), context);
                                        try {

                                            // notify listener that the update is finished
                                            listener.onWeatherUpdateComplete();
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                            } else {
                                // if the listener was null throw error
                                listener.onWeatherUpdateFailed(new Exception("Failed to obtain location."));
                            }
                        }
                    })

                    // in case of a error on the listener simply throw an error
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


