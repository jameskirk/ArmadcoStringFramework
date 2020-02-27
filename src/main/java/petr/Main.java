package petr;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

public class Main {

    public static final int ARRSIZE = 1073741824;
    public static final int THREAD_NUMBER = 16;
    public static final int BLOCK_SIZE = ARRSIZE / THREAD_NUMBER;
    static ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUMBER);
    static ConcurrentMap<Integer, Result> resMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        char[] arr = new char[ARRSIZE - 1];
        generate(arr);
        calculateConc(arr);
    }

    private static void calculateConc(char[] arr) throws InterruptedException, java.util.concurrent.ExecutionException {
        long before = System.nanoTime();
        CompletableFuture[] cfList = new CompletableFuture[16];
        for (int i = 0; i < 16; i++) {
            int finalI = i;
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
                Random r = new Random();
                Result calculate = calculate(arr, BLOCK_SIZE * finalI, BLOCK_SIZE * finalI + BLOCK_SIZE - 1);
                resMap.put(finalI, calculate);
            }, executorService);
            cfList[i] = voidCompletableFuture;
        }
        Void aVoid = CompletableFuture.allOf(cfList).get();
        long after = System.nanoTime();
        System.out.println("Calculated in: " + (after - before) / 1000000 + " ms");
    }

    static Result calculate(char[] arr, int start, int end) {
        Result result = new Result();
        int i = start;
        int count = 1;
        char prev = arr[i];
        i++;
        boolean startingChar = true;
        result.startChar = arr[i];
        while (i < end) {
            if (startingChar && arr[i] != result.startChar) {
                result.startLength = count;
                startingChar = false;
            }
            if (arr[i] != prev) {
                Integer orDefault = result.resultMap.getOrDefault(arr[i], 0);
                result.resultMap.put(arr[i], Math.max(orDefault, count));
                count = 1;
            } else {
                count++;
            }
            i++;
        }
        result.endChar = arr[i - 1];
        result.endLength = count;
        return result;
    }


    static void generate(char[] arr) throws InterruptedException, java.util.concurrent.ExecutionException {
        long before = System.nanoTime();
        CompletableFuture[] cfList = new CompletableFuture[16];
        for (int i = 0; i < 16; i++) {
            int finalI = i;
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
                Random r = new Random();
                for (int j = 0; j < BLOCK_SIZE - 1; j++) {
                    char c = (char) (r.nextInt(26) + 'a');
                    arr[BLOCK_SIZE * finalI + j] = c;
                }
            }, executorService);
            cfList[i] = voidCompletableFuture;
        }
        Void aVoid = CompletableFuture.allOf(cfList).get();
        long after = System.nanoTime();
        System.out.println("Generated in: " + (after - before) / 1000000 + " ms");
    }

    static class Result {
        char startChar;
        int startLength;
        char endChar;
        int endLength;
        Map<Character, Integer> resultMap = new HashMap<>(26);
    }
}