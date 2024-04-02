package com.example.mad_final;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewWeatherFragment extends Fragment {

    private Handler handler = new Handler();


    public ViewWeatherFragment() {
        // Required empty public constructor
    }

    public static ViewWeatherFragment newInstance(String param1, String param2) {
        return new ViewWeatherFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        APIHandler apiHandler = new APIHandler(handler, getContext());
        apiHandler.getWeather();



        return inflater.inflate(R.layout.fragment_view_weather, container, false);
    }
}