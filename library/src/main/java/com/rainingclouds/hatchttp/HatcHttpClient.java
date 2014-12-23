package com.rainingclouds.hatchttp;


import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;

/**
 * Created by akshay on 22/08/14.
 */
class HatcHttpClient {
    private static final String TAG = "###HatcHttpClient";


    private RequestQueue mRequestQueue;


    private static HatcHttpClient Instance;

    private HatcHttpClient() {
        final Cache cache = new NoCache();
        final Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
    }

    public static HatcHttpClient Get() {
        if (Instance != null)
            return Instance;
        Instance = new HatcHttpClient();
        return Instance;
    }

    public <T> void addRequest(final Request<T> request) {
        mRequestQueue.add(request);
    }

}
