/*
 * FILE :            HourlyWeatherFragment.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 06
 * DESCRIPTION :     Hourly weather fragment code
 */

package com.example.mad_final;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;


public class HourlyWeatherFragment extends Fragment {

    private SwipeRefreshLayout refreshPage;
    private ListView listView;
    private CustomHourlyWeatherAdapter customHourlyWeatherAdapter;
    private Context ctx;
    private ConnectivityManager connectivityManager;

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

        ctx = inflater.getContext();
        connectivityManager = ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE));

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
                Context context = getContext();
                if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
                    if (PermissionChecker.checkPermissions(requireActivity())) {
                        updateWeather();
                    } else {
                        Toast.makeText(getContext(), "Insufficient permissions to update weather data.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                    Toast.makeText(getContext(), "Error Connecting to internet", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
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






