package ru.webstudio.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Абстрактный класс описывает общее правило подключения к базе.
 * Сам он не знает, какая именно база используется.
 */
public abstract class AbstractDatabase {
    protected final String url;
    protected final String user;
    protected final String password;

    public AbstractDatabase(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * Абстрактный метод не имеет реализации.
     * Класс-наследник обязан самостоятельно его переопределить.
     */
    public abstract Connection connect() throws SQLException;
}
