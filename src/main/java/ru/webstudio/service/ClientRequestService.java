package ru.webstudio.service;

import ru.webstudio.exception.RequestNotFoundException;
import ru.webstudio.exception.ValidationException;
import ru.webstudio.model.ClientRequest;
import ru.webstudio.model.enums.RequestSortField;
import ru.webstudio.model.enums.RequestStatus;
import ru.webstudio.model.enums.SortDirection;
import ru.webstudio.repository.RequestRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Сервис содержит бизнес-логику приложения.
 * Консоль не обращается к SQL напрямую, а вызывает этот класс.
 */
public class ClientRequestService {
    private final RequestRepository repository;

    public ClientRequestService(RequestRepository repository) {
        this.repository = repository;
    }

    public ClientRequest create(String clientName, String description) {
        validateText(clientName, description);
        return repository.save(new ClientRequest(clientName.trim(), description.trim()));
    }

    public ClientRequest getById(int id) {
        validateId(id);
        return repository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));
    }

    public List<ClientRequest> getAll() {
        return repository.findAll();
    }

    public ClientRequest update(int id, String clientName, String description,
                                RequestStatus status) {
        validateId(id);
        validateText(clientName, description);
        if (status == null) {
            throw new ValidationException("Статус не указан");
        }

        ClientRequest request = getById(id);
        request.setClientName(clientName.trim());
        request.setProjectDescription(description.trim());
        request.setStatus(status);

        if (!repository.update(request)) {
            throw new RequestNotFoundException(id);
        }
        return request;
    }

    public void delete(int id) {
        validateId(id);
        if (!repository.deleteById(id)) {
            throw new RequestNotFoundException(id);
        }
    }

    /**
     * Прямая и обратная сортировка по ID, имени клиента или статусу.
     */
    public List<ClientRequest> sort(RequestSortField field, SortDirection direction) {
        List<ClientRequest> result = new ArrayList<ClientRequest>(repository.findAll());
        Comparator<ClientRequest> comparator = comparatorFor(field);

        if (direction == SortDirection.DESCENDING) {
            comparator = comparator.reversed();
        }
        result.sort(comparator);
        return result;
    }

    /**
     * Фильтрация по части имени, части описания и статусу.
     * Пустой критерий означает, что по нему фильтровать не нужно.
     */
    public List<ClientRequest> filter(String clientName, String description,
                                      RequestStatus status) {
        String namePart = normalize(clientName);
        String descriptionPart = normalize(description);
        List<ClientRequest> result = new ArrayList<ClientRequest>();

        for (ClientRequest request : repository.findAll()) {
            boolean nameMatches = normalize(request.getClientName()).contains(namePart);
            boolean descriptionMatches =
                    normalize(request.getProjectDescription()).contains(descriptionPart);
            boolean statusMatches = status == null || request.getStatus() == status;

            if (nameMatches && descriptionMatches && statusMatches) {
                result.add(request);
            }
        }
        return result;
    }

    private Comparator<ClientRequest> comparatorFor(RequestSortField field) {
        if (field == null) {
            throw new ValidationException("Поле сортировки не указано");
        }

        switch (field) {
            case CLIENT_NAME:
                return Comparator.comparing(
                        ClientRequest::getClientName, String.CASE_INSENSITIVE_ORDER);
            case STATUS:
                return Comparator.comparing(ClientRequest::getStatus);
            case ID:
            default:
                return Comparator.comparingInt(ClientRequest::getId);
        }
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new ValidationException("ID должен быть положительным числом");
        }
    }

    private void validateText(String clientName, String description) {
        if (clientName == null || clientName.trim().isEmpty()) {
            throw new ValidationException("Имя клиента не должно быть пустым");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new ValidationException("Описание проекта не должно быть пустым");
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}
