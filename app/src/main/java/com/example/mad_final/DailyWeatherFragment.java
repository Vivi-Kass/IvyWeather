package com.example.mad_final;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class DailyWeatherFragment extends Fragment {

    private SwipeRefreshLayout refreshPage;

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

        refreshPage = view.findViewById(R.id.swipe_refresh_layout_daily);

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