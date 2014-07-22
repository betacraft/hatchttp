package com.rainingclouds.hatchttp;

import android.content.Context;
import com.rainingclouds.hatchttp.exception.HatcHttpErrorCode;
import com.rainingclouds.hatchttp.exception.HatcHttpException;
import com.rainingclouds.hatchttp.utils.DataConnectionUtils;


import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class to perform a prepareRequest asynchronously in separate thread
 *
 * @param <T>
 */

public abstract class Task<T> {
    /**
     * Logger
     */
    private static final String TAG = "###Task###";
    /**
     * Event listeners associated with this task
     */
    private ArrayList<TaskEventListener<T>> mTaskEventListeners = new ArrayList<TaskEventListener<T>>();

    /**
     * Executor thread
     */
    private volatile Callable<T> mExecutionRoutine;
    /**
     * Status of executor thread
     */
    private AtomicBoolean mIsCancelled = new AtomicBoolean(false);
    /**
     * Task monitor
     */
    private TaskMonitor mTaskMonitor;
    /**
     * Start time of the task
     */
    private long mStartTime;
    /**
     * Context
     */
    protected Context mContext;


    /**
     * Task to be executed under concrete class
     *
     * @return T
     * @throws com.rainingclouds.hatchttp.exception.HatcHttpException
     */
    protected abstract T task() throws HatcHttpException;

    /**
     * Constructor
     *
     * @param context     context in which this task is running
     * @param taskMonitor task monitor allocated for this task see @link{TaskMonitor}
     */
    public Task(final Context context, final TaskMonitor taskMonitor) {
        //Log.d(TAG, "executing");
        mContext = context;
        mTaskMonitor = taskMonitor;
        if (mTaskMonitor != null)
            mTaskMonitor.add(this);
        mExecutionRoutine = new Callable<T>() {
            @Override
            public T call() throws Exception {
                T resp = null;
                try {
                    resp = task();
                    if (!mIsCancelled.get())
                        dispatchTaskExecutionCompleteEvent(resp);
                } catch (HatcHttpException e) {
                    e.printStackTrace();
                    if (!mIsCancelled.get())
                        dispatchTaskExceptionEvent(e);
                }
                return resp;
            }
        };
    }

    /**
     * Callback method to be called when execution of task throws an exception
     *
     * @param exception exception that has occurred
     */
    protected void dispatchTaskExceptionEvent(HatcHttpException exception) {
        if (mTaskMonitor != null)
            mTaskMonitor.remove(this);
        for (TaskEventListener listener : mTaskEventListeners) {
            if (!mIsCancelled.get())
                listener.onTaskExceptionEvent(exception);
        }

    }

    /**
     * Callback method to be called on successful execution of the task
     *
     * @param resp response for the task
     */
    protected void dispatchTaskExecutionCompleteEvent(T resp) {
        if (mTaskMonitor != null)
            mTaskMonitor.remove(this);
        for (TaskEventListener<T> listener : mTaskEventListeners) {
            if (!mIsCancelled.get())
                listener.onTaskExecutionComplete(resp);
        }
    }

    /**
     * Add an event listener for this task
     *
     * @param listener task event listener for this task
     */
    public void setOnTaskEventListener(final TaskEventListener<T> listener) {
        mTaskEventListeners.add(listener);
    }

    /**
     * Execute the given task
     */
    public Future<T> execute() {
        if (!DataConnectionUtils.dataConnectivityAvailable(mContext)) {
            dispatchTaskExceptionEvent(new HatcHttpException(HatcHttpErrorCode.NO_DATA_CONNECTION));
            return null;
        }
        mStartTime = System.currentTimeMillis();
        return TaskExecutor.getInstance().submitTask(mExecutionRoutine);
    }


    /**
     * Execute the given task
     */
    public Future<T> execute(final TaskEventListener<T> listener) {
        mTaskEventListeners.add(listener);
        if (!DataConnectionUtils.dataConnectivityAvailable(mContext)) {
            dispatchTaskExceptionEvent(new HatcHttpException(HatcHttpErrorCode.NO_DATA_CONNECTION));
            return null;
        }
        mStartTime = System.currentTimeMillis();
        return TaskExecutor.getInstance().submitTask(mExecutionRoutine);
    }

    /**
     * Cancel a task in between
     */
    public void cancel() {
        mIsCancelled.set(true);
        //mExecutionRoutine.interrupt();
        mExecutionRoutine = null;
        if (mTaskMonitor != null)
            mTaskMonitor.remove(this);
    }
}