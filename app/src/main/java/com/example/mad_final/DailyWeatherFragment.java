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

import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;

public class DailyWeatherFragment extends Fragment {

    private SwipeRefreshLayout refreshPage;
    private TextView location;
    private ListView listView;
    private CustomDailyWeatherAdapter customDailyWeatherAdapter;




    public DailyWeatherFragment() {
        // Required empty public constructor
    }


    public static DailyWeatherFragment newInstance() {
        return new DailyWeatherFragment();
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




        customDailyWeatherAdapter = new CustomDailyWeatherAdapter(requireContext());
        listView.setAdapter(customDailyWeatherAdapter);

        refreshPage = view.findViewById(R.id.swipe_refresh_layout_daily);
        location = view.findViewById(R.id.daily_location_text);
        location.setText("Daily Weather: " + IvyWeather.getCity());



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
    @SuppressLint("ResourceAsColor")
    private void updateUI() throws JSONException {
        customDailyWeatherAdapter = new CustomDailyWeatherAdapter(requireContext());
        listView.setAdapter(customDailyWeatherAdapter);
        customDailyWeatherAdapter.notifyDataSetChanged();





    }


}