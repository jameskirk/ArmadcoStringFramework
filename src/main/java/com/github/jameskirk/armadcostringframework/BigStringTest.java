package com.github.jameskirk.armadcostringframework;

/**
 * Tests for class BigString
 */
public class BigStringTest {

    public static void main(String[] args) {
        long size = (long) Integer.MAX_VALUE * 22 / 2 - 100000;
        System.out.println("creating string with size = " + size / (1024 * 1024) + " MB");
        BigString bigString = new BigString(size);
        ArmadcoString.runWithPerformance(() -> {
            for (long i = 0; i < size; i++) {
                char c = 'a';
                bigString.append(c);
            }
        });
        System.out.println("string was successfully created");
    }

}
