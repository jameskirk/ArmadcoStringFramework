package com.github.jameskirk.armadcostringframework;

/**
 * 64-bit alternative of java.lang.String
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
        for (int i=0; i<countOfArrays - 1; i++) {
            value[i] = new byte[MAX_COUNT_BYTE_ARRAY];
        }
        value[countOfArrays - 1] = new byte[getCountOfLastArrayOfArrays(length)];
        this.length = length;
    }

    public char charAt(long index) {
        int indexOfArrays = getIndexOfArraysByValueIndex(index);
        int indexInArrayOfArray = getIndexInArrayOfArraysByValueIndex(index);
        return (char) value[indexOfArrays][indexInArrayOfArray];

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

    private int getCountOfArrays(long length) {
        return (int) (( (length - 1) /(MAX_COUNT_BYTE_ARRAY)) + 1);
    }

    private int getCountOfLastArrayOfArrays(long length) {
        return (int) ((length - 1) % (MAX_COUNT_BYTE_ARRAY) + 1);
    }

    private int getIndexOfArraysByValueIndex(long index) {
        return (int) (index/(MAX_COUNT_BYTE_ARRAY));
    }

    private int getIndexInArrayOfArraysByValueIndex(long index) {
        return (int) (index % (MAX_COUNT_BYTE_ARRAY));
    }
}
