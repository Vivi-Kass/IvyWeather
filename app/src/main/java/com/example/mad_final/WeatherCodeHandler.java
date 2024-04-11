/*
 * FILE :            WeatherCodeHandler.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 10
 * DESCRIPTION :     Handles logic for weather codes and icons
 */

package com.example.mad_final;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.IntStream;

public class WeatherCodeHandler {

    private static int isday = 1;
    private static List<Integer> sun = new ArrayList<>(Arrays.asList(0, 1));
    private static List<Integer> sunCloud = new ArrayList<>(Arrays.asList(2));
    private static List<Integer> moon = new ArrayList<>(Arrays.asList(0, 1));
    private static List<Integer> moonCloud = new ArrayList<>(Arrays.asList(2));
    private static List<Integer> cloud = new ArrayList<>(Arrays.asList(3, 45, 48));
    private static List<Integer> rain = new ArrayList<>(Arrays.asList(51, 53, 55, 61, 63, 65, 80, 81, 82));
    private static List<Integer> freezingRain = new ArrayList<>(Arrays.asList(56, 57, 66, 67));
    private static List<Integer> snow = new ArrayList<>(Arrays.asList(71, 73, 75, 77, 85, 86));
    private static List<Integer> thunder = new ArrayList<>(Arrays.asList(95));

    private static Map<Integer, String> weatherCodes = new HashMap<Integer, String>() {{
        //Sky
        put(0, "Clear Sky");
        put(1, "Mainly Clear");
        put(2, "Partly Cloudy");
        put(3, "Overcast");

        //Fog
        put(45, "Fog");
        put(48, "Depositing Rime Fog");

        //Rain/Drizzle/Showers
        put(51, "Light Drizzle");
        put(53, "Moderate Drizzle");
        put(55, "Heavy Drizzle");

        put(61, "Light Rain");
        put(63, "Moderate Rain");
        put(65, "Heavy Rain");

        put(80, "Light Rain Showers");
        put(81, "Moderate Rain Showers");
        put(82, "Heavy Rain Showers");

        //Freezing rain/drizzle
        put(56, "Light Freezing Drizzle");
        put(57, "Heavy Freezing Drizzle");

        put(66, "Light Freezing Rain");
        put(67, "Heavy Freezing Rain");

        //Snow
        put(71, "Light Snow");
        put(73, "Moderate Snow");
        put(75, "Heavy Snow");
        put(77, "Snow Grains");

        put(85, "Light Snow Showers");
        put(86, "Heavy Snow Showers");

        //ThunderStorms
        put(95, "Thunderstorms");
    }};

    public static String weatherStatus(int code)
    {
        return weatherCodes.get(code);
    }

    public static Drawable getIcon(int weatherCode, int dayStatus, Context ctx)
    {
        Drawable icon = ctx.getResources().getDrawable(R.drawable.ic_launcher_foreground); //this is a way to ensure we catch issues
        if (weatherCode >= 3) //no moon or sun
        {
            if (cloud.contains(weatherCode))
            {
                icon = ctx.getResources().getDrawable(R.drawable.clouds);
            }
            else if (rain.contains(weatherCode))
            {
                icon = ctx.getResources().getDrawable(R.drawable.rain);
            }
            else if (freezingRain.contains(weatherCode))
            {
                icon = ctx.getResources().getDrawable(R.drawable.rain);
            }
            else if (snow.contains(weatherCode))
            {
                icon = ctx.getResources().getDrawable(R.drawable.snow);
            }
            else if (thunder.contains(weatherCode))
            {
                icon = ctx.getResources().getDrawable(R.drawable.thunder);
            }
        }
        else //moon or sun
        {
            if (dayStatus == isday) //sun icon
            {
                if (sun.contains(weatherCode))
                {
                    icon = ctx.getResources().getDrawable(R.drawable.sun);
                }
                else if (sunCloud.contains(weatherCode))
                {
                    icon = ctx.getResources().getDrawable(R.drawable.sun_cloud);
                }

            }
            else //moon icon
            {
                if (moon.contains(weatherCode))
                {
                    icon = ctx.getResources().getDrawable(R.drawable.moon);
                }
                else if (moonCloud.contains(weatherCode))
                {
                    icon = ctx.getResources().getDrawable(R.drawable.moon_cloud);
                }
            }
        }

        return icon;
    }




}
