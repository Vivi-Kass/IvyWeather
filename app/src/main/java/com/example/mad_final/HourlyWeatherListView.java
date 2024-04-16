/*
 * FILE :            HourlyWeatherListView.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 06
 * DESCRIPTION :     Required onCreate for hourly weather list view
 */

package com.example.mad_final;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


public class HourlyWeatherListView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_weather_list_view);
    }


}