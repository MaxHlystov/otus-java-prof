package ru.fmtk.khlystov.otus_java;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ru.fmtk.khlystov.otus_java.annotations.After;
import ru.fmtk.khlystov.otus_java.annotations.Before;
import ru.fmtk.khlystov.otus_java.annotations.Test;

public class SimpleTest {

    private List<String> values;
    private String startStop = "not defined";

    @Before
    public void initValues() {
        System.out.println("SimpleTest.initValues");
        values = IntStream.iterate(0, x -> x + 1)
                .mapToObj(Integer::toString)
                .limit(100)
                .collect(Collectors.toList());
        startStop = "started";
    }

    @After
    public void clearValues() {
        System.out.println("SimpleTest.clearValues");
        values = Collections.emptyList();
        startStop = "stopped";
    }

    @After
    public void afterWithException() throws IOException {
        System.out.println("Throw an exception");
        throw new IOException("Test io exception.");
    }

    @Test
    public void test1() {
        System.out.println("Test 1 print values: " + values);
    }

    @Test
    public void test2() {
        throw new RuntimeException("Test 2 throw runtime error");
    }
}
