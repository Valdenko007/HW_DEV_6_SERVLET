package ua.goit.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import ua.goit.utils.DataBaseConnection;
import ua.goit.model.BaseEntity;
import ua.goit.utils.PropertiesLoader;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Closeable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class CrudRepositoryImpl<T extends BaseEntity<ID>, ID> implements Closeable, CrudRepository<T, ID> {


    private final Connection connection;

    private final ObjectMapper mapper;
    private final Class<T> modelClass;
    private final Map<String, String> columnFieldName;

    private final PreparedStatement findAllPreparedStatement;
    private final PreparedStatement findByIdPreparedStatement;
    private final PreparedStatement deletePreparedStatement;
    private final PreparedStatement createPreparedStatement;
    private final PreparedStatement updatePreparedStatement;

    @SneakyThrows
    public CrudRepositoryImpl(Class<T> modelClass) {
        this.connection = DataBaseConnection.getInstance().getConnection();
        String databaseSchemaName = PropertiesLoader.getProperty("db.name");
        this.mapper = new ObjectMapper();
        this.modelClass = modelClass;
        this.columnFieldName = Arrays.stream(this.modelClass.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toMap(this::getColumnName, Field::getName));

        String[] generatedColumns = getGeneratedColumns();

        String tableName = modelClass.getAnnotation(Entity.class) != null
                ? modelClass.getAnnotation(Entity.class).name() : modelClass.getSimpleName().toLowerCase();

        String countValues = IntStream.range(0, columnFieldName.size()).mapToObj(i -> "?").collect(Collectors.joining(","));
        String fieldsForCreate = columnFieldName.keySet().stream().collect(Collectors.joining(","));
        String fieldsForUpdate = columnFieldName.keySet().stream().map(v -> v + "=?").collect(Collectors.joining(","));

        this.findAllPreparedStatement = connection.prepareStatement("SELECT * FROM " + databaseSchemaName + "." +
                tableName, generatedColumns);
        this.findByIdPreparedStatement = connection.prepareStatement("SELECT * FROM " + databaseSchemaName + "." +
                tableName + " WHERE id=?", generatedColumns);
        this.deletePreparedStatement = connection.prepareStatement("DELETE FROM " + databaseSchemaName + "." +
                tableName + " WHERE id=?", generatedColumns);
        this.createPreparedStatement = connection.prepareStatement("INSERT INTO  " + databaseSchemaName + "." +
                tableName + "(" + fieldsForCreate + ")" + " VALUES (" + countValues + ")", generatedColumns);
        this.updatePreparedStatement = connection.prepareStatement("UPDATE " + databaseSchemaName + "." +
                tableName + " SET " + fieldsForUpdate + " WHERE id=?", generatedColumns);

    }



    private String[] getGeneratedColumns() {
        String generatedColumns[] = {getColumnName(Arrays.stream(this.modelClass.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> field.getAnnotation(Id.class) != null)
                .findAny().orElseThrow(() -> new RuntimeException("Entity must contains ID")))};
        return generatedColumns;
    }

    private String getColumnName(Field field) {
        return field.getAnnotation(Column.class) == null ? field.getName() : field.getAnnotation(Column.class).name();
    }


    @Override
    public List<T> saveAll(Iterable<T> itrb) {
        final List<T> result = new ArrayList<>();
        for (T t : itrb) result.add(save(t));
        return result;
    }

    @SneakyThrows
    @Override
    public T save(T t) {
        if (t.getId() == null || !findById(t.getId()).isPresent()) {
            return executeStatement(createPreparedStatement, t);
        } else {
            updatePreparedStatement.setObject(columnFieldName.size() + 1, t.getId());
            return executeStatement(updatePreparedStatement, t);
        }
    }

    @Override
    public T create(T t) {
        return executeStatement(createPreparedStatement, t);
    }

    @SneakyThrows
    @Override
    public T update(T t) {
        updatePreparedStatement.setObject(columnFieldName.size() + 1 , t.getId());
        return executeStatement(updatePreparedStatement, t);
    }


    @SneakyThrows
    @Override
    public void deleteById(ID id) {
        deletePreparedStatement.setObject(1, id);
        deletePreparedStatement.executeUpdate();
    }

    @SneakyThrows
    @Override
    public Optional<T> findById(ID id) {
        findByIdPreparedStatement.setObject(1, id);
        final List<T> result = parse(findByIdPreparedStatement.executeQuery());
        if (result.isEmpty()) return Optional.empty();
        if (result.size() > 1) throw new RuntimeException("Method 'find by id' returned more than one result ");
        return Optional.of(result.get(0));
    }

    @SneakyThrows
    @Override
    public List<T> findAll() {
        return parse(findAllPreparedStatement.executeQuery());
    }

    @SneakyThrows
    @Override
    public void close() {
        connection.close();
    }

    @SneakyThrows
    private T executeStatement(PreparedStatement preparedStatement, T t) {
        int count = 1;
        for (String fieldName : columnFieldName.values()) {
            Field declaredField = modelClass.getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            preparedStatement.setObject(count++, declaredField.get(t));
        }
        preparedStatement.executeUpdate();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        if (rs.next()) {
            return findById((ID) rs.getObject(1)).get();
        }
        return findById(t.getId()).get();
    }

    @SneakyThrows
    private List<T> parse(ResultSet resultSet) {
        final List<T> result = new ArrayList<>();
        while (resultSet.next()) {
            final Map<String, Object> objectMap = new HashMap<>();
            for (String fieldName : columnFieldName.keySet()) {
                objectMap.put(columnFieldName.get(fieldName), resultSet.getObject(fieldName));
            }
            result.add(mapper.convertValue(objectMap, modelClass));
        }
        return result;
    }

}
