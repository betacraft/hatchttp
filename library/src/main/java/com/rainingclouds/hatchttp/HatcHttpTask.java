package com.rainingclouds.hatchttp;

import android.content.Context;
import android.os.Handler;
import com.rainingclouds.hatchttp.exception.HatcHttpErrorCode;
import com.rainingclouds.hatchttp.exception.HatcHttpException;
import com.rainingclouds.hatchttp.utils.DataConnectionUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class to perform a prepareRequest asynchronously in separate thread
 *
 * @param <T>
 */

public abstract class HatcHttpTask<T> {
    /**
     * Logger
     */
    private static final String TAG = "###Task###";
    /**
     * Event listeners associated with this task
     */
    private TaskEventListener<T> mTaskEventListener;
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
    public HatcHttpTask(final Context context, final TaskMonitor taskMonitor) {
        //Log.d(TAG, "executing");
        mContext = context;
        mTaskMonitor = taskMonitor;
        if (mTaskMonitor != null)
            mTaskMonitor.add(this);
        mExecutionRoutine = new Callable<T>() {
            @Override
            public T call() throws Exception {
                try {
                    final T resp = task();
                    if (!mIsCancelled.get()) {
                        dispatchTaskExecutionCompleteEvent(resp);
                    }
                    return resp;
                } catch (final HatcHttpException e) {
                    e.printStackTrace();
                    if (!mIsCancelled.get()) {
                        dispatchTaskExceptionEvent(e);
                    }
                }
                return null;
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
        if (!mIsCancelled.get())
            mTaskEventListener.onTaskExceptionEvent(exception);
    }

    /**
     * Callback method to be called on successful execution of the task
     *
     * @param resp response for the task
     */
    protected void dispatchTaskExecutionCompleteEvent(T resp) {
        if (mTaskMonitor != null)
            mTaskMonitor.remove(this);
        if (!mIsCancelled.get())
            mTaskEventListener.onTaskExecutionComplete(resp);
    }


    /**
     * Execute the given task
     */
    public Future<T> execute() {

        if (!DataConnectionUtils.dataConnectivityAvailable(mContext)) {
            dispatchTaskExceptionEvent(new HatcHttpException(HatcHttpErrorCode.NO_DATA_CONNECTION));
            return null;
        }
        return TaskExecutor.getInstance().submitTask(mExecutionRoutine);
    }


    /**
     * Execute the given task
     */
    public Future<T> execute(final TaskEventListener<T> listener) {

        mTaskEventListener = listener;
        if (!DataConnectionUtils.dataConnectivityAvailable(mContext)) {
            dispatchTaskExceptionEvent(new HatcHttpException(HatcHttpErrorCode.NO_DATA_CONNECTION));
            return null;
        }
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