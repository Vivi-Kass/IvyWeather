package com.example.mad_final;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class CustomHourlyWeatherAdapter extends BaseAdapter {
    private Context context;
    private static int hoursCount = 168; //7 days each with 24 hours
    private int offset;
    private LayoutInflater inflater;
    private final int dateloc = 0;
    private final int timeloc = 1;
    public CustomHourlyWeatherAdapter(Context ctx)
    {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.activity_hourly_weather_list_view, null);

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
            dayTimeString = IvyWeather.getWeatherData().getJSONObject("hourly").getJSONArray("time").getString(realPosition).split("T");
            date.setText(dayTimeString[dateloc]);
            time.setText(dayTimeString[timeloc]);
        }
        catch (Exception e)
        {
            Log.d(TAG,"Failed to get JSON data for hourly weather");
        }

        return convertView;
    }
}
