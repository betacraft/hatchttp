package com.rainingclouds.hatchttp;

import java.net.URI;
import java.net.URISyntaxException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpRequest;

/**
 * NettyHttp Client
 * Created by akshay on 22/08/14.
 */
class NettyHttpClient {

    private static final String TAG = "###NettyHttpClient###";
    private Channel mChannel;


    interface NettyHttpClientListener{
        void connectionFailed(final Throwable throwable);
        void connectionSuccess();
    }

    private NettyHttpClient(final HatcHttpRequest hatcHttpRequest){
        final URI uri;
        try {
            uri = new URI(hatcHttpRequest.getUrl());
        }catch (URISyntaxException ex){
            throw new IllegalStateException("URL passed is illegal",ex);
        }
        final String scheme = uri.getScheme() == null? "http" : uri.getScheme();
        final String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        int port = uri.getPort();
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
                .group(hatcHttpRequest.getOptions().getWorker())
                .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(final SocketChannel ch) throws Exception {
                            final ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpClientCodec());
                            pipeline.addLast(new HttpContentDecompressor());
                            pipeline.addLast(hatcHttpRequest.getRequestHandler());
                        }
                });

        final ChannelFuture channelFuture = bootstrap.connect(host, port).syncUninterruptibly();
        mChannel = channelFuture.channel();
    }

    ChannelFuture writeRequest(final HttpRequest request){
        ChannelFuture future = mChannel.write(request);
        mChannel.flush();
        return future;
    }


    /**
     * Factory method
     * @param hatcHttpRequest
     * @return
     * @throws URISyntaxException
     */
    static NettyHttpClient getFor(final HatcHttpRequest hatcHttpRequest){
        return new NettyHttpClient(hatcHttpRequest);
    }


    void close(){
        mChannel.close();
    }

}
