package ru.fmtk.khlystov.otus_java.runner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestStat {
    private final int testsCnt;
    private final List<Method> successMethods = new ArrayList<>();
    private final Map<Method, Exception> exceptionMethods = new HashMap<>();

    public TestStat(int testsCnt) {
        this.testsCnt = testsCnt;
    }

    public int getTestsCnt() {
        return testsCnt;
    }

    public List<Method> getSuccessMethods() {
        return successMethods;
    }

    public Map<Method, Exception> getExceptionMethods() {
        return exceptionMethods;
    }

    public void addResult(TestResult testResult) {
        if (testResult.isSuccess()) {
            addSuccess(testResult.getMethod());
        } else {
            addException(testResult.getMethod(), testResult.getException());
        }
    }

    protected void addSuccess(Method method) {
        successMethods.add(method);
    }

    protected void addException(Method method, Exception exception) {
        exceptionMethods.put(method, exception);
    }
}
