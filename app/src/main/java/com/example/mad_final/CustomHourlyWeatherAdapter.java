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

import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CustomHourlyWeatherAdapter extends BaseAdapter {
    private Context context;

    private int offset;
    private static int hoursCount = 168; // 7 days each with 24 hours
    private LayoutInflater inflater;

    private final int dateloc = 0;
    private final int timeloc = 1;

    private  TextView day;

    private TextView time;

    private TextView code;

    private TextView temperatureText;

    private  TextView feels;

    private TextView precipitationProb;

    private TextView precipitationAmount;


    public CustomHourlyWeatherAdapter(Context ctx) {
        context = ctx;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        Date currentTime = Calendar.getInstance().getTime();
        offset = currentTime.getHours();
        int total = hoursCount - offset; //subtract the passed time from the total number
        return total;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

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

        int realPosition = position + offset;
        String dayTimeString[] = null;

        day = convertView.findViewById(R.id.hourly_day);
        //TextView date = convertView.findViewById(R.id.hourly_date);
        time = convertView.findViewById(R.id.hourly_time);
        ImageView icon = convertView.findViewById(R.id.weather_icon_hourly);
        code = convertView.findViewById(R.id.hourly_code);
        temperatureText = convertView.findViewById(R.id.hourly_temp_view);
        feels = convertView.findViewById(R.id.hourly_feel_view);
        precipitationProb = convertView.findViewById(R.id.hourly_precipitation_prob);
        precipitationAmount = convertView.findViewById(R.id.hourly_precipitation_amount);


        try{
//            String[] hourStrings = dayTimeString[timeloc].split(":");
//            int hour = Integer.parseInt(hourString[0]);




            dayTimeString = IvyWeather.getWeatherData().getJSONObject("hourly").getJSONArray("time").getString(realPosition).split("T");

            time.setText(dayTimeString[timeloc]);
            //remove first 5 chars
            //String dateShort = dayTimeString[dateloc].substring(5, dayTimeString[dateloc].length());
            //date.setText(dateShort);



            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate localDate = LocalDate.parse(dayTimeString[dateloc]);
                DayOfWeek dayOfWeek = localDate.getDayOfWeek();
                String fullname = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
                day.setText(fullname.substring(0,3)); //3 letters
            }


            double temperature = IvyWeather.getWeatherData().getJSONObject("hourly").getJSONArray("temperature_2m").getDouble(realPosition);
            double feelsLike = IvyWeather.getWeatherData().getJSONObject("hourly").getJSONArray("apparent_temperature").getDouble(realPosition);
            int weatherCode = IvyWeather.getWeatherData().getJSONObject("hourly").getJSONArray("weather_code").getInt(realPosition);
            int precipitationProba = IvyWeather.getWeatherData().getJSONObject("hourly").getJSONArray("precipitation_probability").getInt(realPosition);
            double precipitation = IvyWeather.getWeatherData().getJSONObject("hourly").getJSONArray("precipitation").getDouble(realPosition);

            //icon
            String[] hourString = dayTimeString[timeloc].split(":");
            int hour = Integer.parseInt(hourString[0]);
            boolean isNight = hour >= 18 || hour < 6;

            int isday = 1; //day
            if (hour >= 18 || hour <= 6)
            {
                isday = 0; //night
            }
            icon.setImageDrawable(WeatherCodeHandler.getIcon(weatherCode, isday,convertView.getContext()));
            code.setText(WeatherCodeHandler.weatherStatus(weatherCode));


            temperatureText.setText(String.format(Locale.getDefault(), " %.1f°C", temperature));

            StringBuilder tempFeels = new StringBuilder();
            tempFeels.append("(");
            tempFeels.append(String.format(Locale.getDefault(), "%.1f°C",  feelsLike));
            tempFeels.append(")");
            feels.setText(tempFeels);

            precipitationProb.setText("P.O.P: " + Integer.toString(precipitationProba) + "%");
            precipitationAmount.setText(String.format(Locale.getDefault(), "%.2fmm",  precipitation));



            DayNightMode(convertView, isNight);


        }
        catch (Exception e)
        {
            Log.d(TAG,"Failed to get JSON data for hourly weather");
        }



        return convertView;
    }



    private void DayNightMode(View convertView, boolean isNight){
        int backgroundColor = isNight ? R.drawable.backgroundmain_night : R.drawable.backroundmainpage;
        int textColor = isNight ? R.color.text_night : R.color.text_day;
        int trueTextColor = ContextCompat.getColor(convertView.getContext(), textColor);


        convertView.setBackgroundResource(backgroundColor);
        precipitationProb.setTextColor(trueTextColor);
        feels.setTextColor(trueTextColor);
        precipitationAmount.setTextColor(trueTextColor);
        temperatureText.setTextColor(trueTextColor);
        code.setTextColor(trueTextColor);
        time.setTextColor(trueTextColor);
        day.setTextColor(trueTextColor);
    }


}
