package com.example.mad_final;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

public class WeatherAlert extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("show_notifications", false)) {

            Intent serviceIntent = new Intent(context, VIWeather.class);
            context.startService(serviceIntent);
        }
    }
}
