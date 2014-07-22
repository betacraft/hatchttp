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
     * Getter for error code associated with this exception
     *
     * @return error code see @link{HatcHttpErrorCode}
     */
    public HatcHttpErrorCode getErrorCode() {
        return mErrorCode;
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
     * Constructor
     *
     * @param errorCode error code related with this exception see @link{HatcHttpErrorCode}
     */
    public HatcHttpException(final HatcHttpErrorCode errorCode, final Throwable throwable) {
        mErrorCode = errorCode;
        mThrowable = throwable;
    }

}
