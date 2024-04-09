/*
 * FILE :            ViewWeatherFragment.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 02
 * DESCRIPTION :     Contains the logic for the view weather fragment. Such as using the APIHandler
 */

package com.example.mad_final;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class ViewWeatherFragment extends Fragment {

    private TextView userLatitude;

    private TextView userLongitude;

    private TextView userLocTV;

    private TextView currTemperature;

    private String userLocation;
    private Handler handler = new Handler();
    private FusedLocationProviderClient fusedLocationClient;


    public ViewWeatherFragment() {
        // Required empty public constructor
    }

    public static ViewWeatherFragment newInstance(String param1, String param2) {
        return new ViewWeatherFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_view_weather, container, false);

        // Initialize your TextViews here so they are ready for updates
        userLatitude = rootView.findViewById(R.id.lat_loc);
        userLongitude = rootView.findViewById(R.id.lon_loc);
        userLocTV = rootView.findViewById(R.id.current_location);


        // Inflate the layout for this fragment
        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION }, 0);
            ActivityCompat.requestPermissions(requireActivity(), new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, 1);

        }

        //Permission granted
        if(ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            getLocation();
        }

        return rootView;
    }


    private String getCurrentLocation(double latitude, double longitude, Context context) {

        //Using geocoder and storing addresses in a list
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {

            addresses = geocoder.getFromLocation(latitude, longitude, 1);
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

    private void updateTempwithJson(JSONObject jsonData) {
        try {
           // parse through JSON to get the hourly temperature
            double temperature = jsonData.getJSONObject("hourly").getJSONArray("temperature_2m").getDouble(0); // Adjust based on actual structure

            //update UI
            getActivity().runOnUiThread(() -> {

                //get current view of the text view
                currTemperature = getView().findViewById(R.id.current_temp);
                currTemperature.setText(String.format(Locale.getDefault(), "Temperature: %.1f°C", temperature));
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getLocation()
    {
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(Location location) {
                    String longitude = Double.toString(location.getLongitude());
                    String latitude = Double.toString(location.getLatitude());
                    userLatitude.setText("Lat: " + longitude);
                    userLongitude.setText("Lon: " + latitude);
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        //Toast.makeText(requireContext(), "Latitude: " + String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(requireContext(), "Longitude: " +String.valueOf(location.getLongitude()), Toast.LENGTH_SHORT).show();

                        //Log information so it can be compared later
                        Log.d("ApplicationLatitude", latitude);
                        Log.d("ApplicationLongitude", longitude);
                        userLocation = getCurrentLocation(location.getLatitude(),location.getLongitude() , requireContext());

                        if (userLocation != null) {
                            //Toast.makeText(getContext(), "Current location: " + userLocation, Toast.LENGTH_LONG).show();
                            userLocTV.setText("City: " + userLocation);
                        }
                        APIHandler apiHandler = new APIHandler(handler, getContext(), location, new APIHandler.WeatherDataListener() {
                            @Override
                            public void onDataFetched(JSONObject jsonData) {
                                //Once Json data is fetched, update UI
                                updateTempwithJson(jsonData);
                            }
                        });

                    }

                }

            })
                    .addOnFailureListener(requireActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ViewWeatherFragment", "Failed to get location");
                        }
                    });
        }
        catch (SecurityException e)
        {
            Log.d(TAG, "Security Exception: " + e.getMessage());
        }


    }

}




