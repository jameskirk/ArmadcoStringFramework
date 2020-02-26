package com.github.jameskirk.armadcostringframework;

import java.util.HashMap;
import java.util.Map;

/**
 * Result of ArmadcoString.calculate
 *
 * @author golovin
 */
public class CalculateResult {
    /** char map. Character and max length of character in a row */
    private Map<Character, Long> charMap = new HashMap<>(26);
    /** start char */
    private Character startChar;
    /** start char count */
    private int startCharCount;
    /** end char */
    private Character endChar;
    /** end char count */
    private int endCharCount;

    /**
     * default constructor
     */
    public CalculateResult() {
    }

    /**
     * constructor
     *
     * @param startChar      the start char
     * @param startCharCount the start char count
     * @param endChar        the end char
     * @param endCharCount   the end char count
     */
    public CalculateResult(Character startChar, int startCharCount, Character endChar, int endCharCount) {
        this.startChar = startChar;
        this.startCharCount = startCharCount;
        this.endChar = endChar;
        this.endCharCount = endCharCount;
    }

    /**
     * Gets char map.
     *
     * @return the char map
     */
    public Map<Character, Long> getCharMap() {
        return charMap;
    }

    /**
     * Sets char map.
     *
     * @param charMap the char map
     */
    public void setCharMap(Map<Character, Long> charMap) {
        this.charMap = charMap;
    }

    /**
     * Gets start char.
     *
     * @return the start char
     */
    public Character getStartChar() {
        return startChar;
    }

    /**
     * Sets start char.
     *
     * @param startChar the start char
     */
    public void setStartChar(Character startChar) {
        this.startChar = startChar;
    }

    /**
     * Gets start char count.
     *
     * @return the start char count
     */
    public int getStartCharCount() {
        return startCharCount;
    }

    /**
     * Sets start char count.
     *
     * @param startCharCount the start char count
     */
    public void setStartCharCount(int startCharCount) {
        this.startCharCount = startCharCount;
    }

    /**
     * Gets end char.
     *
     * @return the end char
     */
    public Character getEndChar() {
        return endChar;
    }

    /**
     * Sets end char.
     *
     * @param endChar the end char
     */
    public void setEndChar(Character endChar) {
        this.endChar = endChar;
    }

    /**
     * Gets end char count.
     *
     * @return the end char count
     */
    public int getEndCharCount() {
        return endCharCount;
    }

    /**
     * Sets end char count.
     *
     * @param endCharCount the end char count
     */
    public void setEndCharCount(int endCharCount) {
        this.endCharCount = endCharCount;
    }
}