package com.github.jameskirk.armadcostringframework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Tests for class BigString
 */
public class BigStringTest {
    private static final int THREAD_NUMBER = 16;
    public static final int STRING_MEGABYTES = 25400;
    static ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUMBER);

    public static void main(String[] args) {
        long stringLength = (long) STRING_MEGABYTES * (1024 * 1024);
        System.out.println("creating string with size = " + stringLength / (1024 * 1024) + " MB");
        BigString bigString = new BigString(stringLength);
        ArmadcoString.runWithPerformance(() -> {

            bigString.initRandomLatin(THREAD_NUMBER, executorService);

        });
        executorService.shutdown();
        System.out.println("string was successfully created");
    }


}
