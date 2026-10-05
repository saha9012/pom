package ru.webstudio.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Конкретное подключение к SQLite.
 * Наследование обозначается словом extends.
 */
public class SQLiteDatabase extends AbstractDatabase {

    public SQLiteDatabase(String url) {
        super(url);
    }

    /**
     * Переопределяем абстрактный метод родительского класса.
     */
    @Override
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
