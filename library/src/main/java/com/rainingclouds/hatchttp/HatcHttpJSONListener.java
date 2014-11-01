package com.rainingclouds.hatchttp;

import org.json.JSONObject;


/**
 * HatcHttpRequestListener
 * Created by akshay on 05/10/14.
 */
public interface HatcHttpJSONListener {
    void onComplete(final int status, final JSONObject response);

    void onException(final Throwable throwable);
}
