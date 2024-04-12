package com.example.mad_final;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

public class WeatherAlert extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean notificationsEnabled = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("show_notifications", false);
        Intent serviceIntent = new Intent(context, IvyWeather.class);
        if (notificationsEnabled) {
            context.startService(serviceIntent);
        } else {
            context.stopService(serviceIntent);
        }
    }

}
