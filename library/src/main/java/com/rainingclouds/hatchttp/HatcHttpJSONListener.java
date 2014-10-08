package com.rainingclouds.hatchttp;

import org.json.JSONObject;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * HatcHttpRequestListener
 * Created by akshay on 05/10/14.
 */
public interface HatcHttpJSONListener {
    void onComplete(final HttpResponseStatus status, final HttpHeaders headers, final JSONObject response);
    void onException(final Throwable throwable);
}
