package com.github.jameskirk.armadcostringframework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * !!! ATTENTION !!!
 * Please run it with VM arguments: -Xms2g -Xmx8g -Xss8m
 * !!! ATTENTION !!!
 * Simple Test for class ArmadcoString
 */
public class ArmadcoSimpleStringTest {

    static ExecutorService executorService = Executors.newFixedThreadPool(ArmadcoString.THREAD_NUMBER);

    /**
     * @param args
     */
    public static void main(String[] args) {
        int stringLength = 2147483639 / 2;
        System.out.println("creating string with size = " + stringLength / (1024 * 1024) + " MB");
        final String str = ArmadcoString.createStringDataSize(stringLength);
        ArmadcoString armadcoString = new ArmadcoString(executorService);

        // 1. one thread
        //armadcoString.runMultiThreads(str, 1L);

        // 2. multi threads
        armadcoString.runMultiThreads(str, ArmadcoString.THREAD_NUMBER);
    }
}
