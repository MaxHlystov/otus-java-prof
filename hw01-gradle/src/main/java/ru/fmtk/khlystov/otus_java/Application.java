package ru.fmtk.khlystov.otus_java;

import com.google.common.collect.ImmutableSet;

public class Application {

    public static void main(String... args) {
        final ImmutableSet<String> colors = ImmutableSet.of(
          "red",
          "orange",
          "yellow",
          "green",
          "blue",
          "purple");
        System.out.println("Hello Application!");
        System.out.println(colors);
    }
}