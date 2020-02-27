package com.github.jameskirk.armadcostringframework;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * 64-bit alternative of java.lang.String
 *
 * @author golovin
 */
public final class BigString {
    public static final int MAX_COUNT_BYTE_ARRAY = Integer.MAX_VALUE - 8;

    private byte[][] value;
    private long length;
    private long indexLastInitedChar = -1;

    public BigString(long length) {
        int countOfArrays = getCountOfArrays(length);
        value = new byte[countOfArrays][];
        for (int i = 0; i < countOfArrays - 1; i++) {
            value[i] = new byte[MAX_COUNT_BYTE_ARRAY];
        }
        value[countOfArrays - 1] = new byte[getCountOfLastArrayOfArrays(length)];
        this.length = length;
    }

    public char charAt(long index) {
        int indexOfArrays = (int) (index / ((long) MAX_COUNT_BYTE_ARRAY));
        int indexInArrayOfArray = (int) (index % ((long) MAX_COUNT_BYTE_ARRAY));
        return (char) value[indexOfArrays][indexInArrayOfArray];
    }

    public void setCharAt(long index, char ch) {
        int indexOfArrays = (int) (index / (MAX_COUNT_BYTE_ARRAY));
        int indexInArrayOfArray = (int) (index % (MAX_COUNT_BYTE_ARRAY));
        value[indexOfArrays][indexInArrayOfArray] = (byte) ch;
    }

    public void append(char c) {
        if (indexLastInitedChar + 1 >= length) {
            // TODO: reinit value[][]
        }
        indexLastInitedChar++;
        int countOfArrays = getIndexOfArraysByValueIndex(indexLastInitedChar);
        int indexInArrayOfArray = getIndexInArrayOfArraysByValueIndex(indexLastInitedChar);
        this.value[countOfArrays][indexInArrayOfArray] = (byte) c;
    }

    public void initRandomLatin(final int THREAD_NUMBER, Executor executor) {
        final long BLOCK_SIZE = length / (long) THREAD_NUMBER;
        CompletableFuture[] cfList = new CompletableFuture[THREAD_NUMBER];
        for (int i = 0; i < THREAD_NUMBER; i++) {
            int finalI = i;
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
                Random r = new Random();
                for (int j = 0; j < BLOCK_SIZE - 1; j++) {
                    char c = (char) (r.nextInt(26) + 'a');
                    setCharAt(BLOCK_SIZE * finalI + j, c);
                }
            }, executor);
            cfList[i] = voidCompletableFuture;
        }
        try {
            Void aVoid = CompletableFuture.allOf(cfList).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public long length() {
        return length;
    }

    private int getCountOfArrays(long length) {
        return (int) (((length - 1) / (MAX_COUNT_BYTE_ARRAY)) + 1);
    }

    private int getCountOfLastArrayOfArrays(long length) {
        return (int) ((length - 1) % (MAX_COUNT_BYTE_ARRAY) + 1);
    }

    private int getIndexOfArraysByValueIndex(long index) {
        return (int) (index / (MAX_COUNT_BYTE_ARRAY));
    }

    private int getIndexInArrayOfArraysByValueIndex(long index) {
        return (int) (index % (MAX_COUNT_BYTE_ARRAY));
    }

}
