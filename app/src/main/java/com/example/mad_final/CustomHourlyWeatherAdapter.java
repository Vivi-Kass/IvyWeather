/*
 * FILE :            CustomHourlyWeatherAdapter.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 06
 * DESCRIPTION :     Adapter for the hourly weather
 */

package com.example.mad_final;

import static androidx.fragment.app.FragmentManager.TAG;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CustomHourlyWeatherAdapter extends BaseAdapter {
    private int offset;
    private static final int hoursCount = 168; // 7 days each with 24 hours
    private final LayoutInflater inflater;


    public CustomHourlyWeatherAdapter(Context ctx) {
        inflater = LayoutInflater.from(ctx);
    }

    //Return the count
    @Override
    public int getCount() {
        Date currentTime = Calendar.getInstance().getTime(); //get current time
        offset = currentTime.getHours(); //get the hour
        int total = hoursCount - offset; //subtract the passed time from the total number
        return total; //that's the total
    }

    //Not used but required in code
    @Override
    public Object getItem(int i) {
        return null;
    }

    //Not used but required in code
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"RestrictedApi", "ResourceAsColor"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_hourly_weather_list_view, parent, false);
        }

        //Real position because we don't display the weather for time that already passed
        int realPosition = position + offset;
        String[] dayTimeString;

        //Get views
        TextView day = convertView.findViewById(R.id.hourly_day);
        TextView time = convertView.findViewById(R.id.hourly_time);
        ImageView icon = convertView.findViewById(R.id.weather_icon_hourly);
        TextView code = convertView.findViewById(R.id.hourly_code);
        TextView temperatureText = convertView.findViewById(R.id.hourly_temp_view);
        TextView feels = convertView.findViewById(R.id.hourly_feel_view);
        TextView precipitationProb = convertView.findViewById(R.id.hourly_precipitation_prob);
        TextView precipitationAmount = convertView.findViewById(R.id.hourly_precipitation_amount);


        try{
            //Split the date and time
            dayTimeString = IvyWeather.getWeatherData().getJSONObject("hourly").getJSONArray("time").getString(realPosition).split("T");

            int timeloc = 1;
            time.setText(dayTimeString[timeloc]);

            //Get day of week
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int dateloc = 0;
                LocalDate localDate = LocalDate.parse(dayTimeString[dateloc]);
                DayOfWeek dayOfWeek = localDate.getDayOfWeek();
                String fullname = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
                day.setText(fullname.substring(0,3)); //3 letters
            }

            //Extract data
            double temperature = IvyWeather.getWeatherData().getJSONObject("hourly").getJSONArray("temperature_2m").getDouble(realPosition);
            double feelsLike = IvyWeather.getWeatherData().getJSONObject("hourly").getJSONArray("apparent_temperature").getDouble(realPosition);
            int weatherCode = IvyWeather.getWeatherData().getJSONObject("hourly").getJSONArray("weather_code").getInt(realPosition);
            int precipitationProba = IvyWeather.getWeatherData().getJSONObject("hourly").getJSONArray("precipitation_probability").getInt(realPosition);
            double precipitation = IvyWeather.getWeatherData().getJSONObject("hourly").getJSONArray("precipitation").getDouble(realPosition);

            //icon
            String[] hourString = dayTimeString[timeloc].split(":");
            int hour = Integer.parseInt(hourString[0]);

            //Check if it's day or night
            int isday = 1; //day
            if (hour >= 18 || hour <= 6)
            {
                isday = 0; //night
            }
            icon.setImageDrawable(WeatherCodeHandler.getIcon(weatherCode, isday,convertView.getContext()));
            code.setText(WeatherCodeHandler.weatherStatus(weatherCode));

            //Set temperature and feels like strings
            temperatureText.setText(String.format(Locale.getDefault(), " %.1f°C", temperature));

            //Surround in ()
            StringBuilder tempFeels = new StringBuilder();
            tempFeels.append("(");
            tempFeels.append(String.format(Locale.getDefault(), "%.1f°C",  feelsLike));
            tempFeels.append(")");
            feels.setText(tempFeels);

            //Precipitation
            precipitationProb.setText("P.O.P: " + Integer.toString(precipitationProba) + "%");
            precipitationAmount.setText(String.format(Locale.getDefault(), "%.2fmm",  precipitation));

        }
        catch (Exception e)
        {
            Log.d(TAG,"Failed to get JSON data for hourly weather");
        }

        return convertView;
    }






}
