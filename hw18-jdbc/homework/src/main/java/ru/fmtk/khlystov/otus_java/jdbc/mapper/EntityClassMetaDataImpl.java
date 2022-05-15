package ru.fmtk.khlystov.otus_java.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ru.otus.crm.model.Id;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;
    private final List<Field> allFields;
    private final Field idField;
    private final List<Field> allFieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.allFields = Arrays.asList(clazz.getDeclaredFields());
        this.idField = getIdField(clazz);
        this.allFieldsWithoutId = idField == null ? allFields :
                allFields.stream()
                        .filter(field -> !idField.equals(field))
                        .collect(Collectors.toList());
    }

    private static Field getIdField(Class<?> clazz) {
        Field foundField = null;
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                if (foundField == null) {
                    foundField = field;
                } else {
                    throw new IllegalArgumentException("Class '" + clazz.getName() + "' must has only one @Id field");
                }
            }
        }
        return foundField;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return allFieldsWithoutId;
    }
}
