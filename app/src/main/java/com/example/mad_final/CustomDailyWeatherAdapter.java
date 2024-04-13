package com.example.mad_final;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CustomDailyWeatherAdapter extends BaseAdapter {

    private final int daysInWeek = 7;
    private Context context;
    private LayoutInflater inflater;

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

        return convertView;
    }
}
