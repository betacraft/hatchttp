package com.rainingclouds.hatchttp;

import com.rainingclouds.hatchttp.exception.HatcHttpException;

import java.util.EventListener;

public interface TaskEventListener<T> extends EventListener {

    public void onTaskExecutionComplete(final T response);

    public void onTaskExceptionEvent(final HatcHttpException exception);

}