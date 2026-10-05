package ru.webstudio.repository;

import ru.webstudio.model.ClientRequest;

import java.sql.SQLException;
import java.util.List;

/**
 * Интерфейс — это договор: он говорит, какие методы должны быть,
 * но не описывает, как именно они работают.
 */
public interface RequestRepository {
    void createTable() throws SQLException;

    void add(ClientRequest request) throws SQLException;

    List<ClientRequest> findAll() throws SQLException;
}
