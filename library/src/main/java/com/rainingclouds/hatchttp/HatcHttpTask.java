package com.rainingclouds.hatchttp;

import android.content.Context;

import com.rainingclouds.hatchttp.exception.HatcHttpErrorCode;
import com.rainingclouds.hatchttp.exception.HatcHttpException;
import com.rainingclouds.hatchttp.utils.DataConnectionUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;


/**
 * Created by akshay on 26/08/14.
 */
public abstract class HatcHttpTask<T> {

    private static final String TAG = "###HatcHttpTask###";
    private TaskEventListener mTaskEventListener;
    protected Context mContext;
    protected TaskMonitor mTaskMonitor;
    private AtomicBoolean mIsCancelled = new AtomicBoolean(false);
    private volatile Callable<Void> mExecutionRoutine;

    public interface HatcHttpRequestListener {
        void onComplete(final HttpResponseStatus status, final HttpHeaders headers,final String response);
        void onException(final Throwable throwable);
    }

    public abstract HatcHttpRequest getRequest() throws Exception;

    public void task(final HatcHttpRequestListener clientHandlerListener) throws HatcHttpException{
        try {
            getRequest().execute(clientHandlerListener);
        }catch (Exception e){
            throw new HatcHttpException(HatcHttpErrorCode.REQUEST_PREPARATION_EXCEPTION,e);
        }
    }


    public abstract T gotResponse(final String response) throws Exception;

    public HatcHttpTask(final Context context, final TaskMonitor taskMonitor){
        mContext = context;
        mTaskMonitor = taskMonitor;
        if(mTaskMonitor != null)
            mTaskMonitor.add(this);

        mExecutionRoutine = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    task(new HatcHttpRequestListener() {
                        @Override
                        public void onComplete(final HttpResponseStatus status, final HttpHeaders headers,
                                               final String response) {
                            try {
                                dispatchTaskExecutionCompleteEvent(gotResponse(response));
                            }catch (final Exception e){
                                if(!mIsCancelled.get()){
                                    dispatchTaskExceptionEvent(new HatcHttpException(HatcHttpErrorCode.UNKNOWN,e));
                                }
                            }
                        }

                        @Override
                        public void onException(final Throwable throwable) {
                            dispatchTaskExceptionEvent(new HatcHttpException(HatcHttpErrorCode
                                    .CLIENT_PROTOCOL_EXCEPTION,throwable));
                        }
                    });
                } catch (final HatcHttpException e) {

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
