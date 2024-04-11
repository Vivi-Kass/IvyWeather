package com.example.mad_final;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomHourlyWeatherAdapter extends BaseAdapter {
    private Context context;
    private static int hoursCount = 168; //7 days each with 24 hours
    private LayoutInflater inflater;
    public CustomHourlyWeatherAdapter(Context ctx)
    {
        context = ctx;
        inflater = LayoutInflater.from(ctx);
    }


    @Override
    public int getCount() {
        return hoursCount;
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

        TextView time = convertView.findViewById(R.id.hourly_time);
        ImageView icon = convertView.findViewById(R.id.weather_icon_hourly);
        TextView code = convertView.findViewById(R.id.hourly_code);
        TextView temperture = convertView.findViewById(R.id.hourly_temp_view);
        TextView feels = convertView.findViewById(R.id.hourly_feel_view);
        TextView precipitationProb = convertView.findViewById(R.id.hourly_precipitation_prob);
        TextView precipitationAmount = convertView.findViewById(R.id.hourly_precipitation_amount);

        time.setText("Time:");



        return convertView;
    }
}
