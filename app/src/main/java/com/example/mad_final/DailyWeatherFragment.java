package com.example.mad_final;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class DailyWeatherFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_weather, container, false);
        return view;
    }
}