package ru.fmtk.khlystov.java_otus.appcontainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.fmtk.khlystov.java_otus.appcontainer.api.AppComponent;
import ru.fmtk.khlystov.java_otus.appcontainer.api.AppComponentsContainer;
import ru.fmtk.khlystov.java_otus.appcontainer.api.AppComponentsContainerConfig;
import ru.fmtk.khlystov.java_otus.appcontainer.api.ComponentDefinition;
import ru.fmtk.khlystov.java_otus.utils.Utils;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private final Map<Class<?>, Object> appComponentByClass = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Map<Integer, List<ComponentDefinition>> definitionsByOrder = readComponentDefinitions(configClass);
        createComponents(configClass, definitionsByOrder);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        C component = (C) appComponentByClass.get(componentClass);
        if (component == null) {
            component = (C) appComponents.stream()
                    .filter(cmp -> cmp.getClass().isAssignableFrom(componentClass))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(""));
        }
        return component;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }

    private static Map<Integer, List<ComponentDefinition>> readComponentDefinitions(Class<?> configClass) {
        Map<Integer, List<ComponentDefinition>> definitionsByOrder = new HashMap<>();
        for (Method method : configClass.getMethods()) {
            if (method.isAnnotationPresent(AppComponent.class)) {
                Class<?> componentType = method.getReturnType();
                AppComponent annotation = method.getAnnotation(AppComponent.class);
                int order = annotation.order();
                String componentName = annotation.name();
                if (Utils.isBlank(componentName)) {
                    throw new IllegalStateException("Component defined in " +
                            configClass.getSimpleName() + "::" + method.getName() + " should has a name.");
                }
                List<ComponentDefinition> definitions = definitionsByOrder.computeIfAbsent(order, ArrayList::new);
                definitions.add(new ComponentDefinition(order, componentName, componentType, method));
            }
        }
        return definitionsByOrder;
    }

    private void createComponents(final Class<?> configClass,
                                  final Map<Integer, List<ComponentDefinition>> definitionsByOrder) {
        final Object configObj = createObject(configClass);
        for (int order = 0; order < definitionsByOrder.size(); ++order) {
            definitionsByOrder.get(order).forEach(definition -> {
                Object component = createComponent(configObj, definition);
                if (component == null) {
                    throw new IllegalStateException("Error creating component defined in " +
                            configClass.getSimpleName() + "::" + definition.getMethod().getName());
                }
                appComponents.add(component);
                appComponentsByName.merge(definition.getName(), component,
                        (o, o2) -> {
                            throw new IllegalStateException(
                                    "Component with name " + definition.getName() + " already exists");
                        });
                appComponentByClass.merge(definition.getClazz(), component,
                        (o, o2) -> {
                            throw new IllegalStateException(
                                    "Component with class " + definition.getClazz().getName() + " already exists");
                        });
            });
        }
    }

    private Object createObject(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object createComponent(final Object configObj, final ComponentDefinition definition) {
        Method method = definition.getMethod();
        try {
            Object[] args = getCreationMethodArgs(definition);
            if (args == null) {
                return method.invoke(configObj);
            }
            return method.invoke(configObj, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] getCreationMethodArgs(ComponentDefinition definition) {
        Class<?>[] argTypes = definition.getMethod().getParameterTypes();
        if (argTypes.length == 0) {
            return null;
        }
        Object[] args = new Object[argTypes.length];
        for (int i = 0; i < argTypes.length; ++i) {
            args[i] = appComponentByClass.get(argTypes[i]);
        }
        return args;
    }


    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}
