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
import android.content.pm.PackageManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

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
    private TextView weatherCode;
    private ImageView weatherIcon;
    private TextView tempHigh;
    private TextView tempLow;
    private TextView humidity;
    private TextView precipAmount;
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
        weatherCode = rootView.findViewById(R.id.weather_code);
        weatherIcon = rootView.findViewById(R.id.weather_icon);
        precipAmount = rootView.findViewById(R.id.precip_amount);
        humidity = rootView.findViewById(R.id.humidity_amount);
        tempHigh = rootView.findViewById(R.id.temp_high);
        tempLow = rootView.findViewById(R.id.temp_low);

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

                latlonnotification.setMessage("Latitude: " + IvyWeather.getUserLocation().getLatitude() + "\nLongitude: " + IvyWeather.getUserLocation().getLongitude());

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

            currTemperature.setText(String.format(Locale.getDefault(), " %.1f°C", IvyWeather.getWeatherData().getJSONObject("current").getDouble("temperature_2m")));
            feelsTemp.setText(String.format(Locale.getDefault(), "Feels Like: %.1f°C",  IvyWeather.getWeatherData().getJSONObject("current").getDouble("apparent_temperature")));
            userLocTV.setText("Current Weather: " + IvyWeather.getCity());
            userLatitude.setText("Latitude: " + IvyWeather.getUserLocation().getLatitude());
            userLongitude.setText("Longitude: " + IvyWeather.getUserLocation().getLongitude());
            int code = IvyWeather.getWeatherData().getJSONObject("current").getInt("weather_code");
            weatherCode.setText(WeatherCodeHandler.weatherStatus(code));
            int isDay = IvyWeather.getWeatherData().getJSONObject("current").getInt("is_day");

            weatherIcon.setImageDrawable(WeatherCodeHandler.getIcon(code, isDay, requireContext()));

            tempHigh.setText("H:" + String.format(Locale.getDefault(), " %.1f°C", IvyWeather.getWeatherData().getJSONObject("daily").getJSONArray("temperature_2m_max").getDouble(0)));
            tempLow.setText("L:" + String.format(Locale.getDefault(), " %.1f°C", IvyWeather.getWeatherData().getJSONObject("daily").getJSONArray("temperature_2m_min").getDouble(0)));

            precipAmount.setText(Double.toString(IvyWeather.getWeatherData().getJSONObject("current").getDouble("precipitation")) + "mm");
            humidity.setText("Humidity: " + IvyWeather.getWeatherData().getJSONObject("current").getInt("relative_humidity_2m") + "%");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateWeather() {
        if (!PermissionChecker.checkPermissions(getContext())) {
            PermissionChecker.promptPrecise(getActivity());
            return;
        }
        IvyWeather ivyWeather = (IvyWeather) getActivity().getApplication();
        ivyWeather.updateWeather(new IvyWeather.WeatherUpdateListener() {
            @Override
            public void onWeatherUpdateComplete() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                        Toast.makeText(getContext(), "Weather updated successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onWeatherUpdateFailed(Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Failed to update weather: ", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }, getContext());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) { // Check if the request code corresponds to the request we made
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateWeather();
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}




