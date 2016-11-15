package com.jared.proj1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jared.proj1.data.Channel;
import com.jared.proj1.data.Item;
import com.jared.proj1.service.WeatherServiceCallback;
import com.jared.proj1.service.YahooWeatherService;

/**
 * Created by Sam on 11/14/2016.
 */
public class WeatherActivity implements WeatherServiceCallback {
    private TextView temperature;
    private YahooWeatherService yService;
    private ProgressDialog mProgressDialog;
    private Activity main;

    public void WeatherTempGet(double latitude, double longitude, View v){
        temperature = (TextView)v.findViewById(R.id.temp);

        yService = new YahooWeatherService(this);
        /*mProgressDialog = new ProgressDialog(v);
        mProgressDialog.setMessage("Loading weather...");
        mProgressDialog.show();*/
        yService.refreshWeather("Tallahassee, FL");
    }

    @Override
    public void serviceSuccess(Channel channel) {
        //mProgressDialog.hide();

        Item item = channel.getItem();

        temperature.setText("Pressure: "+item.getCondition().getTemperature()+"\u00B0 "
                +channel.getUnits().getTemperature());

    }

    @Override
    public void serviceFailure(Exception exception) {
        Log.e("serviceFailure", "exception", exception);
    }
}
