package com.rainingclouds.hatchttp;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;

import java.util.concurrent.Executors;

/**
 * NingHttp Client
 * Created by akshay on 22/08/14.
 */
class HatcHttpClient {

    final static AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder()
            .setAllowPoolingConnection(true)
            .setFollowRedirects(true)
            .setExecutorService(Executors.newFixedThreadPool(2))
            .setRequestTimeoutInMs(30000)
            .build();
    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient(config);

    public static AsyncHttpClient get() {
        return asyncHttpClient;
    }

}
