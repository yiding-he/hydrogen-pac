package com.hyd.hydrogenpac.utils;

import java.util.Random;

public class RandomStringGenerator {

    public static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";

    public static final String DIGITS = "1234567890";

    public static String generate(int length, boolean digits, boolean letters, boolean differCase) {

        String charList = "" + (digits? DIGITS: "") + (letters? LETTERS : "") + (differCase? LETTERS.toUpperCase(): "");
        if (charList.length() == 0) {
            return "";
        }

        int charCount = charList.length();
        Random random = new Random();
        char[] chars = new char[length];

        for (int i = 0; i < length; i++) {
            chars[i] = charList.charAt(random.nextInt(charCount));
        }

        return new String(chars);
    }
}