package com.rainingclouds.hatchttp.exception;

/**
 * Created by sudam on 06/09/15.
 */
public class NoConnectionError extends com.android.volley.NoConnectionError {
    public NoConnectionError() {
        super();
    }

    public NoConnectionError(Throwable reason) {
        super(reason);
    }
}
