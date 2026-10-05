package ru.webstudio.repository;

import ru.webstudio.database.AbstractDatabase;
import ru.webstudio.exception.RepositoryException;
import ru.webstudio.model.ClientRequest;
import ru.webstudio.model.enums.RequestStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CRUD-репозиторий заявок для PostgreSQL.
 * Здесь находится только работа с SQL, без бизнес-логики.
 */
public class PostgresRequestRepository implements RequestRepository {
    private final AbstractDatabase database;

    public PostgresRequestRepository(AbstractDatabase database) {
        this.database = database;
    }

    @Override
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS client_requests ("
                + "id SERIAL PRIMARY KEY,"
                + "client_name VARCHAR(150) NOT NULL,"
                + "project_description TEXT NOT NULL,"
                + "status VARCHAR(30) NOT NULL"
                + ")";

        try (Connection connection = database.connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException exception) {
            throw databaseError("Не удалось создать таблицу", exception);
        }
    }

    @Override
    public ClientRequest save(ClientRequest request) {
        String sql = "INSERT INTO client_requests "
                + "(client_name, project_description, status) VALUES (?, ?, ?)";

        try (Connection connection = database.connect();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {
            fillStatement(statement, request);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    request.setId(keys.getInt(1));
                }
            }
            return request;
        } catch (SQLException exception) {
            throw databaseError("Не удалось сохранить заявку", exception);
        }
    }

    @Override
    public Optional<ClientRequest> findById(int id) {
        String sql = "SELECT id, client_name, project_description, status "
                + "FROM client_requests WHERE id = ?";

        try (Connection connection = database.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(mapRow(result));
                }
                return Optional.empty();
            }
        } catch (SQLException exception) {
            throw databaseError("Не удалось найти заявку", exception);
        }
    }

    @Override
    public List<ClientRequest> findAll() {
        String sql = "SELECT id, client_name, project_description, status "
                + "FROM client_requests";
        List<ClientRequest> requests = new ArrayList<ClientRequest>();

        try (Connection connection = database.connect();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {
            while (result.next()) {
                requests.add(mapRow(result));
            }
            return requests;
        } catch (SQLException exception) {
            throw databaseError("Не удалось получить заявки", exception);
        }
    }

    @Override
    public boolean update(ClientRequest request) {
        String sql = "UPDATE client_requests "
                + "SET client_name = ?, project_description = ?, status = ? "
                + "WHERE id = ?";

        try (Connection connection = database.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillStatement(statement, request);
            statement.setInt(4, request.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw databaseError("Не удалось обновить заявку", exception);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM client_requests WHERE id = ?";

        try (Connection connection = database.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw databaseError("Не удалось удалить заявку", exception);
        }
    }

    private void fillStatement(PreparedStatement statement, ClientRequest request)
            throws SQLException {
        statement.setString(1, request.getClientName());
        statement.setString(2, request.getProjectDescription());
        statement.setString(3, request.getStatus().name());
    }

    private ClientRequest mapRow(ResultSet result) throws SQLException {
        return new ClientRequest(
                result.getInt("id"),
                result.getString("client_name"),
                result.getString("project_description"),
                RequestStatus.valueOf(result.getString("status"))
        );
    }

    private RepositoryException databaseError(String message, SQLException cause) {
        return new RepositoryException(message + ": " + cause.getMessage(), cause);
    }
}
