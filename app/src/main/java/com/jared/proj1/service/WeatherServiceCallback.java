package com.jared.proj1.service;

import com.jared.proj1.data.Channel;

/**
 * Created by Sam on 11/14/2016.
 */
public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);

    void serviceFailure(Exception exception);
}
