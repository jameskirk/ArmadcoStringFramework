package com.github.jameskirk.armadcostringframework;

import java.util.*;
import java.util.concurrent.*;

/**
 * Class for calculate max length of characters in a row
 *
 * @author golovin
 */
public class ArmadcoString {
    /**
     * threads
     */
    public static final int THREAD_NUMBER = 16;
    /**
     * executor
     */
    private ExecutorService executor;

    /**
     * Instantiates a new Armadco string.
     *
     * @param executor the executor
     */
    ArmadcoString(ExecutorService executor) {
        this.executor = executor;
    }

    /**
     * @param str
     * @param threadCount
     */
    public void runMultiThreads(String str, final long threadCount) {
        System.out.println("----\ncalculate multi " + threadCount + " threads: ");
        ArmadcoString.runWithPerformance(() -> {
            try {
                List<Future<CalculateResult>> calculateResults = new ArrayList<>();
                Map<Character, Long> result = new HashMap<>();
                for (long i = 1; i <= threadCount; i++) {
                    long size = str.length();
                    int start = Long.valueOf((i - 1) * size / threadCount).intValue();
                    int end = Long.valueOf(i * size / threadCount).intValue();
                    Future<CalculateResult> calculateResult = calculate(str, start, end, executor);
                    calculateResults.add(calculateResult);
                }
                executor.shutdown();
                executor.awaitTermination(60, TimeUnit.SECONDS);
                for (Future<CalculateResult> calculateResult : calculateResults) {
                    Map<Character, Long> charMap = calculateResult.get().getCharMap();
                    for (Map.Entry<Character, Long> charEntry : charMap.entrySet()) {
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
     * @param str
     * @param threadCount
     */
    public void runMultiThreads(BigString str, final long threadCount) {
        System.out.println("----\ncalculate multi " + threadCount + " threads: ");
        ArmadcoString.runWithPerformance(() -> {
            try {
                List<Future<CalculateResult>> calculateResults = new ArrayList<>();
                Map<Character, Long> result = new HashMap<>();
                for (long i = 1; i <= threadCount; i++) {
                    long size = str.length();
                    long start = (i - 1) * size / threadCount;
                    long end = i * size / threadCount;
                    Future<CalculateResult> calculateResult = calculate(str, start, end, executor);
                    calculateResults.add(calculateResult);
                }
                executor.shutdown();
                executor.awaitTermination(60, TimeUnit.SECONDS);
                for (Future<CalculateResult> calculateResult : calculateResults) {
                    Map<Character, Long> charMap = calculateResult.get().getCharMap();
                    for (Map.Entry<Character, Long> charEntry : charMap.entrySet()) {
                        if (result.get(charEntry.getKey()) == null || result.get(charEntry.getKey()) < charEntry.getValue()) {
                            result.put(charEntry.getKey(), charEntry.getValue());
                        }
                    }
                }
                result.entrySet().stream().forEach(e -> System.out.print(e + " "));
                System.out.println();
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
            Map<Character, Long> charMap = calculateResult.getCharMap();
            long count = 0;
            char prevChar = str.charAt(start);
            char currChar = str.charAt(start);
            charMap.put(prevChar, 1L);

            for (int i = start + 1; i < end; i++) {
                currChar = str.charAt(i);
                count++;
                if (prevChar != currChar) {
                    Long countInMap = charMap.get(prevChar);
                    if (countInMap == null || countInMap < count) {
                        charMap.put(prevChar, count);
                    }
                    count = 0;
                }
                prevChar = str.charAt(i);
            }
            if ((charMap.get(currChar) == null || charMap.get(currChar) < count + 1)) {
                charMap.put(currChar, count + 1);
            }
            return calculateResult;
        }, executor);
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
    public CompletableFuture<CalculateResult> calculate(final BigString str, final long start, final long end, final Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            CalculateResult calculateResult = new CalculateResult();
            Map<Character, Long> charMap = calculateResult.getCharMap();
            long count = 0;
            char prevChar = str.charAt(start);
            char currChar = str.charAt(start);
            charMap.put(prevChar, 1L);

            for (long i = start + 1; i < end; i++) {
                currChar = str.charAt(i);
                count++;
                if (prevChar != currChar) {
                    Long countInMap = charMap.get(prevChar);
                    if (countInMap == null || countInMap < count) {
                        charMap.put(prevChar, count);
                    }
                    count = 0;
                }
                prevChar = str.charAt(i);
            }
            if ((charMap.get(currChar) == null || charMap.get(currChar) < count + 1)) {
                charMap.put(currChar, count + 1);
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
        System.out.println("time = " + ms + " ms");
    }

    /**
     * Create Big String with latin characters
     *
     * @param msgSize
     * @return
     */
    public static String createStringDataSize(final int msgSize) {
        long startTime = System.nanoTime(); // performance
        Random r = new Random();
        StringBuilder sb = new StringBuilder(msgSize);
        for (int i = 0; i < msgSize; i++) {
            char c = (char) (r.nextInt(26) + 'a');
            sb.append(c);
        }
        String result = sb.toString();
        long endTime = System.nanoTime();
        long ms = (endTime - startTime) / 1000000;
        System.out.println("creating string time = " + ms + " ms");
        return result;
    }

}