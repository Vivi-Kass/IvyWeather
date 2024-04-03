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
import android.widget.Toast;
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

    private String api = "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m,apparent_temperature,precipitation_probability,precipitation";
    private Handler handler;
    private Context context;
    private JSONObject jsondata;
    private Location location;

    public APIHandler(Handler newHandler, Context newContext, Location newLocation) {
        handler = newHandler;
        context = newContext;
        location = newLocation;
        DataHandler dataHandler = new DataHandler();
        Thread downloaderThread = new Thread(dataHandler);
        downloaderThread.start();
    }

    public Location getLocation()
    {
        return location;
    }

    public JSONObject getData()
    {
        if (jsondata != null)
        {
            return  jsondata;
        }
        else
        {
            return null;
        }
    }

    public class UpdateUI implements Runnable
    {
        @Override
        public void run()
        {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "JSON Data Acquired", Toast.LENGTH_SHORT).show();
                    //put code to update ui with data here!
                }
            });
        }
    }



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
                UpdateUI updateUI  = new UpdateUI();
                Thread downloaderThread = new Thread(updateUI);
                downloaderThread.start();
            }


        }

    }

    public class DataFetcher implements Callable{
        @Override
        public Object call() {

            StringBuffer data = new StringBuffer();
            JSONObject jsonObject = null;

            try {
                URL url = new URL(api);
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
