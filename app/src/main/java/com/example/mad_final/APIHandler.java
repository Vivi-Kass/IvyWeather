/*
 * FILE :            APIHandler.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 02
 * DESCRIPTION :     Contains the logic getting the JSONObject that contains the weather
 */

package com.example.mad_final;

import static android.content.ContentValues.TAG;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class APIHandler {
    private final WeatherDataListener listener;
    private final StringBuilder apiFull = new StringBuilder();
    private final Handler handler;
    private final Context context;
    private JSONObject jsondata;
    private final Location location;

    //Constructor
    public APIHandler(Handler newHandler, Context newContext, Location newLocation, WeatherDataListener listener) {
        handler = newHandler;
        context = newContext;
        location = newLocation;

        //Generate the url for the api call
        String apiStart = "https://api.open-meteo.com/v1/forecast?";
        apiFull.append(apiStart); //add start
        apiFull.append("latitude=").append(location.getLatitude()).append("&");
        apiFull.append("longitude=").append(location.getLongitude());
        //latitude=52.52&longitude=13.41 (API Middle example)
        //API end is what to grab
        String apiEnd = "&current=temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,weather_code&hourly=temperature_2m,apparent_temperature,precipitation_probability,precipitation,weather_code&daily=weather_code,temperature_2m_max,temperature_2m_min,uv_index_max,precipitation_sum&timezone=auto";
        apiFull.append(apiEnd);

        //Start data handler thread
        this.listener = listener;
        DataHandler dataHandler = new DataHandler();
        Thread downloaderThread = new Thread(dataHandler);
        downloaderThread.start();
    }

    //OnData fetched for JSON Object
    public interface WeatherDataListener {
        void onDataFetched(JSONObject jsonData);
    }


    //DataHandler runnable
    public class DataHandler implements Runnable {
        @Override
        public void run() {

            ExecutorService executorService = Executors.newSingleThreadExecutor();

            Future<JSONObject> downloadFuture = executorService.submit(new DataFetcher());
            try {
                jsondata = downloadFuture.get();
            }
            catch (Exception e)
            {
                Log.d(TAG, "Exception occurred when downloading data: " + e.getMessage());
            }

            if (jsondata != null) //only display if an error occured
            {
                //Data fetched
                if (listener != null) {
                    listener.onDataFetched(jsondata);
                }
            }
        }
    }

    //Callable data fetcher
    public class DataFetcher implements Callable{
        @Override
        public Object call() {

            StringBuilder data = new StringBuilder();
            JSONObject jsonObject = null;

            //API call and storage
            try {
                URL url = new URL(apiFull.toString());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while((line = bufferedReader.readLine()) != null) //until end of data append
                {
                    data.append(line);
                }

                if (data.length() > 0)
                {
                    jsonObject = new JSONObject(String.valueOf(data));
                }

            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return jsonObject;
        }
    }


}
