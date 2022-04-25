package ru.fmtk.khlystov.otus_java.aop;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;


public class BeanCreator {

    private final Map<String, Class<?>> classByBeanName = new HashMap<>();
    private final List<Logger> loggers;

    private static <T> T createInstance(Class<T> clazz) {
        try {
            Constructor<T> ctor = clazz.getConstructor();
            return ctor.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException |
                 InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public BeanCreator(String basePackage) {
        List<Class<?>> classes = ReflectionUtil.getClasses(basePackage);
        Set<Class<? extends Logger>> loggerClasses = ReflectionUtil.filterImplementsInterface(classes, Logger.class);
        this.loggers = loggerClasses.stream()
                .map(BeanCreator::createInstance)
                .collect(Collectors.toList());
        if (this.loggers.isEmpty()) {
            System.out.println("Logger implementation hasn't been found.");
        }

        ReflectionUtil.getClassWithMethodAnnotatedWith(classes, Log.class).forEach(clazz ->
                classByBeanName.put(clazz.getSimpleName(), wrapWithLogging(clazz)));
    }

    public <T> T getBean(String beanName) {
        Class<T> clazz = getClassByBeanName(beanName);
        if (clazz == null) {
            throw new IllegalArgumentException("Class for bean with name '" + beanName + "' wasn't found");
        }
        return createInstance(clazz);
    }

    private Class<?> wrapWithLogging(Class<?> clazz) {
        LogInterceptor logInterceptor = new LogInterceptor(loggers);
        ByteBuddyAgent.install();
        return new ByteBuddy()
                .subclass(clazz)
                .method(ElementMatchers.isAnnotatedWith(Log.class))
                .intercept(MethodDelegation.to(logInterceptor))
                .make()
                .load(
                        clazz.getClassLoader(),
                        ClassReloadingStrategy.fromInstalledAgent())
                .getLoaded();
    }

    private <T> Class<T> getClassByBeanName(String beanName) {
        return (Class<T>) this.classByBeanName.get(beanName);
    }
}
