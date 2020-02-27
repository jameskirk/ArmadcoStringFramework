package com.github.jameskirk.armadcostringframework;

import java.util.*;
import java.util.concurrent.*;

/**
 * !!! ATTENTION !!!
 * Please run it with VM arguments: -Xms2g -Xmx8g -Xss8m
 * !!! ATTENTION !!!
 * Class for calculate max length of characters in a row
 *
 * @author golovin
 */
public class ArmadcoString {
    /**
     * executor
     */
    private static ExecutorService executor = Executors.newFixedThreadPool(32);

    /**
     * petr.Main method of Project
     *
     * @param args
     */
    public static void main(String[] args) {
        final String str = ArmadcoString.createStringDataSize(2147483639 / 2); //AbstractStringBuilder.MAX_ARRAY_SIZE
        System.out.println("size of str = " + str.length() / (1024 * 1024) + " MB");
        ArmadcoString armadcoString = new ArmadcoString();

        // 1. one thread
        //armadcoString.runMultiThreads(str, 1L);

        // 2. multi threads
        armadcoString.runMultiThreads(str, 16L);
    }

    /**
     * @param str
     * @param threadCount
     */
    public void runMultiThreads(String str, final long threadCount) {
        System.out.println("\n----\nmulti " + threadCount + " threads: ");
        ArmadcoString.runWithPerformance(() -> {
            try {
                List<Future<CalculateResult>> calculateResults = new ArrayList<>();
                Map<Character, Integer> result = new HashMap<>();
                for (long i = 1; i <= threadCount; i++) {
                    long size = str.length();
                    int start = Long.valueOf((i - 1) * size / threadCount).intValue();
                    int end = Long.valueOf(i * size / threadCount).intValue();
                    Future<CalculateResult> calculateResult = new ArmadcoString().calculate(str, start, end, executor);
                    calculateResults.add(calculateResult);
                }
                executor.shutdown();
                executor.awaitTermination(60, TimeUnit.SECONDS);
                for (Future<CalculateResult> calculateResult : calculateResults) {
                    Map<Character, Integer> charMap = calculateResult.get().getCharMap();
                    for (Map.Entry<Character, Integer> charEntry : charMap.entrySet()) {
                        if (result.get(charEntry.getKey()) == null || result.get(charEntry.getKey()) < charEntry.getValue()) {
                            result.put(charEntry.getKey(), charEntry.getValue());
                        }
                    }
                }
                result.entrySet().stream().forEach(e -> System.out.print(e + " "));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Calculate max length of characters in a row in string for parallel threads
     *
     * @param str
     * @param start
     * @param end
     * @param executor
     * @return map with characters and max length in a row
     */
    public CompletableFuture<CalculateResult> calculate(final String str, final int start, final int end, final Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            CalculateResult calculateResult = new CalculateResult();
            Map<Character, Integer> charMap = calculateResult.getCharMap();
            int count = 0;
            char prevChar = str.charAt(start);
            char currChar = str.charAt(start);
            charMap.put(prevChar, 1);

            for (int i = start + 1; i < end; i++) {
                currChar = str.charAt(i);
                count++;
                if (prevChar != currChar) {
                    Integer countInMap = charMap.get(prevChar);
                    if (countInMap == null || countInMap < count) {
                        charMap.put(prevChar, count);
                    }
                    count = 0;
                }
                prevChar = str.charAt(i);
            }
            if ((charMap.get(currChar) == null || charMap.get(currChar) < count + 1)) {
                charMap.put(currChar,count + 1);
            }
            return calculateResult;
        }, executor);
    }

    /**
     * Run lambda with performance benchmarks
     *
     * @param runnable
     */
    public static void runWithPerformance(Runnable runnable) {
        long startTime = System.nanoTime(); // performance

        runnable.run();

        long endTime = System.nanoTime();
        long ms = (endTime - startTime) / 1000000;
        System.out.println("\ntime = " + ms + " ms");
    }

    /**
     * Create Big String with latin characters
     *
     * @param msgSize
     * @return
     */
    private static String createStringDataSize(int msgSize) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(msgSize);
        for (int i = 0; i < msgSize; i++) {
            char c = (char) (r.nextInt(26) + 'a');
            sb.append(c);
        }
        return sb.toString();
    }

}