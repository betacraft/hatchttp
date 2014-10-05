package com.rainingclouds.hatchttp;

import com.rainingclouds.hatchttp.executors.EfficientThreadPoolExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Executors of HatcHttp Library
 * Created by akshay on 05/10/14.
 */
public class HatcHttpExecutor {

    private static ExecutorService executorService = EfficientThreadPoolExecutor.get(2, 5, 1, TimeUnit.MINUTES, 20,
            "###HatcHttpWorker###");

    public static void Submit(final Runnable task) {
        executorService.execute(task);
    }
}
