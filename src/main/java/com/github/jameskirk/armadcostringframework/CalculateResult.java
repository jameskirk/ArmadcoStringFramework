package com.github.jameskirk.armadcostringframework;

import java.util.HashMap;
import java.util.Map;

/**
 * Result of ArmadcoString.calculate
 */
public class CalculateResult {
    private Map<Character, Long> charMap = new HashMap<>(26);
    private Character startChar;
    private int startCharCount;
    private Character endChar;
    private int endCharCount;

    public CalculateResult() {
    }

    public CalculateResult(Character startChar, int startCharCount, Character endChar, int endCharCount) {
        this.startChar = startChar;
        this.startCharCount = startCharCount;
        this.endChar = endChar;
        this.endCharCount = endCharCount;
    }

    public Map<Character, Long> getCharMap() {
        return charMap;
    }

    public void setCharMap(Map<Character, Long> charMap) {
        this.charMap = charMap;
    }

    public Character getStartChar() {
        return startChar;
    }

    public void setStartChar(Character startChar) {
        this.startChar = startChar;
    }

    public int getStartCharCount() {
        return startCharCount;
    }

    public void setStartCharCount(int startCharCount) {
        this.startCharCount = startCharCount;
    }

    public Character getEndChar() {
        return endChar;
    }

    public void setEndChar(Character endChar) {
        this.endChar = endChar;
    }

    public int getEndCharCount() {
        return endCharCount;
    }

    public void setEndCharCount(int endCharCount) {
        this.endCharCount = endCharCount;
    }
}