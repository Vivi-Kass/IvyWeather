/*
 * FILE :            CustomDailyWeatherAdapter.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 06
 * DESCRIPTION :     Hourly weather fragment code
 */

package com.example.mad_final;

import android.annotation.SuppressLint;
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


public class HourlyWeatherFragment extends Fragment {

    private SwipeRefreshLayout refreshPage;
    private ListView listView;
    private CustomHourlyWeatherAdapter customHourlyWeatherAdapter;

    public HourlyWeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hourly_weather, container, false);

        listView = view.findViewById(R.id.hourly_weather_view);

        //setup adapter
        customHourlyWeatherAdapter = new CustomHourlyWeatherAdapter(requireContext());
        listView.setAdapter(customHourlyWeatherAdapter);

        //Set location text
        TextView location = view.findViewById(R.id.hourly_location_text);
        location.setText("Hourly Weather: " + IvyWeather.getCity());

        //On refresh
        refreshPage = view.findViewById(R.id.swipe_refresh_layout_hourly);
        refreshPage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (PermissionChecker.checkPermissions(requireActivity())) {
                    updateWeather();
                } else {
                    Toast.makeText(getContext(), "Insufficient permissions to update weather data.", Toast.LENGTH_SHORT).show();
                }
                refreshPage.setRefreshing(false);
            }
        });
        return view;
    }


    //Update Weather
    private void updateWeather() {

        IvyWeather ivyWeather = (IvyWeather) getActivity().getApplication();
        ivyWeather.updateWeather(new IvyWeather.WeatherUpdateListener() {
            @Override
            public void onWeatherUpdateComplete() {
                getActivity().runOnUiThread(() -> {
                    updateUI();
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
    private void updateUI() {
        customHourlyWeatherAdapter = new CustomHourlyWeatherAdapter(requireContext());
        listView.setAdapter(customHourlyWeatherAdapter);
        customHourlyWeatherAdapter.notifyDataSetChanged();
    }

}






