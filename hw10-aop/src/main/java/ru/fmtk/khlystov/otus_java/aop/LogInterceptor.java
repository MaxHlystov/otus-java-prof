package ru.fmtk.khlystov.otus_java.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.bytebuddy.dynamic.TargetType;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.Super;

public class LogInterceptor {

    private final List<Logger> loggers;

    public LogInterceptor(List<Logger> loggers) {
        this.loggers = loggers;
    }

    @RuntimeType
    public void intercept(@AllArguments Object[] allArguments,
                          @Origin Method method,
                          @Super(proxyType = TargetType.class) Object delegate) {
        log(getMethodCallDescr(method, allArguments));
        try {
            method.invoke(delegate, allArguments);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private String getMethodCallDescr(Method method, Object[] args) {
        StringBuilder sb = new StringBuilder("executed method: ");
        sb.append(method.getName());
        sb.append(", ");
        if (args == null || args.length == 0) {
            sb.append("with no params");
        } else {
            sb.append("param: ");
            boolean isFirst = true;
            for (Object arg : args) {
                if (!isFirst) {
                    sb.append(", ");
                } else {
                    isFirst = false;
                }
                sb.append(arg);
            }
        }
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    private void log(String s) {
        loggers.forEach(logger -> logger.add(s));
    }
}