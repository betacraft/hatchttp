package com.rainingclouds.hatchttp;

import android.util.Log;

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
class HatcHttpResponseHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final String TAG = "###HatcHttpResponseHandler###";
    private boolean readingChunks;
    private HatcHttpRequestListener mClientHandlerListener;
    private HttpResponseStatus mResponseStatus;
    private HttpHeaders mHttpHeaders;
    private StringBuilder mResponse;

    {
        mResponse = new StringBuilder();
    }

    HatcHttpResponseHandler(final HatcHttpRequestListener clientHandlerListener) {
        mClientHandlerListener = clientHandlerListener;
    }


    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final HttpObject msg) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            mResponseStatus = response.getStatus();
            mHttpHeaders = response.headers();
            Log.d(TAG, "Got status as " + response.getStatus() + " and headers as " + mHttpHeaders.toString());
        }
        if (msg instanceof HttpContent) {
            HttpContent chunk = (HttpContent) msg;
            mResponse.append(chunk.content().toString(CharsetUtil.UTF_8));
            Log.d(TAG, chunk.toString());
            if (chunk instanceof LastHttpContent) {
                if (readingChunks) {
                    Log.d(TAG, "End of content");
                } else {
                    Log.d(TAG, "End of content");
                }
                Log.d(TAG, "Got response as:" + mResponse.toString());
                readingChunks = false;
                mClientHandlerListener.onComplete(mResponseStatus, mHttpHeaders, mResponse.toString());
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