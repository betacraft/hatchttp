package com.rainingclouds.hatchttp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Hatchttp caller options
 * Created by akshay on 22/08/14.
 */
public final class HatcHttpCallerOptions {

    private static final String TAG = "###HatcHttpCallerOptions###";
    private Bootstrap mBootstrap = new Bootstrap();
    private NioEventLoopGroup mWorker = new NioEventLoopGroup(3);
    private int mConnectionTimeout = 30000;
    private int mMaxConnectionRetries = 3;

    public HatcHttpCallerOptions setWorkerThreads(final int workerThreads){
        mWorker = new NioEventLoopGroup(workerThreads);
        return this;
    }

    public HatcHttpCallerOptions setConnectionTimeout(final int connectionTimeout){
        mConnectionTimeout = connectionTimeout;
        return this;
    }

    public HatcHttpCallerOptions setConnectionRetries(final int maxConnectionRetries){
        mMaxConnectionRetries = maxConnectionRetries;
        return this;
    }

    NioEventLoopGroup getWorker(){
        return mWorker;
    }
    Bootstrap getBootstrap(){return mBootstrap;}

}
