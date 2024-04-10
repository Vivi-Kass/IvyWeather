/*
 * FILE :            WeatherCodeHandler.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 10
 * DESCRIPTION :     Handles logic for weather codes and icons
 */

package com.example.mad_final;

import java.util.HashMap;
import java.util.Map;

public class WeatherCodeHandler {
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




}
