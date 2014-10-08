package com.rainingclouds.hatchttp;

import io.netty.channel.SimpleChannelInboundHandler;
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
    /**
     * HatcHttp request response handler
     */
    private SimpleChannelInboundHandler mResponseHandler;

    /**
     * Constructor
     *
     * @param url url for the request
     */
    private HatcHttpRequest(final String url, final HttpMethod method) {
        mRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, method, url);
        mQueryStringEncoder = new QueryStringEncoder(url);
    }

    /**
     * GET request
     *
     * @param url url for the request
     * @return current instance of @HatcHttpRequest
     */
    public static HatcHttpRequest GET(final String url) {
        return new HatcHttpRequest(url, HttpMethod.GET);
    }

    /**
     * POST request
     *
     * @param url url for the request
     * @return current instance of @HatcHttpRequest
     */
    public static HatcHttpRequest POST(final String url) {
        return new HatcHttpRequest(url, HttpMethod.POST);
    }

    /**
     * PUT request
     *
     * @param url url for the request
     * @return current instance of @HatcHttpRequest
     */
    public static HatcHttpRequest PUT(final String url) {
        return new HatcHttpRequest(url, HttpMethod.PUT);
    }

    /**
     * DELETE request
     *
     * @param url url for the request
     * @return current instance of @HatcHttpRequest
     */
    public static HatcHttpRequest DELETE(final String url) {
        return new HatcHttpRequest(url, HttpMethod.DELETE);
    }

    /**
     * PATCH request
     *
     * @param url url for the request
     * @return current instance of @HatcHttpRequest
     */
    public static HatcHttpRequest PATCH(final String url) {
        return new HatcHttpRequest(url, HttpMethod.PATCH);
    }

    /**
     * To add header in your request
     *
     * @param name  name of the header
     * @param value value of the header
     * @return current instance of @HatcHttpRequest
     */
    public HatcHttpRequest addHeader(final String name, final Object value) {
        mRequest.headers().add(name, value);
        return this;
    }

    /**
     * To add param
     *
     * @param name  name of the param
     * @param value value of the param
     * @return current instance of @HatcHttpRequest
     */
    public HatcHttpRequest addParam(final String name, final String value) {
        mQueryStringEncoder.addParam(name, value);
        return this;
    }


    /**
     * To set content of the current request
     *
     * @param content content
     * @return current instance of @HatcHttpRequest
     */
    public HatcHttpRequest setContent(final String content) {
        mRequest.content().writeBytes(content.getBytes());
        addHeader("Content-Length", content.length());
        return this;
    }


    String getUrl() {
        return mRequest.getUri();
    }

    SimpleChannelInboundHandler getResponseHandler(){return mResponseHandler;}

    /**
     * To execute the request
     *
     * @param hatcHttpRequestListener listener for this request
     */
    public void execute(final HatcHttpRequestListener hatcHttpRequestListener) {
        HatcHttpExecutor.Submit(new Runnable() {
            @Override
            public void run() {
                mResponseHandler = new HatcHttpResponseHandler(hatcHttpRequestListener);
                HatcHttpClient.getFor(HatcHttpRequest.this).writeRequest(mRequest);
            }
        });
    }

    public void execute(final HatcHttpJSONListener hatcHttpJSONListener){
        HatcHttpExecutor.Submit(new Runnable() {
            @Override
            public void run() {
                mResponseHandler = new HatcHttpJSONResponseHandler(hatcHttpJSONListener);
                HatcHttpClient.getFor(HatcHttpRequest.this).writeRequest(mRequest);
            }
        });
    }


}
