package com.example.mad_final;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;

public class CustomDailyWeatherAdapter extends BaseAdapter {

    private final int daysInWeek = 7;

    private final int dateloc = 0;
    private final int isday = 1;
    private Context context;
    private LayoutInflater inflater;
    private final int timeloc = 1;

    private TextView day;
    private TextView date;

    private TextView code;

    private TextView tempHigh;

    private TextView tempLow;

    private TextView uv;

    private TextView precip;

    public CustomDailyWeatherAdapter(Context ctx)
    {
        context = ctx;
        inflater = LayoutInflater.from(ctx);
    }


    @Override
    public int getCount() {
        return daysInWeek;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

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

        day = convertView.findViewById(R.id.daily_day);
        date = convertView.findViewById(R.id.daily_date);
        ImageView icon = convertView.findViewById(R.id.weather_icon_daily);
        code = convertView.findViewById(R.id.daily_code);
        tempHigh = convertView.findViewById(R.id.daily_temp_high);
        tempLow = convertView.findViewById(R.id.daily_temp_low);
        uv = convertView.findViewById(R.id.daily_uv_index);
        precip = convertView.findViewById(R.id.daily_precip_amount);


        try
        {
            //get date minus year
            String fullDate = IvyWeather.getWeatherData().getJSONObject("daily").getJSONArray("time").getString(position);
            String dayTimeString[] = fullDate.split("T");
            //remove first 5 chars
            String dateShort = dayTimeString[dateloc].substring(5, dayTimeString[dateloc].length());

            Calendar calendar = Calendar.getInstance();
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            boolean isNight = hourOfDay >= 18 || hourOfDay < 6;
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
            icon.setImageDrawable(WeatherCodeHandler.getIcon(weatherCode, isday,convertView.getContext()));
            code.setText(WeatherCodeHandler.weatherStatus(weatherCode));

            //set UV and precip
            uv.setText("UV:" + Double.toString(IvyWeather.getWeatherData().getJSONObject("daily").getJSONArray("uv_index_max").getDouble(position)));
            precip.setText(Double.toString(IvyWeather.getWeatherData().getJSONObject("daily").getJSONArray("precipitation_sum").getDouble(position)) + "mm");

            DayNightMode(convertView,  isNight);
        }
        catch (Exception e)
        {

        }



        return convertView;
    }

    private void DayNightMode(View convertView, boolean isNight){
        int backgroundColor = isNight ? R.drawable.backgroundmain_night : R.drawable.backroundmainpage;
        int textColor = isNight ? R.color.text_night : R.color.text_day;
        int trueTextColor = ContextCompat.getColor(convertView.getContext(), textColor);


        convertView.setBackgroundResource(backgroundColor);
        day.setTextColor(trueTextColor);
        date.setTextColor(trueTextColor);
        code.setTextColor(trueTextColor);
        tempHigh.setTextColor(trueTextColor);
        tempLow.setTextColor(trueTextColor);
        code.setTextColor(trueTextColor);
        uv.setTextColor(trueTextColor);
        precip.setTextColor(trueTextColor);
    }
}
