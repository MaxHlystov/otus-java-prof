package ru.fmtk.khlystov.otus_java;

import java.util.Arrays;
import java.util.Objects;

import ru.fmtk.khlystov.otus_java.runner.TestRunnerUtils;

public class Application {
    public static void main(String... args) {
        if (args.length < 1) {
            System.out.println("You should to specify fully specify class name to run tests.");
            return;
        }
        Arrays.stream(args)
                .filter(Objects::nonNull)
                .forEach(path -> {
                    System.out.println("Start to process class with name: " + path);
                    Class<?> clazz = getClass(path);
                    if (clazz == null) {
                        System.out.println("Can't found class with name " + path);
                    } else {
                        TestRunnerUtils.runTests(clazz);
                    }
                });
    }

    private static Class<?> getClass(String path) {
        try {
            return Class.forName(path);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
