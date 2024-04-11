package com.example.mad_final;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HourlyWeatherFragment extends Fragment {
    public HourlyWeatherFragment() {
        // Required empty public constructor
    }

    public static HourlyWeatherFragment newInstance(String param1, String param2) {
        return  new HourlyWeatherFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hourly_weather, container, false);





        return view;
    }
}