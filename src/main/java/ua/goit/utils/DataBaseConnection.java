package ua.goit.utils;

import lombok.SneakyThrows;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnection implements Closeable {

    private static final String user_name = PropertiesLoader.getProperty("db.username");
    private static final String password = PropertiesLoader.getProperty("db.password");
    private static final String URL = PropertiesLoader.getProperty("db.url");
    private static final String JDBC_DRIVER = PropertiesLoader.getProperty("db.driver");
    private static DataBaseConnection dataBaseConnection;
    private final Connection connection;

    @SneakyThrows
    private DataBaseConnection() {
        Class.forName(JDBC_DRIVER);
        this.connection = DriverManager.getConnection(URL, user_name, password);
    }

    @SneakyThrows
    public static DataBaseConnection getInstance() {
        if (dataBaseConnection == null) {
            dataBaseConnection = new DataBaseConnection();
        } else if (dataBaseConnection.connection.isClosed()) {
            dataBaseConnection = new DataBaseConnection();
        }
        return dataBaseConnection;
    }

    public Connection getConnection() {
        return connection;
    }

    @SneakyThrows
    @Override
    public void close() {
        connection.close();
    }
}
