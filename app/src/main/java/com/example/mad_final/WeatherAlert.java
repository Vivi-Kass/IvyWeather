package com.example.mad_final;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

import org.json.JSONException;

import java.util.Locale;

public class WeatherAlert extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Trigger weather update to fetch current data
        IvyWeather ivyWeather = ((IvyWeather) context.getApplicationContext());
        ivyWeather.updateWeather(new IvyWeather.WeatherUpdateListener() {
            @Override
            public void onWeatherUpdateComplete() throws JSONException {
                Context context = IvyWeather.getInstance().getApplicationContext();

                double currentTemp = IvyWeather.getWeatherData().getJSONObject("current").getDouble("temperature_2m");
                String tempText = String.format(Locale.getDefault(), "Current temperature: %.1f°C", currentTemp);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(
                            "weather_updates",
                            "Weather Updates",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("Notifications for current weather");
                    notificationManager.createNotificationChannel(channel);
                }

                // Build the notification.
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "weather_updates")
                        .setContentTitle("Weather Alert")
                        .setContentText(tempText)
                        .setSmallIcon(R.drawable.ivy_app_icon_foreground)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                // Issue the notification.
                notificationManager.notify(0, builder.build());
            }


            @Override
            public void onWeatherUpdateFailed(Exception e) {

            }
        }, context);
    }
}