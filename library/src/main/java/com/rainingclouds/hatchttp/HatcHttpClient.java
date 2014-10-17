package com.rainingclouds.hatchttp;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.rainingclouds.hatchttp.executors.EfficientThreadPoolExecutor;

import java.util.concurrent.TimeUnit;

/**
 * NingHttp Client
 * Created by akshay on 22/08/14.
 */
class HatcHttpClient {

    final static AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder()
            .setAllowPoolingConnection(true)
            .setFollowRedirects(true)
            .setExecutorService(EfficientThreadPoolExecutor.get(3,10,1, TimeUnit.MINUTES,4,"hatcHttp_workers"))
            .setRequestTimeoutInMs(30000)
            .build();
    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient(config);

    public static AsyncHttpClient get() {
        return asyncHttpClient;
    }

}
