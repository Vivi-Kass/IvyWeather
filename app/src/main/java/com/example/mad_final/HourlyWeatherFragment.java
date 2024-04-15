package com.example.mad_final;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class HourlyWeatherFragment extends Fragment {

    private SwipeRefreshLayout refreshPage;
    private TextView location;
    private ListView listView;
    private int offset;
    private static int hoursCount = 168; // 7 days each with 24 hours


    private View rootView;
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

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //check the current time, if its night than change the background



        View view = inflater.inflate(R.layout.fragment_hourly_weather, container, false);

        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        boolean isNight = hourOfDay >= 18 || hourOfDay < 6;
        int backgroundColor = isNight ? R.drawable.backgroundmain_night : R.drawable.backroundmainpage;
        int textColor = isNight ? R.color.text_night : R.color.text_day;

        listView = view.findViewById(R.id.hourly_weather_view);

        customHourlyWeatherAdapter = new CustomHourlyWeatherAdapter(requireContext());
        listView.setAdapter(customHourlyWeatherAdapter);

        refreshPage = view.findViewById(R.id.swipe_refresh_layout_hourly);
        location = view.findViewById(R.id.hourly_location_text);
        location.setText("Hourly Weather: " + IvyWeather.getCity());


        // check why background behind
        view.setBackgroundResource(backgroundColor);
        location.setTextColor(getResources().getColor(textColor));

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






