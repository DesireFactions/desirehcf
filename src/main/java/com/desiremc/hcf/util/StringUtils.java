package com.desiremc.hcf.util;

public class StringUtils {

    public static String implode(String[] strings, int start, int end) {
        StringBuilder sb = new StringBuilder();

        for (int i = start; i < end; i++) {
            sb.append(strings[i] + " ");
        }

        return sb.toString().trim();
    }

    public static String compile(String[] strings) {
        return implode(strings, 0, strings.length);
    }

}
