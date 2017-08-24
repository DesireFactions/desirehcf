package me.borawski.hcf.util;

public class StringUtils {

    public static String compile(String[] strings) {
        StringBuilder sb = new StringBuilder();

        for (String s : strings) {
            sb.append(s + " ");
        }

        return sb.toString().trim();
    }

}
