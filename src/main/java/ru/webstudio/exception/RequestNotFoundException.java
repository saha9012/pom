package ru.webstudio.exception;

/**
 * Ошибка поиска несуществующей заявки.
 */
public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(int id) {
        super("Заявка с ID " + id + " не найдена");
    }
}
