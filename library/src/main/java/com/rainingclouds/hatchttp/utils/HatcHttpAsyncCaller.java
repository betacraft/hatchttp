package com.rainingclouds.hatchttp.utils;

import com.rainingclouds.hatchttp.TaskMonitor;
import com.rainingclouds.hatchttp.exception.HatcHttpException;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Async http caller
 * Created by akshay on 15/08/14.
 */
public final class HatcHttpAsyncCaller extends HatcHttpCaller{
    /**
     * TAG for logging
     */
    private static final String TAG = "###HatcHttpAsyncCaller###";
    /**
     * Executor
     */
    private ExecutorService mExecutor = Executors.newFixedThreadPool(4);
    /**
     * Task monitor associated with this called
     */
    private TaskMonitor mTaskMonitor;

    /**
     * Factory method
     * @return
     */
    public static HatcHttpAsyncCaller getInstance(){
        return new HatcHttpAsyncCaller();
    }

    /**
     * Factory methods
     * @return
     */
    public static HatcHttpAsyncCaller getInstance(final TaskMonitor mTaskMonitor) {
        return new HatcHttpAsyncCaller();
    }

    /**
     * Method to execute post request asynchronously
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws HatcHttpException
     */
    public Future<String> sendAsyncPostRequest(final String url, final List<BasicNameValuePair> headers,
                                       final List<BasicNameValuePair> params)
            throws HatcHttpException {
        return mExecutor.submit(new Callable<String>() {
            @Override
            public String call() throws HatcHttpException {
                return sendPostRequest(url,headers,params);
            }
        });
    }


    /**
     * Method to send async put request
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws HatcHttpException
     */
    public Future<String> sendAsyncPutRequest(final String url, final List<BasicNameValuePair> headers,
                                          final List<BasicNameValuePair> params)
            throws HatcHttpException {
        return mExecutor.submit(new Callable<String>() {
            @Override
            public String call() throws HatcHttpException {
                return sendPutRequest(url, headers, params);
            }
        });
    }


    /**
     * Method to send async delete request
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws HatcHttpException
     */
    public Future<String> sendAsyncDeleteRequest(final String url, final List<BasicNameValuePair> headers,
                                     final List<BasicNameValuePair> params) throws HatcHttpException {
        return mExecutor.submit(new Callable<String>() {
            @Override
            public String call() throws HatcHttpException {
                return sendDeleteRequest(url, headers, params);
            }
        });
    }


    /**
     * Method to send asyn put request
     * @param url
     * @param headers
     * @param json
     * @return
     * @throws HatcHttpException
     */
    public Future<String> sendAsyncPutRequest(final String url, final List<BasicNameValuePair> headers,
                                  final JSONObject json) throws HatcHttpException {
        return mExecutor.submit(new Callable<String>() {
            @Override
            public String call() throws HatcHttpException {
                return sendPutRequest(url, headers, json);
            }
        });
    }


    /**
     * Method to send async json post request
     * @param url
     * @param headers
     * @param json
     * @return
     * @throws HatcHttpException
     */
    public Future<String> sendAsyncJSONPostRequest(final String url, final List<BasicNameValuePair> headers,
                                       final JSONObject json) throws HatcHttpException {
        return mExecutor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return sendJSONPostRequest(url, headers, json);
            };
        });
    }

    /**
     * Method to send async get request
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws HatcHttpException
     */
    public Future<String> sendAsyncGetRequest(final String url, final List<BasicNameValuePair> headers,
                                           final List<BasicNameValuePair> params)
            throws HatcHttpException {
        return mExecutor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return sendGetRequest(url, headers, params);
            }
        });
    }
}
