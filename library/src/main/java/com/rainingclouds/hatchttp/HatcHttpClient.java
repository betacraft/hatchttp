package com.rainingclouds.hatchttp;

import android.util.Log;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * NettyHttp Client
 * Created by akshay on 22/08/14.
 */
class HatcHttpClient {

    private static final String TAG = "###NettyHttpClient###";
    private Channel mChannel;
    private static final NioEventLoopGroup NIO_EVENT_LOOP_GROUP = new NioEventLoopGroup(1);

    private HatcHttpClient(final HatcHttpRequest hatcHttpRequest) throws InterruptedException {


        final String scheme = hatcHttpRequest.getUri().getScheme() == null ? "http" : hatcHttpRequest.getUri().getScheme();
        final String host = hatcHttpRequest.getUri().getHost() == null ? "127.0.0.1" : hatcHttpRequest.getUri().getHost();
        int port = hatcHttpRequest.getUri().getPort();
        if (port == -1) {
            if ("http".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("https".equalsIgnoreCase(scheme)) {
                port = 443;
            }
        }
        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            throw new IllegalStateException("Only HTTP(S) is supported");
        }


        final Bootstrap bootstrap = new Bootstrap().channel(NioSocketChannel.class)
                .group(NIO_EVENT_LOOP_GROUP)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(final SocketChannel ch) throws Exception {
                        final ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new HttpClientCodec());
                        pipeline.addLast(new HttpContentDecompressor());
                        pipeline.addLast(hatcHttpRequest.getResponseHandler());
                        pipeline.addLast(new LoggingHandler(LogLevel.TRACE));
                    }
                });
        Log.d(TAG, "Connecting to " + host + ":" + port);
        mChannel = bootstrap.connect(host, port).sync().channel();
        Log.d(TAG, "Channel connected");
    }

    void writeRequest(final HttpRequest request) {
        Log.d(TAG, "Writing request " + request.getUri());
        mChannel.writeAndFlush(request);

    }

    static HatcHttpClient getFor(final HatcHttpRequest hatcHttpRequest) throws InterruptedException {
        return new HatcHttpClient(hatcHttpRequest);
    }
}
