package com.example.mad_final;

import static android.content.ContentValues.TAG;

import static java.lang.Boolean.FALSE;

import android.content.Context;
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

    public APIHandler(Handler newHandler, Context newContext) {
        handler = newHandler;
        context = newContext;
    }


    public void getWeather()
    {
        DataHandler dataHandler = new DataHandler();
        Thread downloaderThread = new Thread(dataHandler);
        downloaderThread.start();
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
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "JSON Data Acquired", Toast.LENGTH_SHORT).show();

                    }
                });
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
