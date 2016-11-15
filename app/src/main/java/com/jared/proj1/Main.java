package com.jared.proj1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
//import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.jared.proj1.data.Channel;
import com.jared.proj1.service.WeatherServiceCallback;
import com.jared.proj1.service.YahooWeatherService;

public class Main extends AppCompatActivity implements SensorEventListener {

    public static final String TAG = "COP4656";
    public String longitudeTXT = "Longitude: ";
    public String latitudeTXT = "Latitude: ";

    private final int REQUEST_CODE = 10;
    private float currentDegree = 0f;
    int REFRESH_TIME = 1000;

    private SensorManager mSensorManager;
    private Sensor mTemperature;
    private Sensor mPressure;

    LocationManager mlocationManager;
    LocationListener mlocationListener;

    /*
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    */
    ImageView image;
    TextView pressure;
    TextView temp;
    TextView compassDisplay;
    TextView longitude;
    TextView latitude;
    Button getCoordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        /*
            add to dependencies if you implement tablayout
            compile 'com.android.support:design:23.1.1'

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            viewPager = (ViewPager) findViewById(R.id.viewPager);
        */
        image = (ImageView) findViewById(R.id.comapss);
        temp = (TextView) findViewById(R.id.temp);
        pressure = (TextView) findViewById(R.id.pressure);
        compassDisplay = (TextView) findViewById(R.id.compassDisplay);
        longitude = (TextView) findViewById(R.id.longitude);
        latitude = (TextView) findViewById(R.id.latitude);
        getCoordinates = (Button) findViewById(R.id.location);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mPressure = (mSensorManager).getDefaultSensor(Sensor.TYPE_PRESSURE);
        mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude.setText(longitudeTXT);
                if(location.getLongitude() > 0) {
                    longitude.append(String.valueOf(location.getLongitude()) + " W");
                }else if(location.getLongitude() < 0){
                    longitude.append(String.valueOf(-1 * location.getLongitude()) + " E");
                }else{
                    longitude.append(String.valueOf(location.getLongitude()));
                }

                latitude.setText(latitudeTXT);
                if(location.getLatitude() > 0) {
                    latitude.append(String.valueOf(location.getLatitude()) + " N");
                }else if(location.getLatitude() < 0){
                    latitude.append(String.valueOf(-1 * location.getLatitude()) + " S");
                }else{
                    latitude.append(String.valueOf(location.getLatitude()));
                }

                WeatherActivity wa = new WeatherActivity();
                wa.WeatherTempGet(location.getLatitude(), location.getLongitude(), findViewById(android.R.id.content));
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
            public void onProviderEnabled(String provider) {

            }
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

        };

        //checks if the version requires user permissions or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.INTERNET},
                                    REQUEST_CODE);
            }else{
                configureButton();
            }
        }else {
            configureButton();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onPause() {
        super.onPause();
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);

    }


    /* This handles the permissions request from the check in onCreate */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Access granted
                    configureButton();
                }
                break;
        }
    }

    /* This is the get coordiantes button */
    private void configureButton() {
        getCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, REFRESH_TIME, 5, mlocationListener);
            }
        });
    }

    /* These functions are for the compass and temperature sensor */
    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            //for the temperature
            float ambient_temp = event.values[0];
            temp.setText("Temperature: " + String.valueOf(ambient_temp) + getResources().getString(R.string.celsius));
        }
        else if(sensor.getType() == Sensor.TYPE_PRESSURE){
            float[] values = event.values;
            pressure.setText("Pressure: " + Math.round(values[0]) + " Millibars");
        }
        else{
            // for the compass
            // get the angle around the z-axis rotated
            int degree = Math.round(event.values[0]);
            compassDisplay.setText("Direction: " + Integer.toString(degree) + "° degrees");

            //create a rotation animation (reverse turn degree degrees)
            RotateAnimation rot_anim = new RotateAnimation(
                    currentDegree,
                    -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);
            // how long the animation will take place
            rot_anim.setDuration(210);
            // set the animation after the end of the reservation status
            rot_anim.setFillAfter(true);
            // Start the animation
            image.startAnimation(rot_anim);
            currentDegree = -degree;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not used just needed for the sensor to compile
    }
}