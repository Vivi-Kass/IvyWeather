/*
 * FILE :            MainActivity.java
 * PROJECT :         PROG3150 - Project
 * PROGRAMMER :      Vivian Morton, Isaac Ribeiro Leao
 * FIRST VERSION :   2024 - 04 - 02
 * DESCRIPTION :     Contains the logic for the main activity
 */

package com.example.mad_final;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
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
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private FrameLayout framelayout;
    private TextView textView;
    private Button button;
    private final Handler handler = new Handler();




    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){


        // check if user is connected to wifi
        if (IvyWeather.CheckWifiConnection(this)) {
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
            // user isnt connected, so do not allow him to move to any other pages
            Toast.makeText(MainActivity.this, "You're not connected to the internet", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Get views
        framelayout = findViewById(R.id.fragment_frame);
        textView = findViewById(R.id.text_info);
        button = findViewById(R.id.reload_button);

        //Check permissions and prompt is necessary
        PermissionChecker.promptPrecise(this);
        PermissionChecker.promptCoarse(this);

        // before calling the API, check for network connection
        if (IvyWeather.CheckWifiConnection(this)) {
            if(PermissionChecker.checkPermissions(this))
            {
                getLocationCallAPI();
            }
            else
            {
                String needLocation = "This app needs the location to be allowed in order to work.\n Please enable it in settings.";
                textView.setText(needLocation);
                button.setVisibility(View.VISIBLE);
            }
        }
        else {

            // wifi isnt connected, check the connection
            String wifiError = "App current offline \n Please connect to a stable wifi connection.";
            textView.setText(wifiError);
            button.setVisibility(View.VISIBLE);
        }

            //On click listener
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if(IvyWeather.CheckWifiConnection(view.getContext())) {

                        if(PermissionChecker.checkPermissions(MainActivity.this))
                        {
                            getLocationCallAPI();
                            textView.setText(R.string.loading);
                        }
                        else
                        {
                            String retryMessage = "Error in updating app, Please try again";
                            textView.setText(retryMessage);
                            button.setVisibility(View.VISIBLE);
                            Toast.makeText(MainActivity.this, "Location Not Allowed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        // wifi isnt connected, check the connection
                        String wifiError = "App current offline \n Please connect to a stable wifi connection.";
                        textView.setText(wifiError);
                        button.setVisibility(View.VISIBLE);
                    }

                }
            });




    }



    private void startCurrentWeatherFragment()
    {
        //Hide elements that aren't used
        textView.setVisibility(View.GONE);
        button.setVisibility(View.GONE);

        //enable framelayout
        framelayout.setVisibility(View.VISIBLE);

        //start fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        CurrentWeatherFragment currentWeatherFragment = new CurrentWeatherFragment();
        Log.d(TAG, "App starting. Setting fragment to currentWeatherFragment");
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, currentWeatherFragment)
                .commit();
    }

    private void startHourlyWeatherFragment()
    {
        //Hide elements that aren't used
        textView.setVisibility(View.GONE);
        button.setVisibility(View.GONE);

        //enable framelayout
        framelayout.setVisibility(View.VISIBLE);

        //start fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        HourlyWeatherFragment hourlyWeatherFragment = new HourlyWeatherFragment();
        Log.d(TAG, "App starting. Setting fragment to currentWeatherFragment");
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, hourlyWeatherFragment)
                .commit();
    }

    private void startDailyWeatherFragment()
    {
        //Hide elements that aren't used
        textView.setVisibility(View.GONE);
        button.setVisibility(View.GONE);

        //enable framelayout
        framelayout.setVisibility(View.VISIBLE);

        //start fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        DailyWeatherFragment dailyWeatherFragment = new DailyWeatherFragment();
        Log.d(TAG, "App starting. Setting fragment to currentWeatherFragment");
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, dailyWeatherFragment)
                .commit();
    }

    private void startCreditsFragment()
    {
        //Hide elements that aren't used
        textView.setVisibility(View.GONE);
        button.setVisibility(View.GONE);

        //enable framelayout
        framelayout.setVisibility(View.VISIBLE);

        //start fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        CreditsFragment creditsFragment = new CreditsFragment();
        Log.d(TAG, "App starting. Setting fragment to currentWeatherFragment");
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, creditsFragment)
                .commit();
    }


    //Call api to get location
    private void getLocationCallAPI()
    {
        try {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                IvyWeather.setUserLocation(location);
                                Log.d(TAG, "Location acquired");
                                APIHandler apiHandler = new APIHandler(handler, getBaseContext(), IvyWeather.getUserLocation(), new APIHandler.WeatherDataListener() {
                                    @Override
                                    public void onDataFetched(JSONObject jsonData) {

                                        //Once Json data is fetched, update UI
                                        IvyWeather.setWeatherData(jsonData);
                                        IvyWeather.setCity(IvyWeather.getUserLocation(), getBaseContext());


                                        //Once finished start the current weather fragment
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
                    //Log failure
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


}