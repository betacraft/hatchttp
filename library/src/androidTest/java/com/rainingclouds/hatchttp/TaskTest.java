package com.rainingclouds.hatchttp;

import android.test.AndroidTestCase;
import android.util.Log;
import com.rainingclouds.hatchttp.exception.HatcHttpException;
import com.rainingclouds.hatchttp.utils.HatcHttpCaller;


public class TaskTest extends AndroidTestCase {
    private static final String TAG = "###TaskTest###";
    private TaskMonitor mTaskMonitor;

    public void setUp() throws Exception {
        super.setUp();
        mTaskMonitor = new TaskMonitor();
    }

    public void testGetMethod() throws Exception {
        Task<String> getGoogleHomePageHtml = new Task<String>(getContext(), mTaskMonitor) {
            @Override
            protected String task() throws HatcHttpException {
                return HatcHttpCaller.sendGetRequest("http://google.com", null, null);
            }
        };
        getGoogleHomePageHtml.execute(new TaskEventListener<String>() {
            @Override
            public void onTaskExecutionComplete(final String response) {
                Log.i(TAG, response);

            }

            @Override
            public void onTaskExceptionEvent(final HatcHttpException exception) {
                Log.e(TAG, "Error happened while getting google homepage", exception);

            }
        });

    }

    public void tearDown() throws Exception {
        mTaskMonitor.cancelRunningTasks();
    }
}