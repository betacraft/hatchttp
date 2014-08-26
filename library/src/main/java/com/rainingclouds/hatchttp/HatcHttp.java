package com.rainingclouds.hatchttp;


/**
 * A static to help to use different REST APIs
 *
 * @author Akshay Deo
 * @version 0.2
 * @since 0.1
 */
public class HatcHttp {

    /**
     * Debug tag to be used in Log
     */
    private static final String TAG = "###HatcHttpCalled###";
    private static HatcHttp mInstance;
    private HatcHttpOptions mCallerOptions = new HatcHttpOptions();


    public static HatcHttp Init(){
        if(mInstance != null){
            return mInstance;
        }
        mInstance = new HatcHttp();
        return mInstance;
    }

    public static HatcHttp SetOptions(final HatcHttpOptions options){
        Init().mCallerOptions = options;
        return mInstance;
    }

    static HatcHttp getInstance(){
        Init();
        return mInstance;
    }

    HatcHttpOptions getOptions(){
        return mCallerOptions;
    }
}