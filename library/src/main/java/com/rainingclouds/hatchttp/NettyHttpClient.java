package com.rainingclouds.hatchttp;

import java.net.URI;
import java.net.URISyntaxException;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericProgressiveFutureListener;
import io.netty.util.concurrent.ProgressiveFuture;

/**
 * Created by akshay on 22/08/14.
 */
class NettyHttpClient {

    private static final String TAG = "###NettyHttpClient###";
    private Channel mChannel;


    interface NettyHttpClientListener{
        void connectionFailed(final Throwable throwable);
        void connectionSuccess();
    }

    private NettyHttpClient(final String url, final HatcHttpCallerOptions options,
                            final NettyHttpClientListener nettyHttpClientListener) throws URISyntaxException {
        final URI uri = new URI(url);
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
        //TODO add a listener here
        final ChannelFuture channelFuture = options.getBootstrap().connect(host, port);
        channelFuture.addListener(new GenericProgressiveFutureListener<ProgressiveFuture<Object>>() {
            @Override
            public void operationProgressed(final ProgressiveFuture future, final long progress,
                                            final long total) throws Exception {

            }

            @Override
            public void operationComplete(final ProgressiveFuture future) throws Exception {
                if(future.isSuccess()) {
                    nettyHttpClientListener.connectionSuccess();
                    return;
                }
                nettyHttpClientListener.connectionFailed(future.cause());
            }
        });
        mChannel = channelFuture.channel();
    }

    /**
     * Factory method
     * @param url
     * @param options
     * @return
     * @throws URISyntaxException
     */
    static NettyHttpClient getFor(final String url, final HatcHttpCallerOptions options,
                                  final NettyHttpClientListener listener) throws URISyntaxException {
        return new NettyHttpClient(url, options, listener);
    }


    void close(){
        mChannel.close();
    }

}
