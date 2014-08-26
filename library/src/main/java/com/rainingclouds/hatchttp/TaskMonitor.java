package com.rainingclouds.hatchttp;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A class to monitor all tasks
 * User: akshay
 * Date: 1/25/13
 * Time: 4:02 PM
 */
public final class TaskMonitor {
    /**
     * TAG for logging
     */
    private static final String TAG = "###TaskMonitor###";
    /**
     * Closed
     */
    private AtomicBoolean mIsClosed = new AtomicBoolean(false);
    /**
     * List of tasks that are currently getting executed
     */
    private ConcurrentLinkedQueue<HatcHttpTask> mTasksExecuting;

    /**
     * Constructor
     */
    public TaskMonitor() {
        mTasksExecuting = new ConcurrentLinkedQueue<HatcHttpTask>();
    }

    /**
     * Adds a task to the task monitor
     *
     * @param task task to be added
     */
    void add(final HatcHttpTask task) {
        mTasksExecuting.add(task);
    }

    /**
     * Removes a task from task monitor
     *
     * @param task task to be removed
     */
    void remove(final HatcHttpTask task) {
        mTasksExecuting.remove(task);
    }


    /**
     * To cancel all the ongoing tasks under the task monitor
     */
    public void cancelRunningTasks() {
        mIsClosed.set(true);
        for (HatcHttpTask task : mTasksExecuting) {
            task.cancel();
        }
    }


}
