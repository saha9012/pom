package ru.webstudio;

import ru.webstudio.database.AbstractDatabase;
import ru.webstudio.database.SQLiteDatabase;
import ru.webstudio.database.SimpleJdbcConnection;
import ru.webstudio.model.ClientRequest;
import ru.webstudio.repository.RequestRepository;
import ru.webstudio.repository.SQLiteRequestRepository;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    // SQLite хранит всю базу в обычном файле webstudio.db рядом с pom.xml.
    private static final String DATABASE_URL = "jdbc:sqlite:webstudio.db";

    public static void main(String[] args) {
        configureConsole();
        showSimpleConnectionExample();

        // Переменная имеет абстрактный тип, а объект — конкретный тип SQLite.
        AbstractDatabase database = new SQLiteDatabase(DATABASE_URL);
        RequestRepository repository = new SQLiteRequestRepository(database);

        try {
            repository.createTable();
            runMenu(repository);
        } catch (SQLException exception) {
            System.out.println("Ошибка базы данных: " + exception.getMessage());
        }
    }

    /**
     * Cursor использует UTF-8. Эта настройка нужна, чтобы русский текст
     * правильно отображался в консоли даже при запуске на старой Java 8.
     */
    private static void configureConsole() {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException exception) {
            System.out.println("Не удалось включить UTF-8.");
        }
    }

    /**
     * Обычное прямое JDBC-подключение через DriverManager.
     */
    private static void showSimpleConnectionExample() {
        try (Connection ignored = SimpleJdbcConnection.connect(DATABASE_URL)) {
            System.out.println("Обычное JDBC-подключение выполнено успешно.");
        } catch (SQLException exception) {
            System.out.println("Не удалось подключиться к базе: " + exception.getMessage());
        }
    }

    private static void runMenu(RequestRepository repository) throws SQLException {
        Scanner scanner = new Scanner(System.in, "UTF-8");
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("=== Веб-студия ===");
            System.out.println("1. Добавить заявку клиента");
            System.out.println("2. Показать все заявки");
            System.out.println("0. Выход");
            System.out.print("Выберите пункт: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addRequest(scanner, repository);
                    break;
                case "2":
                    showRequests(repository);
                    break;
                case "0":
                    running = false;
                    System.out.println("Программа завершена.");
                    break;
                default:
                    System.out.println("Такого пункта нет. Введите 0, 1 или 2.");
            }
        }

        scanner.close();
    }

    private static void addRequest(Scanner scanner, RequestRepository repository)
            throws SQLException {
        System.out.print("Имя клиента: ");
        String clientName = scanner.nextLine().trim();

        System.out.print("Что нужно разработать: ");
        String description = scanner.nextLine().trim();

        if (clientName.isEmpty() || description.isEmpty()) {
            System.out.println("Имя и описание не должны быть пустыми.");
            return;
        }

        ClientRequest request = new ClientRequest(clientName, description);
        repository.add(request);
        System.out.println("Заявка сохранена в webstudio.db.");
    }

    private static void showRequests(RequestRepository repository) throws SQLException {
        List<ClientRequest> requests = repository.findAll();

        if (requests.isEmpty()) {
            System.out.println("Заявок пока нет.");
            return;
        }

        System.out.println("--- Заявки клиентов ---");
        for (ClientRequest request : requests) {
            System.out.println(request);
        }
    }
}
