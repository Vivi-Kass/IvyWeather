package com.example.mad_final;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import org.json.JSONException;

public class WeatherAlert extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Trigger weather update to fetch current data
        IvyWeather ivyWeather = ((IvyWeather) context.getApplicationContext());
        ivyWeather.updateWeather(new IvyWeather.WeatherUpdateListener() {
            @Override
            public void onWeatherUpdateComplete() throws JSONException {
                double currentTemp = IvyWeather.getWeatherData().getJSONObject("current").getDouble("temperature_2m");

            }

            @Override
            public void onWeatherUpdateFailed(Exception e) {

            }
        }, context);
    }
}