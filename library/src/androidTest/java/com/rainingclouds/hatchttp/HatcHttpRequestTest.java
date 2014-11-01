package com.rainingclouds.hatchttp;

import junit.framework.TestCase;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;

public class HatcHttpRequestTest extends TestCase {

    private static final String TAG = "###HatcHttpRequestTest###";

    public void testGET() throws Exception {
        HatcHttpRequest.GET("http://google.com")
                .execute(new HatcHttpRequestListener() {
                    @Override
                    public void onComplete(HttpResponseStatus status, HttpHeaders headers, String response) {
                        assertTrue(true);
                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });
    }
}