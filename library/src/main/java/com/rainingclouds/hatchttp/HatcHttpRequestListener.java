package com.rainingclouds.hatchttp;

/**
 * HatcHttpRequestListener
 * Created by akshay on 05/10/14.
 */
public interface HatcHttpRequestListener {
    void onComplete(final int status, final String response);
    void onException(final Throwable throwable);
}
