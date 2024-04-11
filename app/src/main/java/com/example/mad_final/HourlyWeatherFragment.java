package com.example.mad_final;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HourlyWeatherFragment extends Fragment {

    private SwipeRefreshLayout refreshPage;
    Handler handler = new Handler();

    public HourlyWeatherFragment() {
        // Required empty public constructor
    }

    public static HourlyWeatherFragment newInstance(String param1, String param2) {
        return new HourlyWeatherFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hourly_weather, container, false);

        refreshPage = view.findViewById(R.id.swipe_refresh_layout_hourly);

        refreshPage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (PermissionChecker.checkPermissions(requireActivity())) {
                    //updateWeather();
                    Toast.makeText(getContext(), "Page updated", Toast.LENGTH_SHORT).show();
                }
                refreshPage.setRefreshing(false);
            }
        });


        return view;
    }

    private void updateUI() {
        try {
            // parse through JSON to get the hourly temperature


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}








