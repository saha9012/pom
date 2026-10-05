package ru.webstudio.model;

/**
 * Заявка клиента веб-студии.
 *
 * Поля private показывают инкапсуляцию: изменить данные можно
 * только через методы класса, а не напрямую.
 */
public class ClientRequest {
    private int id;
    private String clientName;
    private String projectDescription;
    private String status;

    /**
     * Первый конструктор нужен при создании новой заявки.
     * Статус автоматически будет "Новая".
     */
    public ClientRequest(String clientName, String projectDescription) {
        this(0, clientName, projectDescription, "Новая");
    }

    /**
     * Второй конструктор имеет другое количество параметров.
     * Это перегрузка: методы называются одинаково, но принимают разные параметры.
     */
    public ClientRequest(int id, String clientName, String projectDescription, String status) {
        this.id = id;
        this.clientName = clientName;
        this.projectDescription = projectDescription;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Этот метод переопределяет метод toString из класса Object.
     * Переопределение меняет поведение унаследованного метода.
     */
    @Override
    public String toString() {
        return id + ". Клиент: " + clientName
                + " | Проект: " + projectDescription
                + " | Статус: " + status;
    }
}
