package ru.webstudio.repository;

import ru.webstudio.database.AbstractDatabase;
import ru.webstudio.model.ClientRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация интерфейса RequestRepository для базы SQLite.
 */
public class SQLiteRequestRepository implements RequestRepository {
    private final AbstractDatabase database;

    /**
     * Принимаем абстрактный тип, а не конкретный SQLiteDatabase.
     * Это пример полиморфизма: сюда можно передать любой подходящий
     * дочерний класс AbstractDatabase.
     */
    public SQLiteRequestRepository(AbstractDatabase database) {
        this.database = database;
    }

    @Override
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS client_requests ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "client_name TEXT NOT NULL,"
                + "project_description TEXT NOT NULL,"
                + "status TEXT NOT NULL"
                + ")";

        // try-with-resources автоматически закрывает подключение и Statement.
        try (Connection connection = database.connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Override
    public void add(ClientRequest request) throws SQLException {
        String sql = "INSERT INTO client_requests "
                + "(client_name, project_description, status) VALUES (?, ?, ?)";

        // PreparedStatement безопасно подставляет данные вместо знаков вопроса.
        try (Connection connection = database.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, request.getClientName());
            statement.setString(2, request.getProjectDescription());
            statement.setString(3, request.getStatus());
            statement.executeUpdate();
        }
    }

    @Override
    public List<ClientRequest> findAll() throws SQLException {
        String sql = "SELECT id, client_name, project_description, status "
                + "FROM client_requests ORDER BY id";
        List<ClientRequest> requests = new ArrayList<ClientRequest>();

        try (Connection connection = database.connect();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                ClientRequest request = new ClientRequest(
                        result.getInt("id"),
                        result.getString("client_name"),
                        result.getString("project_description"),
                        result.getString("status")
                );
                requests.add(request);
            }
        }

        return requests;
    }
}
