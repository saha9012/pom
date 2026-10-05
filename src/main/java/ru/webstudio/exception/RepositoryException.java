package ru.webstudio.exception;

/**
 * Понятная для приложения обёртка над технической ошибкой JDBC.
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
