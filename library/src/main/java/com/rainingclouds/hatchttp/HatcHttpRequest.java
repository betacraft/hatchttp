package com.rainingclouds.hatchttp;

import android.util.Log;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringEncoder;

/**
 * HatcHttp request a wrapper over HttpRequest
 * Created by akshay on 25/08/14.
 */
public final class HatcHttpRequest {
    /**
     * TAG for logging
     */
    private static final String TAG = "###HatcHttpRequst###";
    /**
     * Encoder for params
     */
    private QueryStringEncoder mQueryStringEncoder;
    /**
     * Underlying request
     */
    private FullHttpRequest mHttpRequest;
    private NettyHttpClient.NettyHttpClientListener mClientChannelListener;
    private HatcHttpRequestHandler mRequestHandler;

    {
       mClientChannelListener = new NettyHttpClient.NettyHttpClientListener
                () {
            @Override
            public void connectionFailed(Throwable throwable) {
                Log.d(TAG, "Connection failed", throwable);
            }

            @Override
            public void connectionSuccess() {
                Log.d(TAG, "Connection successful");
            }
        };
    }

    private HatcHttpRequest(final HatcHttpMethod method, final String url){
        mHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,method.getHttpMethod(),url);
        mQueryStringEncoder = new QueryStringEncoder(url);
    }

    public HatcHttpRequest addHeader(final String name, final Object value){
        mHttpRequest.headers().add(name,value);
        return this;
    }

    public HatcHttpRequest addParam(final String name, final String value){
        mQueryStringEncoder.addParam(name,value);
        return this;
    }

    public HatcHttpRequest setContent(final String content){
        mHttpRequest.content().writeBytes(content.getBytes());
        addHeader("Content-Length",content.length());
        return this;
    }

    String getUrl(){
        return mHttpRequest.getUri();
    }



    HttpRequest getHttpRequest(){
        return mHttpRequest;
    }

    HatcHttpOptions getOptions(){
        return HatcHttp.getInstance().getOptions();
    }

    NettyHttpClient.NettyHttpClientListener getClientChannelListener(){
        return mClientChannelListener;
    }

    HatcHttpRequestHandler getRequestHandler(){
        return mRequestHandler;
    }

    public HttpHeaders getHeaders(){
        return mHttpRequest.headers();
    }

    public static HatcHttpRequest prepareFor(final HatcHttpMethod method,
                                             final String url) {
        return new HatcHttpRequest(method,url);
    }


    public void execute(final HatcHttpTask.HatcHttpRequestListener clientHandlerListener){
        try {
            mHttpRequest.setUri(mQueryStringEncoder.toUri().toASCIIString());
        }catch (Exception e){
            clientHandlerListener.onException(e);
        }
        mRequestHandler = new HatcHttpRequestHandler(clientHandlerListener);
        NettyHttpClient httpClient =  NettyHttpClient.getFor(this);
        httpClient.writeRequest(mHttpRequest);
    }
}
