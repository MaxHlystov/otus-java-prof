package ru.fmtk.khlystov.otus_java.runner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import ru.fmtk.khlystov.otus_java.annotations.After;
import ru.fmtk.khlystov.otus_java.annotations.Before;
import ru.fmtk.khlystov.otus_java.annotations.Test;

import static org.reflections.ReflectionUtils.Methods;
import static org.reflections.util.ReflectionUtilsPredicates.withAnnotation;

public class TestRunnerUtils {
    private TestRunnerUtils() {
    }

    public static void runTests(Class<?> clazz) {
        TestContext testContext = getTestContext(clazz);
        int testsCnt = testContext.getTestMethods().size();
        int currentTestIdx = 1;
        TestStat stat = new TestStat(testsCnt);
        for (Method method : testContext.getTestMethods()) {
            System.out.println("\tProcess test #" + currentTestIdx + " of " + testsCnt +
                    ". Name is '" + method.getName() + "'");
            TestResult testResult = runTestMethod(
                    clazz, method, testContext.getBeforeMethods(), testContext.getAfterMethods());
            stat.addResult(testResult);
        }
        printsStat(testsCnt, stat);
    }

    private static TestResult runTestMethod(Class<?> clazz, Method method,
                                            Set<Method> beforeMethods,
                                            Set<Method> afterMethods
    ) {
        try {
            Constructor<?> ctor = clazz.getConstructor();
            Object obj = ctor.newInstance();
            runMethods(obj, beforeMethods);
            runMethods(obj, List.of(method));
            runMethods(obj, afterMethods);
            return new TestResult(method);

        } catch (Exception e) {
            return new TestResult(method, e);
        }
    }

    private static void runMethods(Object obj, Collection<Method> methods
    ) throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            method.invoke(obj);
        }
    }

    public static void printsStat(int testsCnt, TestStat stat) {
        System.out.println("Ran " + testsCnt + " tests.");
        System.out.println("Success results " + stat.getSuccessMethods().size());
        System.out.println("Exception results " + stat.getExceptionMethods().size());
    }

    public static TestContext getTestContext(Class<?> clazz) {
        TestContext result = new TestContext();
        Package pack = clazz.getPackage();
        if (pack != null) {
            result.addBeforeMethod(Methods.get(clazz)
                    .filter(withAnnotation(Before.class))
                    .as(Method.class)
                    .apply(null));
            result.addAfterMethod(Methods.get(clazz)
                    .filter(withAnnotation(After.class))
                    .as(Method.class)
                    .apply(null));
            result.addTestMethod(Methods.get(clazz)
                    .filter(withAnnotation(Test.class))
                    .as(Method.class)
                    .apply(null));
        }
        return result;
    }
}
