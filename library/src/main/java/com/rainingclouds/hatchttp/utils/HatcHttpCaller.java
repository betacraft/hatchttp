package com.rainingclouds.hatchttp.utils;

import android.util.Log;
import com.rainingclouds.hatchttp.exception.HatcHttpErrorCode;
import com.rainingclouds.hatchttp.exception.HatcHttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * A static to help to use different REST APIs
 *
 * @author Akshay Deo
 * @version 0.2
 * @since 0.1
 */
public class HatcHttpCaller {

    /**
     * Debug tag to be used in Log
     */
    private static final String TAG = "###HatcHttpCalled###";
    /**
     * Connection timeout time is 30 seconds
     */
    private static int CONNECTION_TIMEOUT_TIME = 30000;
    /**
     * Max connection retries
     */
    private static int MAX_CONNECTION_RETRIES = 3;


    /**
     * Factory methods
     * @return
     */
    public static HatcHttpAsyncCaller getInstance(){
        return new HatcHttpAsyncCaller();
    }

    /**
     * Setter for connection retries attempts
     * @param maxRetries
     */
    public void setMaxConnectionRetries(final int maxRetries){
        MAX_CONNECTION_RETRIES = maxRetries;
    }

    /**
     * Setter for connection timeout
     * @param timeout
     */
    public void setConnectionTimeoutTime(final int timeout){
        CONNECTION_TIMEOUT_TIME = timeout;
    }

    /**
     * Execute request method
     *
     * @param httpRequest
     * @return
     * @throws com.rainingclouds.hatchttp.exception.HatcHttpException
     */
    private HttpResponse executeRequest(final org.apache.http.client.methods.HttpRequestBase httpRequest)
            throws HatcHttpException {
        Exception underlyingException;
        int retryCount = 0;
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT_TIME);
        HttpConnectionParams.setSoTimeout(httpParameters, CONNECTION_TIMEOUT_TIME);
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpResponse response;
        do {
            Log.d(TAG, "Making call " + retryCount);
            try {
                response = httpClient.execute(httpRequest);
                return response;
            } catch (ClientProtocolException e) {
                Log.e(TAG, "Exception happened while contacting to server", e);
                underlyingException = e;
            } catch (IOException e) {
                Log.e(TAG, "Exception happened while contacting to server", e);
                underlyingException = e;
            } catch (Exception e){
                Log.e(TAG, "Exception happened while connecting to the server",e);
                underlyingException =e ;
            }
        } while (++retryCount < MAX_CONNECTION_RETRIES);
        throw new HatcHttpException(HatcHttpErrorCode.MAX_RETRIES_FOR_CONTACTING_SERVER,underlyingException);
    }

    /**
     * Method to send a post request to the passed URL
     *
     * @param url    url to which post request has to be made
     * @param params params
     * @return response given by server
     * @throws com.rainingclouds.hatchttp.exception.HatcHttpException
     */
    public String sendPostRequest(final String url,
                                         final List<BasicNameValuePair> headers,
                                         List<BasicNameValuePair> params)
            throws HatcHttpException {
        // Create http post request
        HttpPost httpPost = new HttpPost(url);
        StatusLine status;
        try {
            if (params != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            }
            if (headers != null) {
                for (BasicNameValuePair header : headers)
                    httpPost.setHeader(header.getName(), header.getValue());
            }
            HttpResponse response = executeRequest(httpPost);
            status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK)
                return EntityUtils.toString(response.getEntity());
            else if (status.getStatusCode() == HttpStatus.SC_REQUEST_TIMEOUT) {
                throw new HatcHttpException(HatcHttpErrorCode.REQUEST_TIMEOUT);
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error while executing post request", e);
            throw new HatcHttpException(HatcHttpErrorCode.UNSUPPORTED_ENCODING, e);
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Error while executing post request", e);
            throw new HatcHttpException(HatcHttpErrorCode.CLIENT_PROTOCOL_EXCEPTION, e);
        } catch (IOException e) {
            Log.e(TAG, "Error while executing post request", e);
            throw new HatcHttpException(HatcHttpErrorCode.SOCKET_EXCEPTION, e);
        }
        throw new HatcHttpException(status.getStatusCode(),status.getReasonPhrase());
    }

    /**
     * Method to send a put request to the passed URL
     *
     * @param url    url to which post request has to be made
     * @param params params
     * @return response given by server
     * @throws com.rainingclouds.hatchttp.exception.HatcHttpException
     */
    public String sendPutRequest(final String url,
                                        final List<BasicNameValuePair> headers,
                                        List<BasicNameValuePair> params)
            throws HatcHttpException {
        // Create http post request
        HttpPost httpPost = new HttpPost(url);
        StatusLine status;
        if (params != null) {
            params.add(new BasicNameValuePair("_method", "put"));
        } else {
            params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("_method", "put"));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            if (headers != null) {
                for (BasicNameValuePair header : headers)
                    httpPost.setHeader(header.getName(), header.getValue());
            }
            //Log.d(TAG, httpPost.getURI().toString());
            long callStartTime = System.currentTimeMillis();
            HttpResponse response = executeRequest(httpPost);

            status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK)
                return EntityUtils.toString(response.getEntity());
            else if (status.getStatusCode() == HttpStatus.SC_REQUEST_TIMEOUT) {
                throw new HatcHttpException(HatcHttpErrorCode.REQUEST_TIMEOUT);
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error while executing put request", e);
            throw new HatcHttpException(HatcHttpErrorCode.UNSUPPORTED_ENCODING, e);
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Error while executing put request", e);
            throw new HatcHttpException(HatcHttpErrorCode.CLIENT_PROTOCOL_EXCEPTION, e);
        } catch (IOException e) {
            Log.e(TAG, "Error while executing put request", e);
            throw new HatcHttpException(HatcHttpErrorCode.SOCKET_EXCEPTION, e);
        }
        throw new HatcHttpException(status.getStatusCode(),status.getReasonPhrase());
    }

    /**
     * Method to send a Delete request to the passed URL
     *
     * @param url    url to which post request has to be made
     * @param params params
     * @return response given by server
     * @throws com.rainingclouds.hatchttp.exception.HatcHttpException
     */
    public String sendDeleteRequest(final String url,
                                           final List<BasicNameValuePair> headers,
                                           List<BasicNameValuePair> params)
            throws HatcHttpException {
        // Create http post request
        HttpPost httpPost = new HttpPost(url);
        StatusLine status;
        if (params != null) {
            params.add(new BasicNameValuePair("_method", "delete"));
        } else {
            params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("_method", "delete"));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            if (headers != null) {
                for (BasicNameValuePair header : headers)
                    httpPost.setHeader(header.getName(), header.getValue());
            }
            //Log.d(TAG, httpPost.getURI().toString());
            long callStartTime = System.currentTimeMillis();
            HttpResponse response = executeRequest(httpPost);
            status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK)
                return EntityUtils.toString(response.getEntity());
            else if (status.getStatusCode() == HttpStatus.SC_REQUEST_TIMEOUT) {
                throw new HatcHttpException(HatcHttpErrorCode.REQUEST_TIMEOUT);
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error while executing delete request", e);
            throw new HatcHttpException(HatcHttpErrorCode.UNSUPPORTED_ENCODING, e);
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Error while executing delete request", e);
            throw new HatcHttpException(HatcHttpErrorCode.CLIENT_PROTOCOL_EXCEPTION, e);
        } catch (IOException e) {
            Log.e(TAG, "Error while executing delete request", e);
            throw new HatcHttpException(HatcHttpErrorCode.SOCKET_EXCEPTION, e);
        }
        throw new HatcHttpException(status.getStatusCode(),status.getReasonPhrase());
    }


    /**
     * Method to send a put request to the passed URL
     *
     * @param url url to which post request has to be made
     * @return response given by server
     * @throws com.rainingclouds.hatchttp.exception.HatcHttpException
     */
    public String sendPutRequest(final String url,
                                        List<BasicNameValuePair> headers,
                                        JSONObject json)
            throws HatcHttpException {
        // Create http post request
        HttpPut httpPut = new HttpPut(url);

        StatusLine status;
        if (headers == null) {
            headers = new ArrayList<BasicNameValuePair>();
        }
        headers.add(new BasicNameValuePair("Accept", "application/json"));
        headers.add(new BasicNameValuePair("Content-type", "application/json"));
        try {
            StringEntity jsonEntity = new StringEntity(json.toString());
            jsonEntity.setContentType("application/json");
            httpPut.setEntity(jsonEntity);
            for (BasicNameValuePair header : headers)
                httpPut.setHeader(header.getName(), header.getValue());
            Log.d(TAG, "Sending JSON " + json);
            //Log.d(TAG, httpPut.getURI().toString());
            HttpResponse response = executeRequest(httpPut);
            status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK)
                return EntityUtils.toString(response.getEntity());
            else if (status.getStatusCode() == HttpStatus.SC_REQUEST_TIMEOUT) {
                throw new HatcHttpException(HatcHttpErrorCode.REQUEST_TIMEOUT);
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error while executing put request", e);
            throw new HatcHttpException(HatcHttpErrorCode.UNSUPPORTED_ENCODING, e);
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Error while executing put request", e);
            throw new HatcHttpException(HatcHttpErrorCode.CLIENT_PROTOCOL_EXCEPTION, e);
        } catch (IOException e) {
            Log.e(TAG, "Error while executing put request", e);
            throw new HatcHttpException(HatcHttpErrorCode.SOCKET_EXCEPTION, e);
        }
        throw new HatcHttpException(status.getStatusCode(),status.getReasonPhrase());
    }


    /**
     * Method to send a post request to the passed URL
     *
     * @param url url to which post request has to be made
     * @return response given by server
     * @throws com.rainingclouds.hatchttp.exception.HatcHttpException
     */
    public String sendJSONPostRequest(final String url, List<BasicNameValuePair> headers, final JSONObject json)
            throws HatcHttpException {
        // Create http post request
        HttpPost httpPost = new HttpPost(url);
        StatusLine status;
        if (headers == null) {
            headers = new ArrayList<BasicNameValuePair>();
        }
        headers.add(new BasicNameValuePair("Content-Type", "application/json"));
        try {
            for (BasicNameValuePair header : headers)
                httpPost.setHeader(header.getName(), header.getValue());
            httpPost.setEntity(new StringEntity(json.toString()));
            Log.d(TAG, httpPost.getURI().toString());
            HttpResponse response = executeRequest(httpPost);
            status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK)
                return EntityUtils.toString(response.getEntity());
            else if (status.getStatusCode() == HttpStatus.SC_REQUEST_TIMEOUT) {
                throw new HatcHttpException(HatcHttpErrorCode.REQUEST_TIMEOUT);
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error while executing post request", e);
            throw new HatcHttpException(HatcHttpErrorCode.UNSUPPORTED_ENCODING, e);
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Error while executing post request", e);
            throw new HatcHttpException(HatcHttpErrorCode.CLIENT_PROTOCOL_EXCEPTION, e);
        } catch (IOException e) {
            Log.e(TAG, "Error while executing post request", e);
            throw new HatcHttpException(HatcHttpErrorCode.SOCKET_EXCEPTION, e);
        }
        throw new HatcHttpException(status.getStatusCode(),status.getReasonPhrase());
    }


    /**
     * Method to send get request to the given URL with given headers and
     * parameters
     *
     * @param url     url to which get request has to be made
     * @param headers headers to be sent
     * @param params  params to be sent
     * @return response given by server
     * @throws com.rainingclouds.hatchttp.exception.HatcHttpException
     */
    public String sendGetRequest(final String url,
                                        final List<BasicNameValuePair> headers,
                                        List<BasicNameValuePair> params)
            throws HatcHttpException {

        // Prepare http get request
        HttpGet httpGet;
        // Add parameters to http get request
        if (params != null) {
            String urlParameters = URLEncodedUtils.format(params, "utf-8");
            httpGet = new HttpGet(url + "?" + urlParameters);
        } else
            httpGet = new HttpGet(url);
        StatusLine status;
        // Add all headers
        if (headers != null)
            for (BasicNameValuePair header : headers)
                httpGet.addHeader(header.getName(), header.getValue());
        Log.d(TAG, "Sending get request " + httpGet.getURI().toString());
        try {
            long callStartTime = System.currentTimeMillis();
            HttpResponse response = executeRequest(httpGet);
            status = response.getStatusLine();
            //Log.d(TAG, "" + status.getReasonPhrase());
            if (status.getStatusCode() == HttpStatus.SC_OK) {
                //Log.d(TAG, "Got status ok");
                return EntityUtils.toString(response.getEntity());
            } else if (status.getStatusCode() == HttpStatus.SC_REQUEST_TIMEOUT) {
                throw new HatcHttpException(HatcHttpErrorCode.REQUEST_TIMEOUT);
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error while executing get request", e);
            throw new HatcHttpException(HatcHttpErrorCode.UNSUPPORTED_ENCODING, e);
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Error while executing get request", e);
            throw new HatcHttpException(HatcHttpErrorCode.CLIENT_PROTOCOL_EXCEPTION, e);
        } catch (IOException e) {
            Log.e(TAG, "Error while executing get request", e);
            throw new HatcHttpException(HatcHttpErrorCode.SOCKET_EXCEPTION, e);
        }
        throw new HatcHttpException(status.getStatusCode(),status.getReasonPhrase());
    }

}