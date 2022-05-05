package ru.fmtk.khlystov.otus_java.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.crm.service.DbServiceClientImpl;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        StringBuilder result = getSelectFromQuery();
        return logAndResolve("getSelectAllSql", result);
    }

    @Override
    public String getSelectByIdSql() {
        StringBuilder result = getSelectFromQuery();
        result.append("WHERE ");
        result.append(entityClassMetaData.getIdField().getName());
        result.append(" = ?");
        result.append(System.lineSeparator());
        return logAndResolve("getSelectByIdSql", result);
    }

    @Override
    public String getInsertSql() {
        StringBuilder result = new StringBuilder("INSERT INTO ");
        result.append(entityClassMetaData.getName().toLowerCase(Locale.ROOT));
        result.append("(");
        setFieldsQuery(result, entityClassMetaData.getFieldsWithoutId());
        result.append(")");
        result.append(System.lineSeparator());
        result.append("VALUES (?");
        result.append(", ?".repeat(Math.max(0, entityClassMetaData.getFieldsWithoutId().size() - 1)));
        result.append(")");
        result.append(System.lineSeparator());
        return logAndResolve("getInsertSql", result);
    }

    @Override
    public String getUpdateSql() {
        StringBuilder result = new StringBuilder("UPDATE ");
        result.append(entityClassMetaData.getName());
        result.append(" SET ");
        setFieldsForUpdateQuery(result, entityClassMetaData.getFieldsWithoutId());
        result.append(System.lineSeparator());
        result.append("WHERE ");
        result.append(entityClassMetaData.getIdField());
        result.append(" = ?");
        result.append(System.lineSeparator());
        return logAndResolve("getUpdateSql", result);
    }

    private String logAndResolve(String prefix, StringBuilder stringBuilder) {
        var query = stringBuilder.toString();
        log.debug(prefix + " " + query);
        return query;
    }

    private StringBuilder getSelectFromQuery() {
        StringBuilder result = getSelectQuery();
        result.append("FROM ");
        result.append(entityClassMetaData.getName());
        result.append(System.lineSeparator());
        return result;
    }

    private StringBuilder getSelectQuery() {
        StringBuilder result = new StringBuilder("SELECT");
        result.append(System.lineSeparator());
        setFieldsQuery(result, entityClassMetaData.getAllFields());
        result.append(System.lineSeparator());
        return result;
    }

    private void setFieldsQuery(StringBuilder result, List<Field> fields) {
        boolean isFirst = true;
        for (Field field : fields) {
            if (isFirst) {
                isFirst = false;
            } else {
                result.append(",");
                result.append(System.lineSeparator());
            }
            result.append("    ");
            result.append(field.getName());
        }
    }

    private void setFieldsForUpdateQuery(StringBuilder result, List<Field> fields) {
        boolean isFirst = true;
        for (Field field : fields) {
            if (isFirst) {
                isFirst = false;
            } else {
                result.append(",");
                result.append(System.lineSeparator());
            }
            result.append("    ");
            result.append(field.getName());
            result.append(" = ?");
        }
    }
}
