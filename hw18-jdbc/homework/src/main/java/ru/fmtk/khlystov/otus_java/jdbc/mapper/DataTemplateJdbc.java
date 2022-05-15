package ru.fmtk.khlystov.otus_java.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor,
                            EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData
    ) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection,
                entitySQLMetaData.getSelectByIdSql(),
                List.of(id),
                this::createObjectFromResultSet);
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection,
                entitySQLMetaData.getSelectAllSql(),
                Collections.emptyList(),
                rs -> {
                    var results = new ArrayList<T>();
                    try {
                        while (rs.next()) {
                            results.add(createObjectFromResultSet(rs));
                        }
                        return results;
                    } catch (SQLException e) {
                        throw new DataTemplateException(e);
                    }
                }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T obj) {
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(),
                    getFieldValues(obj, entityClassMetaData.getFieldsWithoutId()));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T obj) {
        try {
            var updateValues = new ArrayList<>(getFieldValues(obj, entityClassMetaData.getFieldsWithoutId()));
            updateValues.add(getField(obj, entityClassMetaData.getIdField()));
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), updateValues);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getFieldValues(final T obj, final List<Field> fields) {
        return fields.stream()
                .map(field -> {
                    try {
                        return getField(obj, field);
                    } catch (IllegalAccessException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private Object getField(T obj, Field field) throws IllegalAccessException {
        boolean accessible = field.canAccess(obj);
        field.setAccessible(true);
        Object value = field.get(obj);
        field.setAccessible(accessible);
        return value;
    }

    private void setField(T obj, Field field, Object value) throws IllegalAccessException {
        boolean accessible = field.canAccess(obj);
        field.setAccessible(true);
        field.set(obj, value);
        field.setAccessible(accessible);
    }

    private T createObjectFromResultSet(ResultSet rs) {
        try {
            if (rs.next()) {
                T obj = entityClassMetaData.getConstructor().newInstance();
                setField(obj, entityClassMetaData.getIdField(), rs.getLong(entityClassMetaData.getIdField().getName()));
                for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                    setField(obj, field, rs.getObject(field.getName()));
                }
                return obj;
            }
            return null;
        } catch (SQLException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }
}
