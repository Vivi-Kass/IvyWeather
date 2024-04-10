/*
 * FILE :            CurrentWeatherFragment.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 02
 * DESCRIPTION :     Contains the logic for the view weather fragment. Such as using the APIHandler
 */

package com.example.mad_final;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CurrentWeatherFragment extends Fragment {

    private SwipeRefreshLayout swipeRefresh;
    private TextView userLatitude;
    private TextView userLongitude;
    private Button toMoreDetails;
    private Button refreshPage;
    private TextView userLocTV;
    private TextView currTemperature;
    private TextView feelsTemp;
    private Handler handler = new Handler();

    Location userLocation;



    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    public static CurrentWeatherFragment newInstance() {
        return new CurrentWeatherFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_view_weather, container, false);

        // Initialize TextViews and buttons here so they are ready for updates
        userLatitude = rootView.findViewById(R.id.lat_loc);
        userLongitude = rootView.findViewById(R.id.lon_loc);
        userLocTV = rootView.findViewById(R.id.current_location);
        refreshPage = rootView.findViewById(R.id.refresh_info);
        toMoreDetails = rootView.findViewById(R.id.more_details);
        swipeRefresh = rootView.findViewById(R.id.swipe_refresh_layout);
        currTemperature = rootView.findViewById(R.id.current_temp);
        feelsTemp = rootView.findViewById(R.id.feels_temp);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(PermissionChecker.checkPermissions(requireActivity())) {
                    updateWeather();
                    Toast.makeText(getContext(), "Page updated", Toast.LENGTH_SHORT).show();
                }
                swipeRefresh.setRefreshing(false);
            }
        });


        refreshPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PermissionChecker.checkPermissions(requireActivity())) {
                    //getLocation();
                    Toast.makeText(getContext(), "Page updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error in fetching new info", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder latlonnotification = new AlertDialog.Builder(requireActivity());
                latlonnotification.setTitle("Location Details");

                latlonnotification.setMessage("Latitude: " + VIWeather.getUserLocation().getLatitude() + "\nLongitude: " + VIWeather.getUserLocation().getLongitude());

                latlonnotification.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });


                AlertDialog dialog = latlonnotification.create();
                dialog.show();
            }
        });

        updateUI();

        return rootView;
    }

    private void updateUI()
    {
        try {
            // parse through JSON to get the hourly temperature

            Date currentTime = Calendar.getInstance().getTime();

            int hour = currentTime.getHours();

            double temperature = VIWeather.getWeatherData().getJSONObject("hourly").getJSONArray("temperature_2m").getDouble(hour); // Adjust based on actual structure
            double feelsLike = VIWeather.getWeatherData().getJSONObject("hourly").getJSONArray("apparent_temperature").getDouble(hour); // Adjust based on actual structure
            currTemperature.setText(String.format(Locale.getDefault(), " %.1f°C", temperature));
            feelsTemp.setText(String.format(Locale.getDefault(), "Feels Like: %.1f°C",  feelsLike));
            userLocTV.setText(String.format(Locale.getDefault(), "City: " + VIWeather.getCity()));
            userLatitude.setText("Latitude: " + VIWeather.getUserLocation().getLatitude());
            userLongitude.setText("Longitude: " + VIWeather.getUserLocation().getLongitude());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateWeather()
    {
        try {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(Location location) {
                            //String longitude = Double.toString(location.getLongitude());
                            //String latitude = Double.toString(location.getLatitude());
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                VIWeather.setUserLocation(location);
                                Log.d(TAG, "Location acquired");
                                APIHandler apiHandler = new APIHandler(handler, getContext(), VIWeather.getUserLocation(), new APIHandler.WeatherDataListener() {
                                    @Override
                                    public void onDataFetched(JSONObject jsonData) {
                                        //Once Json data is fetched, update UI
                                        VIWeather.setWeatherData(jsonData);

                                        VIWeather.setCity(VIWeather.getUserLocation(), requireContext());
                                        //send parameters to fragment
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                updateUI();
                                                Log.d(TAG, "UI Updated");
                                            }
                                        });
                                    }
                                });

                            }

                        }

                    })
                    .addOnFailureListener(requireActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("CurrentWeatherFragment", "Failed to get location");
                        }
                    });
        }
        catch (SecurityException e)
        {
            Log.d(TAG, "Security Exception: " + e.getMessage());
        }

    }


}




