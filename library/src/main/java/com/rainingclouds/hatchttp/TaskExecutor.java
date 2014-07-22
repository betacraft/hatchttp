package com.rainingclouds.hatchttp;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Task executor executes submitted tasks
 * User: akshay
 */
public final class TaskExecutor {
    /**
     * TAG for logging
     */
    private static final String TAG = "###TaskExecutor###";
    /**
     * Executor service
     */
    private ExecutorService mExecutorService;
    /**
     * Instance
     */
    private static TaskExecutor mInstance;


    private TaskExecutor() {
        mExecutorService = Executors.newFixedThreadPool(5);

    }

    public static TaskExecutor getInstance() {
        if (mInstance == null)
            mInstance = new TaskExecutor();
        return mInstance;
    }


    public Future submitTask(final Callable task) {
        if (mExecutorService != null)
            return mExecutorService.submit(task);
        return null;
    }

    public void shutDown() {
        if (mExecutorService != null)
            mExecutorService.shutdownNow();
    }
}
