package ru.webstudio.model;

import ru.webstudio.model.enums.RequestStatus;

/**
 * Заявка клиента веб-студии.
 *
 * Поля private — это инкапсуляция. Доступ к ним идёт через
 * getters и setters.
 */
public class ClientRequest {
    private int id;
    private String clientName;
    private String projectDescription;
    private RequestStatus status;

    public ClientRequest(String clientName, String projectDescription) {
        this(0, clientName, projectDescription, RequestStatus.NEW);
    }

    /**
     * Два конструктора с разными параметрами — пример перегрузки.
     */
    public ClientRequest(int id, String clientName, String projectDescription,
                         RequestStatus status) {
        this.id = id;
        this.clientName = clientName;
        this.projectDescription = projectDescription;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    /**
     * Переопределяем стандартный метод Object.toString().
     */
    @Override
    public String toString() {
        return id + ". Клиент: " + clientName
                + " | Проект: " + projectDescription
                + " | Статус: " + status.getDisplayName();
    }
}
