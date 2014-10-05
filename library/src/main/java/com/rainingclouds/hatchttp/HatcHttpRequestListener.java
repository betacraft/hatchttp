package com.rainingclouds.hatchttp;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * HatcHttpRequestListener
 * Created by akshay on 05/10/14.
 */
public interface HatcHttpRequestListener {
    void onComplete(final HttpResponseStatus status, final HttpHeaders headers,final String response);
    void onException(final Throwable throwable);
}
