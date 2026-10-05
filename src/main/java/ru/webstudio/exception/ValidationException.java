package ru.webstudio.exception;

/**
 * Ошибка неправильных данных, введённых пользователем.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
