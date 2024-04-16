/*
 * FILE :            MainActivity.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 02
 * DESCRIPTION :     Contains the logic for the main activity
 * CREDITS: https://www.vecteezy.com/free-vector/weather-icons Weather Icons Vectors by Vecteezy
 */

package com.example.mad_final;

import static android.content.ContentValues.TAG;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    private FrameLayout framelayout;
    private TextView textView;
    private Button button;

    public Menu menu;
    private final Handler handler = new Handler();
    private FusedLocationProviderClient fusedLocationClient;
    private final String needLocation = "This app needs the location to be allowed in order to work.\n Please enable it in settings.";

    private WifiManager currcon;
    private  WifiInfo userWifi;

   private  ConnectivityManager currcelular;

   private  NetworkInfo userCelular;



    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu = findViewById(R.menu.menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        if (CheckWifiConnection()) {
            int id = item.getItemId();
            if (id == R.id.current_weather && PermissionChecker.checkPermissions(this)) {
                startCurrentWeatherFragment();
                return true;
            } else if (id == R.id.hourly_weather && PermissionChecker.checkPermissions(this)) {
                startHourlyWeatherFragment();
                return true;
            } else if (id == R.id.daily_weather && PermissionChecker.checkPermissions(this)) {
                startDailyWeatherFragment();
                return true;
            } else if (id == R.id.credits) {
                startCreditsFragment();
                return true;
            }
            else
            {
                //otherwise can't change screen
                Toast.makeText(MainActivity.this, "Location Not Allowed", Toast.LENGTH_SHORT).show();
            }
            return super.onOptionsItemSelected(item);
        }
        else {

            Toast.makeText(MainActivity.this, "You're not connected to the internet", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        framelayout = findViewById(R.id.fragment_frame);
        textView = findViewById(R.id.text_info);
        button = findViewById(R.id.reload_button);

        //Check permissions and prompt is necessary
        PermissionChecker.promptPrecise(this);
        PermissionChecker.promptCoarse(this);
        if (CheckWifiConnection()) {
            if(PermissionChecker.checkPermissions(this))
            {
                getLocationCallAPI();
            }
            else
            {
                textView.setText(needLocation);
                button.setVisibility(View.VISIBLE);
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(PermissionChecker.checkPermissions(MainActivity.this))
                    {
                        getLocationCallAPI();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Location Not Allowed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(MainActivity.this, "You're not connected to the internet", Toast.LENGTH_SHORT).show();
        }



    }



    private void startCurrentWeatherFragment()
    {
        textView.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        framelayout.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        CurrentWeatherFragment currentWeatherFragment = new CurrentWeatherFragment();
        Log.d(TAG, "App starting. Setting fragment to currentWeatherFragment");
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, currentWeatherFragment)
                .commit();
    }

    private void startHourlyWeatherFragment()
    {
        textView.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        framelayout.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        HourlyWeatherFragment hourlyWeatherFragment = new HourlyWeatherFragment();
        Log.d(TAG, "App starting. Setting fragment to currentWeatherFragment");
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, hourlyWeatherFragment)
                .commit();
    }

    private void startDailyWeatherFragment()
    {
        textView.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        framelayout.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        DailyWeatherFragment dailyWeatherFragment = new DailyWeatherFragment();
        Log.d(TAG, "App starting. Setting fragment to currentWeatherFragment");
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, dailyWeatherFragment)
                .commit();
    }

    private void startCreditsFragment()
    {
        textView.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        framelayout.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        CreditsFragment creditsFragment = new CreditsFragment();
        Log.d(TAG, "App starting. Setting fragment to currentWeatherFragment");
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, creditsFragment)
                .commit();
    }


    private void getLocationCallAPI()
    {
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(Location location) {
                            //String longitude = Double.toString(location.getLongitude());
                            //String latitude = Double.toString(location.getLatitude());
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                IvyWeather.setUserLocation(location);
                                Log.d(TAG, "Location acquired");
                                APIHandler apiHandler = new APIHandler(handler, getBaseContext(), IvyWeather.getUserLocation(), new APIHandler.WeatherDataListener() {
                                    @Override
                                    public void onDataFetched(JSONObject jsonData) {
                                        //Once Json data is fetched, update UI
                                        IvyWeather.setWeatherData(jsonData);

                                        IvyWeather.setCity(IvyWeather.getUserLocation(), getBaseContext());



                                        //send parameters to fragment
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //put code to update ui with data here!
                                                startCurrentWeatherFragment();
                                            }
                                        });
                                    }
                                });

                            }

                        }

                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("CurrentWeatherFragment", "Failed to get location");
                        }
                    });
        }
        catch (SecurityException e)
        {
            Log.d(TAG, "Security Exception: " + e.getMessage());
        }

    }


    private boolean CheckWifiConnection(){
        currcon = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (currcon.isWifiEnabled()) { // means Wi-Fi adapter is ON
            userWifi = currcon.getConnectionInfo();
            if(userWifi.getNetworkId() != -1 ) {
                return true;   // no network connection
            }
        }
        else {
            currcelular = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            userCelular = currcelular.getActiveNetworkInfo();
            if (userCelular != null && userCelular.isConnected()) {
                boolean isMobile = userCelular.getType() == ConnectivityManager.TYPE_MOBILE;
                if (isMobile) {
                    return true;
                }
            }
        }
        return false;
    }


}