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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomHourlyWeatherAdapter extends BaseAdapter {
    private Context context;

    private int offset;
    private static int hoursCount = 168; // 7 days each with 24 hours
    private LayoutInflater inflater;
    private List<WeatherData> weatherDataList = new ArrayList<>();

    public CustomHourlyWeatherAdapter(Context ctx, List<WeatherData> data) {
        context = ctx;
        inflater = LayoutInflater.from(ctx);
        weatherDataList = data;
    }

    @Override
    public int getCount() {
        return weatherDataList.size();
    }

    @Override
    public WeatherData getItem(int position) {
        return weatherDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_hourly_weather_list_view, parent, false);
        }

        WeatherData weatherData = getItem(position);

        int realPosition = position + offset;
        String dayTimeString[] = null;

        TextView date = convertView.findViewById(R.id.hourly_date);
        TextView time = convertView.findViewById(R.id.hourly_time);
        ImageView icon = convertView.findViewById(R.id.weather_icon_hourly);
        TextView code = convertView.findViewById(R.id.hourly_code);
        TextView temperture = convertView.findViewById(R.id.hourly_temp_view);
        TextView feels = convertView.findViewById(R.id.hourly_feel_view);
        TextView precipitationProb = convertView.findViewById(R.id.hourly_precipitation_prob);
        TextView precipitationAmount = convertView.findViewById(R.id.hourly_precipitation_amount);


        try{
            date.setText(weatherData.date);
            time.setText(weatherData.time);
            temperture.setText(weatherData.temperature + "°C");
            feels.setText(weatherData.feelsLike + "°C");
            precipitationProb.setText(weatherData.precipitationProb + "%");
            precipitationAmount.setText(weatherData.precipitationAmount + " mm");
            icon.setImageResource(weatherData.weatherIconResId);
        }
        catch (Exception e)
        {
            Log.d(TAG,"Failed to get JSON data for hourly weather");
        }


        return convertView;
    }


    public void updateData(List<WeatherData> data) {
        weatherDataList = data;
        notifyDataSetChanged();
    }

    public static class WeatherData {
        public String date;
        public String time;
        public String temperature;
        public String feelsLike;
        public String description;
        public String precipitationProb;
        public String precipitationAmount;
        public int weatherIconResId;

        public WeatherData(String date, String time, String temperature, String feelsLike, String description, String precipitationProb, String precipitationAmount, int weatherIconResId) {
            this.date = date;
            this.time = time;
            this.temperature = temperature;
            this.feelsLike = feelsLike;
            this.description = description;
            this.precipitationProb = precipitationProb;
            this.precipitationAmount = precipitationAmount;
            this.weatherIconResId = weatherIconResId;
        }
    }

}
