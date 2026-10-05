package ru.webstudio.repository;

import ru.webstudio.model.ClientRequest;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс — это договор: он говорит, какие методы должны быть,
 * но не описывает, как именно они работают.
 */
public interface RequestRepository {
    void createTable();

    ClientRequest save(ClientRequest request);

    Optional<ClientRequest> findById(int id);

    List<ClientRequest> findAll();

    boolean update(ClientRequest request);

    boolean deleteById(int id);
}
