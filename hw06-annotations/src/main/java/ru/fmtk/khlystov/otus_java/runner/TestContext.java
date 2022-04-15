package ru.fmtk.khlystov.otus_java.runner;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TestContext {
    private final Set<Method> beforeMethods = new HashSet<>();
    private final Set<Method> afterMethods = new HashSet<>();
    private final Set<Method> testMethods = new HashSet<>();

    public void addBeforeMethod(Collection<Method> methods) {
        beforeMethods.addAll(methods);
    }

    public void addAfterMethod(Collection<Method> methods) {
        afterMethods.addAll(methods);
    }

    public void addTestMethod(Collection<Method> methods) {
        testMethods.addAll(methods);
    }

    public Set<Method> getBeforeMethods() {
        return beforeMethods;
    }

    public Set<Method> getAfterMethods() {
        return afterMethods;
    }

    public Set<Method> getTestMethods() {
        return testMethods;
    }
}
