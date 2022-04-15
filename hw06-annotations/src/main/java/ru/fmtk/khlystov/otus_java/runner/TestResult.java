package ru.fmtk.khlystov.otus_java.runner;

import java.lang.reflect.Method;

public class TestResult {
    private final Method method;
    private final Exception exception;

    public TestResult(Method method) {
        if(method == null) {
            throw new IllegalArgumentException("Not specified method of the result");
        }
        this.method = method;
        this.exception = null;
    }

    public TestResult(Method method, Exception exception) {
        if(method == null) {
            throw new IllegalArgumentException("Not specified method of the result");
        }
        if(exception == null) {
            throw new IllegalArgumentException("Not specified exception of the result with error");
        }
        this.method = method;
        this.exception = exception;
    }

    public boolean isSuccess() {
        return exception == null;
    }

    public Method getMethod() {
        return method;
    }

    public Exception getException() {
        return exception;
    }
}
