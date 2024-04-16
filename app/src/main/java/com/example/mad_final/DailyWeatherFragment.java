/*
 * FILE :            DailyWeatherFragment.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 06
 * DESCRIPTION :     Code for daily weather
 */


package com.example.mad_final;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;


public class DailyWeatherFragment extends Fragment {

    private SwipeRefreshLayout refreshPage;
    private ListView listView;
    private CustomDailyWeatherAdapter customDailyWeatherAdapter;

    public DailyWeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_daily_weather, container, false);
        listView = view.findViewById(R.id.daily_weather_view);
        Context context = getContext();
        if (context != null && IvyWeather.CheckWifiConnection(context)) {
            //Setup the adapter
            customDailyWeatherAdapter = new CustomDailyWeatherAdapter(requireContext());
            listView.setAdapter(customDailyWeatherAdapter);

            //Set location text
            TextView location = view.findViewById(R.id.daily_location_text);
            location.setText("Daily Weather: " + IvyWeather.getCity());

            refreshPage = view.findViewById(R.id.swipe_refresh_layout_daily);
        }
        //On refresh
        refreshPage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Context context = getContext();
                if (context != null && IvyWeather.CheckWifiConnection(context)) {
                    if (PermissionChecker.checkPermissions(requireActivity())) {
                        updateWeather();
                    } else {
                        Toast.makeText(getContext(), "Insufficient permissions to update weather data.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                    Toast.makeText(getContext(), "Error Connecting to Wifi", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                }
                refreshPage.setRefreshing(false);
            }
        });
        return view;
    }


    //Update weather code
    private void updateWeather() {

        IvyWeather ivyWeather = (IvyWeather) getActivity().getApplication();
        ivyWeather.updateWeather(new IvyWeather.WeatherUpdateListener() {
            @Override
            public void onWeatherUpdateComplete() {
                getActivity().runOnUiThread(() -> {
                    try {
                        updateUI();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(getContext(), "Weather data updated.", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onWeatherUpdateFailed(Exception e) {
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Failed to update weather data: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }, getContext());
    }

    //Update UI
    @SuppressLint("ResourceAsColor")
    private void updateUI() throws JSONException {
        customDailyWeatherAdapter = new CustomDailyWeatherAdapter(requireContext());
        listView.setAdapter(customDailyWeatherAdapter);
        customDailyWeatherAdapter.notifyDataSetChanged();
    }


}