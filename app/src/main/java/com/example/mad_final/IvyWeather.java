/*
 * FILE :            IvyWeather.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 10
 * DESCRIPTION :     Contains the important data used for the program to run correctly
 */


package com.example.mad_final;

import android.app.Application;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

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

    public IvyWeather getInstance(){
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

}
