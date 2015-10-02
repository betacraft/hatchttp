package com.rainingclouds.hatchttp;

/**
 * Created by sudam on 05/09/15.
 */
public class HatcHttpConfig {

    private static int mTimeOut;

    /**
     * Set timeout in milliseconds
     *
     * @param timeOut timeout for HatcHttpRequest
     */
    public static void setRequestTimeOut(final int timeOut){
        mTimeOut = timeOut;
    }

    /**
     * Returns timeout in milliseconds
     */
    public static int getRequestTimeOut(){
        return mTimeOut;
    }
}
