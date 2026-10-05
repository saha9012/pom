package ru.webstudio.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Обычный вариант JDBC-подключения без абстрактного класса.
 * Он оставлен отдельно, чтобы можно было сравнить два подхода.
 */
public class SimpleJdbcConnection {

    public static Connection connect(String url, String user, String password)
            throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
