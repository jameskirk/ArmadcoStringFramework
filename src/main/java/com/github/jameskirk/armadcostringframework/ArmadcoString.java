package com.github.jameskirk.armadcostringframework;

import java.util.*;
import java.util.concurrent.*;

/**
 * !!! ATTENTION !!!
 * Please run it with VM arguments: -Xms2g -Xmx8g -Xss8m
 * !!! ATTENTION !!!
 */
public class ArmadcoString {

    private static ExecutorService executor = Executors.newFixedThreadPool(32);

    public static void main(String[] args) {
        final String str = ArmadcoString.createStringDataSize(2147483639); //AbstractStringBuilder.MAX_ARRAY_SIZE
        System.out.println("size of str = " + str.length() / (1024 * 1024) + " MB");

        // 1. one thread
        System.out.println("one thread: ");
        ArmadcoString.runWithPerformance(str, () -> {
            Map<Character, Long> charMap = new ArmadcoString().calculate(str);
            charMap.entrySet().stream().forEach(e -> System.out.print(e + " "));
        });

        // 2. multi threads
        System.out.println("\n----\nmulti threads: ");
        final long THREADS = 16;
        ArmadcoString.runWithPerformance(str, () -> {
            try {
                List<Future<CalculateResult>> calculateResults = new ArrayList<>();
                Map<Character, Long> result = new HashMap<>();
                for (long i = 1; i <= THREADS; i++) {
                    long size = str.length();
                    int start = Long.valueOf((i - 1) * size / THREADS).intValue();
                    int end = Long.valueOf(i * size / THREADS).intValue();
                    Future<CalculateResult> calculateResult = new ArmadcoString().calculate(str, start, end, executor);
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

    public Map<Character, Long> calculate(final String str) {
        Map<Character, Long> charMap = new HashMap<>(26);
        long count = 0;
        char prevChar = str.charAt(0);
        charMap.put(prevChar, 1L);
        int size = str.length();

        for (int i = 1; i < size; i++) {
            char currChar = str.charAt(i);
            count++;
            if (prevChar == currChar) {
                // do nothing
            } else {
                if (charMap.get(prevChar) == null || charMap.get(prevChar) < count) {
                    charMap.put(prevChar, Long.valueOf(count));
                } else {
                    // do nothing
                }
                count = 0;
            }
            if (i == size - 1 && (charMap.get(currChar) == null || charMap.get(currChar) < count + 1)) {
                charMap.put(currChar, Long.valueOf(count + 1));
            }
            prevChar = str.charAt(i);
        }
        return charMap;
    }

    public CompletableFuture<CalculateResult> calculate(final String str, final int start, final int end, final Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            CalculateResult calculateResult = new CalculateResult();
            Map<Character, Long> charMap = calculateResult.getCharMap();
            long count = 0;
            char prevChar = str.charAt(start);
            charMap.put(prevChar, 1L);

            for (int i = start + 1; i < end; i++) {
                char currChar = str.charAt(i);
                count++;
                if (prevChar == currChar) {
                    // do nothing
                } else {
                    if (charMap.get(prevChar) == null || charMap.get(prevChar) < count) {
                        charMap.put(prevChar, Long.valueOf(count));
                    } else {
                        // do nothing
                    }
                    count = 0;
                }
                if (i == end - 1 && (charMap.get(currChar) == null || charMap.get(currChar) < count + 1)) {
                    charMap.put(currChar, Long.valueOf(count + 1));
                }
                prevChar = str.charAt(i);
            }
            return calculateResult;
        }, executor);
    }

    private static void runWithPerformance(final String str, Runnable runnable) {
        long startTime = System.nanoTime(); // performance

        runnable.run();

        long endTime = System.nanoTime();
        long ms = (endTime - startTime) / 1000000;
        System.out.println("\ntime = " + ms + " ms");
    }

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