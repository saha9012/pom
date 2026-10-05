package ru.webstudio.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Подключение к PostgreSQL через абстрактный класс.
 */
public class PostgresDatabase extends AbstractDatabase {

    public PostgresDatabase(String url, String user, String password) {
        super(url, user, password);
    }

    /**
     * Переопределение абстрактного метода родительского класса.
     */
    @Override
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
