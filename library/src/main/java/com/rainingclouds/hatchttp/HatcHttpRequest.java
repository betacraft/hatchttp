package com.rainingclouds.hatchttp;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringEncoder;

/**
 * This will be the only class available outside
 * The single entry of all the functionality supported by this library
 * Created by akshay on 05/10/14.
 */
public class HatcHttpRequest {

    /**
     * TAG for logging
     */
    private static final String TAG = "###HatcHttpRequest###";
    /**
     * Underlying http request
     */
    private FullHttpRequest mRequest;
    /**
     * Query string encoder
     */
    private QueryStringEncoder mQueryStringEncoder;

    private HatcHttpRequestListener mListener;

    /**
     * Constructor
     *
     * @param url url for the request
     */
    private HatcHttpRequest(final String url, final HttpMethod method) {
        mRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, method, url);
        mQueryStringEncoder = new QueryStringEncoder(url);
    }


    public static HatcHttpRequest GET(final String url) {
        return new HatcHttpRequest(url, HttpMethod.GET);
    }

    public static HatcHttpRequest POST(final String url) {
        return new HatcHttpRequest(url, HttpMethod.POST);
    }

    public static HatcHttpRequest PUT(final String url) {
        return new HatcHttpRequest(url, HttpMethod.PUT);
    }

    public static HatcHttpRequest DELETE(final String url) {
        return new HatcHttpRequest(url, HttpMethod.DELETE);
    }

    public static HatcHttpRequest PATCH(final String url) {
        return new HatcHttpRequest(url, HttpMethod.PATCH);
    }

    public HatcHttpRequest addHeader(final String name, final Object value) {
        mRequest.headers().add(name, value);
        return this;
    }

    public HatcHttpRequest addParam(final String name, final String value) {
        mQueryStringEncoder.addParam(name, value);
        return this;
    }

    public HatcHttpRequest setContent(final String content) {
        mRequest.content().writeBytes(content.getBytes());
        addHeader("Content-Length", content.length());
        return this;
    }


    String getUrl() {
        return mRequest.getUri();
    }

    HatcHttpRequestListener getListener() {
        return mListener;
    }

    public <T> void execute(final HatcHttpRequestListener hatcHttpRequestListener) {
        mListener = hatcHttpRequestListener;
        HatcHttpClient.getFor(this).writeRequest(mRequest);
    }

}
