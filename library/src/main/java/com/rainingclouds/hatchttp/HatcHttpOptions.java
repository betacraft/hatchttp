package com.rainingclouds.hatchttp;



import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Hatchttp  options
 * Created by akshay on 22/08/14.
 */
public final class HatcHttpOptions {

    private static final String TAG = "###HatcHttpCallerOptions###";

    private NioEventLoopGroup mWorker = new NioEventLoopGroup(3);
    private int mConnectionTimeout = 30000;
    private int mMaxConnectionRetries = 3;


    public HatcHttpOptions setWorkerThreads(final int workerThreads){
        mWorker = new NioEventLoopGroup(workerThreads);
        return this;
    }

    public HatcHttpOptions setConnectionTimeout(final int connectionTimeout){
        mConnectionTimeout = connectionTimeout;
        return this;
    }

    public HatcHttpOptions setConnectionRetries(final int maxConnectionRetries){
        mMaxConnectionRetries = maxConnectionRetries;
        return this;
    }

    NioEventLoopGroup getWorker(){
        return mWorker;
    }


}
