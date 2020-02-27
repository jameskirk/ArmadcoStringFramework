package com.github.jameskirk.armadcostringframework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * !!! ATTENTION !!!
 * Please run it with VM arguments: -Xms2g -Xmx8g -Xss8m
 * !!! ATTENTION !!!
 * Test for class ArmadcoString
 */
public class ArmadcoStringTest {
    /**
     * string in MB
     */
    public static final int STRING_MEGABYTES = 25400;

    private static ExecutorService executorService = Executors.newFixedThreadPool(ArmadcoString.THREAD_NUMBER);

    /**
     * @param args
     */
    public static void main(String[] args) {
        long stringLength = (long) STRING_MEGABYTES * (1024 * 1024);
        System.out.println("creating string with size = " + stringLength / (1024 * 1024) + " MB");
        ArmadcoString armadcoString = new ArmadcoString(executorService);

        // 3. multip threads BigString
        final BigString bigString = new BigString(stringLength);
        ArmadcoString.runWithPerformance(() -> {
            bigString.initRandomLatin(ArmadcoString.THREAD_NUMBER, executorService);
        });
        armadcoString.runMultiThreads(bigString, ArmadcoString.THREAD_NUMBER);
    }

}
