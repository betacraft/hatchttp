package com.rainingclouds.hatchttp.exception;

/**
 * This is wrapper class for all the exceptions happening in the library
 * Created by akshay on 7/22/14.
 */
public final class HatcHttpException extends Exception {
    /**
     * Logger
     */
    private static final String TAG = "###HatcHttpException###";
    /**
     * Error code
     */
    private HatcHttpErrorCode mErrorCode;
    /**
     * Underlying exception
     */
    private Throwable mThrowable;
    /**
     * HTTP Status code
     */
    private int mHttpStatusCode;
    /**
     * HTTP Response
     */
    private String mHttpResponse;

    /**
     * Getter for error code associated with this exception
     *
     * @return error code see @link{HatcHttpErrorCode}
     */
    public HatcHttpErrorCode getErrorCode() {
        return mErrorCode;
    }


    public int getHttpStatusCode(){
        return mHttpStatusCode;
    }

    public String getHttpResponse(){
        return mHttpResponse;
    }

    /**
     * Getter for underlying exception
     *
     * @return @Throwable
     */
    public Throwable getThrowable() {
        return mThrowable;
    }

    /**
     * Constructor
     *
     * @param errorCode error code related with this exception see @link{HatcHttpErrorCode}
     */
    public HatcHttpException(final HatcHttpErrorCode errorCode) {
        mErrorCode = errorCode;
    }


    /**
     *
     * @param httpStatus
     */
    public HatcHttpException(final int httpStatus, final String httpResponse){
        mErrorCode = HatcHttpErrorCode.WEB_APP_EXCEPTION;
        mHttpStatusCode = httpStatus;
        mHttpResponse = httpResponse;
    }

    /**
     * Constructor
     *
     * @param errorCode error code related with this exception see @link{HatcHttpErrorCode}
     */
    public HatcHttpException(final HatcHttpErrorCode errorCode, final Throwable throwable) {
        mErrorCode = errorCode;
        mThrowable = throwable;
    }

}
