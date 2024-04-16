/*
 * FILE :            DailyWeatherListView.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 11
 * DESCRIPTION :     Required onCreate for Daily weather list view
 */

package com.example.mad_final;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


public class DailyWeatherListView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_weather_list_view);
    }

}