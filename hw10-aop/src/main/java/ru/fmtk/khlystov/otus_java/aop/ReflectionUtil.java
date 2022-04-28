package ru.fmtk.khlystov.otus_java.aop;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionUtil {
    private ReflectionUtil() {
    }

    public static <T> Set<Class<? extends T>> filterImplementsInterface(Collection<Class<?>> classes,
                                                                        Class<T> interfaceClass) {
        return classes.stream()
                .filter(clazz -> isImplementsInterface(clazz, interfaceClass))
                .map(clazz -> (Class<T>) clazz)
                .collect(Collectors.toSet());
    }

    public static List<Class<?>> filterHasWithAnnotation(Collection<Class<?>> classes,
                                                         Class<? extends Annotation> annotationClass) {
        return classes.stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotationClass))
                .collect(Collectors.toList());
    }


    public static Set<Class<?>> getClassWithMethodAnnotatedWith(List<Class<?>> classes,
                                                                Class<? extends Annotation> annotationClass) {
        return classes.stream()
                .filter(clazz -> hasClassWithMethodAnnotatedWith(clazz, annotationClass))
                .collect(Collectors.toSet());
    }

    public static boolean hasClassWithMethodAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return Arrays.stream(clazz.getMethods())
                .anyMatch(method -> method.isAnnotationPresent(annotationClass));
    }

    public static List<Class<?>> getClasses(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            return Collections.emptyList();
        }
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = getResources(classLoader, path);
        if (resources == null) {
            return Collections.emptyList();
        }
        List<File> dirs = getFilesFromUrls(resources);
        return getClasses(packageName, dirs);
    }

    private static boolean isImplementsInterface(Class<?> clazz, Class<?> interfaceClass) {
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> i : interfaces) {
            if (i.toString().equals(interfaceClass.toString())) {
                return true;
            }
        }
        return false;
    }

    private static List<Class<?>> getClasses(String packageName, List<File> dirs) {
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static Enumeration<URL> getResources(ClassLoader classLoader, String path) {
        try {
            return classLoader.getResources(path);
        } catch (IOException e) {
            return null;
        }
    }

    private static List<File> getFilesFromUrls(Enumeration<URL> resources) {
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        return dirs;
    }

    private static List<Class<?>> findClasses(File directory, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (!file.getName().contains(".")) {
                        classes.addAll(findClasses(file, packageName + "." + file.getName()));
                    }
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' +
                            file.getName().substring(0, file.getName().length() - 6);
                    try {
                        classes.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                    }
                }
            }
        }
        return classes;
    }
}
