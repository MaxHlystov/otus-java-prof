package ru.fmtk.khlystov.java_otus.appcontainer.api;

import java.lang.reflect.Method;
import java.util.Objects;

import ru.fmtk.khlystov.java_otus.utils.Utils;

public class ComponentDefinition {
    private final int order;
    private final String name;
    private final Class<?> clazz;
    private final Method method;

    public ComponentDefinition(int order, String name, Class<?> clazz, Method method) {
        if (Utils.isBlank(name)) {
            throw new IllegalArgumentException("Name must be not blank");
        }
        if (clazz == null) {
            throw new IllegalArgumentException("Clazz must be not null");
        }
        if (method == null) {
            throw new IllegalArgumentException("Method must be not null");
        }
        this.order = order;
        this.name = name;
        this.clazz = clazz;
        this.method = method;
    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComponentDefinition that = (ComponentDefinition) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
