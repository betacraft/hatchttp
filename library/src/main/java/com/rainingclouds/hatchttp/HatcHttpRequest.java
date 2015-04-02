package com.rainingclouds.hatchttp;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    private int mMethod;
    private String mUrl;
    private String mBody;
    private Map<String, String> mHeaders;
    private Map<String, String> mParams;
    private String mAuth;

    private HatcHttpRequest(final String url, final int method) {
        mMethod = method;
        mUrl = url;
        mHeaders = new HashMap<>();
        mParams = new HashMap<>();
    }


    /**
     * GET request
     *
     * @param url url for the request
     * @return current instance of @HatcHttpRequest
     */
    public static HatcHttpRequest GET(final String url) {
        return new HatcHttpRequest(url, Request.Method.GET);
    }

    /**
     * POST request
     *
     * @param url url for the request
     * @return current instance of @HatcHttpRequest
     */
    public static HatcHttpRequest POST(final String url) {
        return new HatcHttpRequest(url, Request.Method.POST);
    }

    /**
     * PUT request
     *
     * @param url url for the request
     * @return current instance of @HatcHttpRequest
     */
    public static HatcHttpRequest PUT(final String url) {
        return new HatcHttpRequest(url,
                Request.Method.PUT);
    }

    /**
     * DELETE request
     *
     * @param url url for the request
     * @return current instance of @HatcHttpRequest
     */
    public static HatcHttpRequest DELETE(final String url) {
        return new HatcHttpRequest(url, Request.Method.DELETE);
    }

    /**
     * PATCH request
     *
     * @param url url for the request
     * @return current instance of @HatcHttpRequest
     */
    public static HatcHttpRequest PATCH(final String url) {
        return new HatcHttpRequest(url, Request.Method.PATCH);
    }

    /**
     * To add header in your request
     *
     * @param name  name of the header
     * @param value value of the header
     * @return current instance of @HatcHttpRequest
     */
    public HatcHttpRequest addHeader(final String name, final String value) {
        mHeaders.put(name, value);
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
        mParams.put(name, value);
        return this;
    }


    public HatcHttpRequest addAuth(final String userName, final String password) {
        final String creds = String.format("%s:%s", userName, password);
        mAuth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        return this;
    }

    /**
     * To set content of the current request
     *
     * @param content content
     * @return current instance of @HatcHttpRequest
     */
    public HatcHttpRequest setContent(final String content) {
        mBody = content;
        return this;
    }


    /**
     * To execute the request
     *
     * @param hatcHttpRequestListener listener for this request
     */
    public void execute(final HatcHttpRequestListener hatcHttpRequestListener) {
        final Uri.Builder builder = Uri.parse(mUrl).buildUpon();
        for (Map.Entry<String, String> param : mParams.entrySet()) {
            builder.appendQueryParameter(param.getKey(), param.getValue());
        }
        mUrl = builder.build().toString();
        Log.d(TAG, "Calling =>" + mUrl);
        final Request<String> request = new StringRequest(mMethod, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, mUrl + ":" + response);
                hatcHttpRequestListener.onComplete(200, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, mUrl + ":" + error.getMessage(), error);
                hatcHttpRequestListener.onException(new Throwable(new String(error.networkResponse.data)));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (mAuth == null)
                    return mHeaders;
                mHeaders.put("Authorization", mAuth);
                return mHeaders;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return mParams;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                if (mBody == null)
                    return null;
                return mBody.getBytes();
            }
        };
        HatcHttpClient.Get().addRequest(request);
    }

    public void execute(final HatcHttpJSONListener hatcHttpJSONListener) {
        final Uri.Builder builder = Uri.parse(mUrl).buildUpon();
        for (Map.Entry<String, String> param : mParams.entrySet()) {
            builder.appendQueryParameter(param.getKey(), param.getValue());
        }
        mUrl = builder.build().toString();
        Log.d(TAG, "Calling =>" + mUrl);

        final Request<String> request = new StringRequest(mMethod, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Log.d(TAG, mUrl + ":" + response);
                JSONObject responseObject = null;
                try {
                    responseObject = new JSONObject(response);
                } catch (JSONException e) {
                    hatcHttpJSONListener.onException(e);
                    return;
                }
                hatcHttpJSONListener.onComplete(200, responseObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, mUrl + ":" + error.getMessage(), error);
                hatcHttpJSONListener.onException(new Throwable(new String(error.networkResponse.data)));
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (mAuth == null)
                    return mHeaders;
                mHeaders.put("Authorization", mAuth);
                return mHeaders;

            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return mParams;
            }


            @Override
            public byte[] getBody() throws AuthFailureError {
                if (mBody == null)
                    return null;
                return mBody.getBytes();
            }
        };
        HatcHttpClient.Get().addRequest(request);
    }
}
