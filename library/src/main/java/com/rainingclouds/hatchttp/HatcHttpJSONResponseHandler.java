package com.rainingclouds.hatchttp;

import android.util.Log;

import org.json.JSONObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

/**
 * This handler handles the data coming from the server
 * Created by akshay on 05/10/14.
 */
class HatcHttpJSONResponseHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final String TAG = "###HatcHttpResponseHandler###";
    private boolean readingChunks;
    private HatcHttpJSONListener mClientHandlerListener;
    private HttpResponseStatus mResponseStatus;
    private HttpHeaders mHttpHeaders;
    private StringBuilder mResponse;

    {
        mResponse = new StringBuilder();
    }

    HatcHttpJSONResponseHandler(final HatcHttpJSONListener clientHandlerListener) {
        mClientHandlerListener = clientHandlerListener;
    }


    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final HttpObject msg) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            mResponseStatus = response.getStatus();
            mHttpHeaders = response.headers();
            if(!mHttpHeaders.contains(HttpHeaders.Names.CONTENT_TYPE)){
                Log.e(TAG,"No header for content type");
                throw new IllegalStateException("Content-Type is not available in header");
            }
            if(!mHttpHeaders.get(HttpHeaders.Names.CONTENT_TYPE).equalsIgnoreCase("application/json")){
                Log.e(TAG,"Content-Type is not application/json");
                throw new IllegalStateException("Content-Type is not application/json");
            }
        }
        if (msg instanceof HttpContent) {
            HttpContent chunk = (HttpContent) msg;
            Log.d(TAG,chunk.toString());
            mResponse.append(chunk.content().toString(CharsetUtil.UTF_8));
            if (chunk instanceof LastHttpContent) {
                if (readingChunks) {
                    Log.d(TAG, "End of content");
                } else {
                    Log.d(TAG, "End of content");
                }
                Log.d(TAG, "Got response as:" + mResponse.toString());
                readingChunks = false;
                mClientHandlerListener.onComplete(mResponseStatus, mHttpHeaders, new JSONObject(mResponse.toString()));
                ctx.channel().close();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.channel().close();
        mClientHandlerListener.onException(cause);
    }
}