package ru.fmtk.khlystov.java_otus.utils;

public class Utils {
    private Utils() {}

    public static boolean isBlank(String s) {
        return s == null || "".equals(s.trim());
    }
}
