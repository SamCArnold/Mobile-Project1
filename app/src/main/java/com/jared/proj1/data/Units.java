package com.jared.proj1.data;

import org.json.JSONObject;

/**
 * Created by Sam on 11/14/2016.
 */
public class Units implements JSONPopulator {
    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
    }
}
