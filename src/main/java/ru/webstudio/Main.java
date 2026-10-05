package ru.webstudio;

import ru.webstudio.database.AbstractDatabase;
import ru.webstudio.database.PostgresDatabase;
import ru.webstudio.database.SimpleJdbcConnection;
import ru.webstudio.exception.ExportException;
import ru.webstudio.exception.RepositoryException;
import ru.webstudio.exception.RequestNotFoundException;
import ru.webstudio.exception.ValidationException;
import ru.webstudio.export.CsvExporter;
import ru.webstudio.model.ClientRequest;
import ru.webstudio.model.enums.RequestSortField;
import ru.webstudio.model.enums.RequestStatus;
import ru.webstudio.model.enums.SortDirection;
import ru.webstudio.repository.PostgresRequestRepository;
import ru.webstudio.repository.RequestRepository;
import ru.webstudio.service.ClientRequestService;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
    // Эти значения можно переопределить переменными окружения.
    private static final String DATABASE_URL =
            env("DB_URL", "jdbc:postgresql://localhost:5432/webstudio");
    private static final String DATABASE_USER = env("DB_USER", "postgres");
    private static final String DATABASE_PASSWORD = env("DB_PASSWORD", "postgres");

    public static void main(String[] args) {
        configureConsole();

        if (!showSimpleConnectionExample()) {
            printDatabaseHelp();
            return;
        }

        // Полиморфизм: тип абстрактный, а объект конкретный — PostgreSQL.
        AbstractDatabase database = new PostgresDatabase(
                DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        RequestRepository repository = new PostgresRequestRepository(database);
        ClientRequestService service = new ClientRequestService(repository);
        CsvExporter exporter = new CsvExporter();

        try {
            repository.createTable();
            runMenu(service, exporter);
        } catch (RepositoryException exception) {
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
    private static boolean showSimpleConnectionExample() {
        try (Connection ignored = SimpleJdbcConnection.connect(
                DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            System.out.println("Обычное JDBC-подключение выполнено успешно.");
            return true;
        } catch (SQLException exception) {
            System.out.println("Не удалось подключиться к базе: " + exception.getMessage());
            return false;
        }
    }

    private static void runMenu(ClientRequestService service, CsvExporter exporter) {
        Scanner scanner = new Scanner(System.in, "UTF-8");
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        addRequest(scanner, service);
                        break;
                    case "2":
                        printRequests(service.getAll());
                        break;
                    case "3":
                        showById(scanner, service);
                        break;
                    case "4":
                        updateRequest(scanner, service);
                        break;
                    case "5":
                        deleteRequest(scanner, service);
                        break;
                    case "6":
                        sortRequests(scanner, service);
                        break;
                    case "7":
                        filterRequests(scanner, service);
                        break;
                    case "8":
                        exportRequests(service, exporter);
                        break;
                    case "0":
                        running = false;
                        System.out.println("Программа завершена.");
                        break;
                    default:
                        System.out.println("Такого пункта нет.");
                }
            } catch (ValidationException | RequestNotFoundException
                     | RepositoryException | ExportException exception) {
                System.out.println("Ошибка: " + exception.getMessage());
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("=== Веб-студия ===");
        System.out.println("1. Добавить заявку");
        System.out.println("2. Показать все заявки");
        System.out.println("3. Найти заявку по ID");
        System.out.println("4. Изменить заявку");
        System.out.println("5. Удалить заявку");
        System.out.println("6. Сортировать заявки");
        System.out.println("7. Фильтровать заявки");
        System.out.println("8. Экспортировать в CSV");
        System.out.println("0. Выход");
        System.out.print("Выберите пункт: ");
    }

    private static void addRequest(Scanner scanner, ClientRequestService service) {
        System.out.print("Имя клиента: ");
        String clientName = scanner.nextLine();
        System.out.print("Что нужно разработать: ");
        String description = scanner.nextLine();

        ClientRequest created = service.create(clientName, description);
        System.out.println("Заявка создана. ID: " + created.getId());
    }

    private static void showById(Scanner scanner, ClientRequestService service) {
        int id = readInt(scanner, "Введите ID: ");
        System.out.println(service.getById(id));
    }

    private static void updateRequest(Scanner scanner, ClientRequestService service) {
        int id = readInt(scanner, "ID изменяемой заявки: ");
        System.out.print("Новое имя клиента: ");
        String name = scanner.nextLine();
        System.out.print("Новое описание: ");
        String description = scanner.nextLine();
        RequestStatus status = readStatus(scanner, false);

        System.out.println("Обновлено: " + service.update(id, name, description, status));
    }

    private static void deleteRequest(Scanner scanner, ClientRequestService service) {
        int id = readInt(scanner, "ID удаляемой заявки: ");
        service.delete(id);
        System.out.println("Заявка удалена.");
    }

    private static void sortRequests(Scanner scanner, ClientRequestService service) {
        System.out.println("Поле: 1 — ID, 2 — имя клиента, 3 — статус");
        int fieldNumber = readInt(scanner, "Выберите поле: ");
        RequestSortField field;
        switch (fieldNumber) {
            case 1:
                field = RequestSortField.ID;
                break;
            case 2:
                field = RequestSortField.CLIENT_NAME;
                break;
            case 3:
                field = RequestSortField.STATUS;
                break;
            default:
                throw new ValidationException("Поле сортировки должно быть от 1 до 3");
        }

        int directionNumber = readInt(
                scanner, "Направление: 1 — прямое, 2 — обратное: ");
        SortDirection direction;
        if (directionNumber == 1) {
            direction = SortDirection.ASCENDING;
        } else if (directionNumber == 2) {
            direction = SortDirection.DESCENDING;
        } else {
            throw new ValidationException("Направление должно быть 1 или 2");
        }

        printRequests(service.sort(field, direction));
    }

    private static void filterRequests(Scanner scanner, ClientRequestService service) {
        System.out.print("Часть имени клиента (Enter — любое): ");
        String name = scanner.nextLine();
        System.out.print("Часть описания (Enter — любое): ");
        String description = scanner.nextLine();
        RequestStatus status = readStatus(scanner, true);

        printRequests(service.filter(name, description, status));
    }

    private static void exportRequests(ClientRequestService service, CsvExporter exporter) {
        Path file = Paths.get("client_requests.csv").toAbsolutePath();
        exporter.export(service.getAll(), file);
        System.out.println("CSV создан: " + file);
    }

    private static void printRequests(List<ClientRequest> requests) {
        if (requests.isEmpty()) {
            System.out.println("Заявок пока нет.");
            return;
        }

        System.out.println("--- Заявки клиентов ---");
        for (ClientRequest request : requests) {
            System.out.println(request);
        }
    }

    private static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String value = scanner.nextLine().trim();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new ValidationException("Нужно ввести целое число");
        }
    }

    private static RequestStatus readStatus(Scanner scanner, boolean allowEmpty) {
        System.out.println("Статус: 1 — новая, 2 — в работе, "
                + "3 — завершена, 4 — отменена");
        System.out.print(allowEmpty ? "Выберите статус (Enter — любой): "
                : "Выберите статус: ");
        String value = scanner.nextLine().trim();

        if (allowEmpty && value.isEmpty()) {
            return null;
        }

        try {
            return RequestStatus.fromNumber(Integer.parseInt(value));
        } catch (IllegalArgumentException exception) {
            throw new ValidationException("Статус должен быть числом от 1 до 4");
        }
    }

    private static String env(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.trim().isEmpty() ? defaultValue : value;
    }

    private static void printDatabaseHelp() {
        System.out.println("Запустите PostgreSQL и создайте базу webstudio.");
        System.out.println("По умолчанию: пользователь postgres, пароль postgres.");
        System.out.println("Другие настройки задаются через DB_URL, DB_USER, DB_PASSWORD.");
    }
}
