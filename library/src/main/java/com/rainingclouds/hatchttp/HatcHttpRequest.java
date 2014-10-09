package com.rainingclouds.hatchttp;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;

import org.json.JSONObject;

import java.io.IOException;

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
    private RequestBuilder mRequestBuilder;


    private HatcHttpRequest(final String url, final String method) {
        mRequestBuilder = new RequestBuilder()
                .setUrl(url)
                .setMethod(method);
    }

    /**
     * GET request
     *
     * @param url url for the request
     * @return current instance of @HatcHttpRequest
     */
    public static HatcHttpRequest GET(final String url) {
        return new HatcHttpRequest(url, "GET");
    }

    /**
     * POST request
     *
     * @param url url for the request
     * @return current instance of @HatcHttpRequest
     */
    public static HatcHttpRequest POST(final String url) {
        return new HatcHttpRequest(url, "POST");
    }

    /**
     * PUT request
     *
     * @param url url for the request
     * @return current instance of @HatcHttpRequest
     */
    public static HatcHttpRequest PUT(final String url) {
        return new HatcHttpRequest(url, "PUT");
    }

    /**
     * DELETE request
     *
     * @param url url for the request
     * @return current instance of @HatcHttpRequest
     */
    public static HatcHttpRequest DELETE(final String url) {
        return new HatcHttpRequest(url, "DELETE");
    }

    /**
     * PATCH request
     *
     * @param url url for the request
     * @return current instance of @HatcHttpRequest
     */
    public static HatcHttpRequest PATCH(final String url) {
        return new HatcHttpRequest(url, "PATCH");
    }

    /**
     * To add header in your request
     *
     * @param name  name of the header
     * @param value value of the header
     * @return current instance of @HatcHttpRequest
     */
    public HatcHttpRequest addHeader(final String name, final String value) {
        mRequestBuilder.addHeader(name, value);
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
        mRequestBuilder.addParameter(name, value);
        return this;
    }


    /**
     * To set content of the current request
     *
     * @param content content
     * @return current instance of @HatcHttpRequest
     */
    public HatcHttpRequest setContent(final String content) {
        mRequestBuilder.setBody(content);
        return this;
    }


    /**
     * To execute the request
     *
     * @param hatcHttpRequestListener listener for this request
     */
    public void execute(final HatcHttpRequestListener hatcHttpRequestListener) {
        HatcHttpExecutor.Submit(new Runnable() {
            @Override
            public void run() {
                try {
                    HatcHttpClient.get().executeRequest(mRequestBuilder.build(), new AsyncCompletionHandler<Object>() {
                        @Override
                        public Object onCompleted(final Response response) throws Exception {
                            hatcHttpRequestListener.onComplete(response.getStatusCode(), response.getResponseBody());
                            return null;
                        }

                        @Override
                        public void onThrowable(final Throwable t) {
                            super.onThrowable(t);
                            hatcHttpRequestListener.onException(t);
                        }
                    });
                } catch (IOException exception) {
                    hatcHttpRequestListener.onException(exception);
                }
            }
        });
    }

    public void execute(final HatcHttpJSONListener hatcHttpJSONListener) {
        HatcHttpExecutor.Submit(new Runnable() {
            @Override
            public void run() {
                try {
                    HatcHttpClient.get().executeRequest(mRequestBuilder.build(), new AsyncCompletionHandler<Object>() {
                        @Override
                        public Object onCompleted(final Response response) throws Exception {
                            hatcHttpJSONListener.onComplete(response.getStatusCode(), new JSONObject(response.getResponseBody()));
                            return null;
                        }

                        @Override
                        public void onThrowable(final Throwable t) {
                            super.onThrowable(t);
                            hatcHttpJSONListener.onException(t);
                        }
                    });
                } catch (IOException exception) {
                    hatcHttpJSONListener.onException(exception);
                }
            }
        });
    }
}
