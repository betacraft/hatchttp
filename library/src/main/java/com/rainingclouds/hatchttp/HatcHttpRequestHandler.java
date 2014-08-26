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
 * Created by akshay on 26/08/14.
 */
class HatcHttpRequestHandler extends SimpleChannelInboundHandler<HttpObject>{

    private static final String TAG = "###HatcHttpRequestHandler";
    private boolean readingChunks;
    private HatcHttpTask.HatcHttpRequestListener mClientHandlerListener;
    private HttpResponseStatus mResponseStatus;
    private HttpHeaders mHttpHeaders;
    private StringBuilder mResponse;

    {
        mResponse = new StringBuilder();
    }

    HatcHttpRequestHandler(final HatcHttpTask.HatcHttpRequestListener clientHandlerListener){
        mClientHandlerListener = clientHandlerListener;
    }


    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final HttpObject msg) throws Exception {
            if (msg instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) msg;
                mResponseStatus = response.getStatus();
                mHttpHeaders = response.headers();
            }
            if (msg instanceof HttpContent) {
                HttpContent chunk = (HttpContent) msg;
                mResponse.append(chunk.content().toString(CharsetUtil.UTF_8));
                if (chunk instanceof LastHttpContent) {
                    if (readingChunks) {
                        Log.d(TAG,"End of content");
                    } else {
                        Log.d(TAG,"End of content");
                    }
                    readingChunks = false;
                    mClientHandlerListener.onComplete(mResponseStatus,mHttpHeaders,mResponse.toString());
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
