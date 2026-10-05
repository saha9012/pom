package ru.webstudio.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Абстрактный класс описывает общее правило подключения к базе.
 * Сам он не знает, какая именно база используется: SQLite, MySQL и т. д.
 */
public abstract class AbstractDatabase {
    protected final String url;

    public AbstractDatabase(String url) {
        this.url = url;
    }

    /**
     * Абстрактный метод не имеет реализации.
     * Класс-наследник обязан самостоятельно его переопределить.
     */
    public abstract Connection connect() throws SQLException;
}
