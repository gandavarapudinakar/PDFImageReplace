package com.zessta;

import java.util.regex.Pattern;

/**
 * Test
 */
public class Test {

    public static void main(String[] args) {
        Pattern adharNumber=Pattern.compile("\\d{4}.\\d{4}.\\d{4}");
        System.out.println(adharNumber.matcher(" 95 3962 4463 5778 ‘").find());
    }
}