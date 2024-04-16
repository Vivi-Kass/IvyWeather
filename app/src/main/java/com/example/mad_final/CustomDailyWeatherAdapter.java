/*
 * FILE :            CustomDailyWeatherAdapter.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 11
 * DESCRIPTION :     Adapter for the daily weather
 */

package com.example.mad_final;

import static android.content.ContentValues.TAG;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class CustomDailyWeatherAdapter extends BaseAdapter {

    private final LayoutInflater inflater;

    public CustomDailyWeatherAdapter(Context ctx)
    {
        inflater = LayoutInflater.from(ctx);
    }

    //7 days in a week
    @Override
    public int getCount() {
        int daysInWeek = 7;
        return daysInWeek;
    }

    //Not used, but required in code
    @Override
    public Object getItem(int i) {
        return null;
    }

    //Not used, but required in code
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_daily_weather_list_view, parent, false);
        }

        TextView day = convertView.findViewById(R.id.daily_day);
        TextView date = convertView.findViewById(R.id.daily_date);
        ImageView icon = convertView.findViewById(R.id.weather_icon_daily);
        TextView code = convertView.findViewById(R.id.daily_code);
        TextView tempHigh = convertView.findViewById(R.id.daily_temp_high);
        TextView tempLow = convertView.findViewById(R.id.daily_temp_low);
        TextView uv = convertView.findViewById(R.id.daily_uv_index);
        TextView precip = convertView.findViewById(R.id.daily_precip_amount);


        try
        {
            //get date minus year
            String fullDate = IvyWeather.getWeatherData().getJSONObject("daily").getJSONArray("time").getString(position);
            String dayTimeString[] = fullDate.split("T");
            //remove first 5 chars
            int dateloc = 0;
            String dateShort = dayTimeString[dateloc].substring(5, dayTimeString[dateloc].length());

            date.setText(dateShort);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate localDate = LocalDate.parse(fullDate);
                DayOfWeek dayOfWeek = localDate.getDayOfWeek();
                String fullname = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
                day.setText(fullname.substring(0,3)); //3 letters
            }


            //weather code and temp high and low
            int weatherCode = IvyWeather.getWeatherData().getJSONObject("daily").getJSONArray("weather_code").getInt(position);
            double tempHighInt = IvyWeather.getWeatherData().getJSONObject("daily").getJSONArray("temperature_2m_max").getDouble(position);
            double tempLowInt = IvyWeather.getWeatherData().getJSONObject("daily").getJSONArray("temperature_2m_min").getDouble(position);

            tempHigh.setText("H:" + String.format(Locale.getDefault(), " %.1f°C", tempHighInt));
            tempLow.setText("L:" + String.format(Locale.getDefault(), " %.1f°C", tempLowInt));

            //set the icon and text
            int isday = 1;
            icon.setImageDrawable(WeatherCodeHandler.getIcon(weatherCode, isday,convertView.getContext()));
            code.setText(WeatherCodeHandler.weatherStatus(weatherCode));

            //set UV and precip
            uv.setText("UV:" + Double.toString(IvyWeather.getWeatherData().getJSONObject("daily").getJSONArray("uv_index_max").getDouble(position)));
            precip.setText(Double.toString(IvyWeather.getWeatherData().getJSONObject("daily").getJSONArray("precipitation_sum").getDouble(position)) + "mm");


        }
        catch (Exception e)
        {
            Log.d(TAG, "Failed to get daily weather");
        }

        return convertView;
    }


}
