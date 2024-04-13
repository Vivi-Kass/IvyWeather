package com.example.mad_final;

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
    private TextView location;
    private ListView listView;
    private int offset;
    private static int hoursCount = 168; // 7 days each with 24 hours
    private CustomHourlyWeatherAdapter customHourlyWeatherAdapter;

    public HourlyWeatherFragment() {
        // Required empty public constructor
    }

    public static HourlyWeatherFragment newInstance() {
        return new HourlyWeatherFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hourly_weather, container, false);

        listView = view.findViewById(R.id.hourly_weather_view);

        customHourlyWeatherAdapter = new CustomHourlyWeatherAdapter(requireContext());
        listView.setAdapter(customHourlyWeatherAdapter);

        refreshPage = view.findViewById(R.id.swipe_refresh_layout_hourly);
        location = view.findViewById(R.id.hourly_location_text);
        location.setText("City: " + IvyWeather.getCity());

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
    private void updateUI() {
        customHourlyWeatherAdapter = new CustomHourlyWeatherAdapter(requireContext());
        listView.setAdapter(customHourlyWeatherAdapter);
        customHourlyWeatherAdapter.notifyDataSetChanged();
    }

}






