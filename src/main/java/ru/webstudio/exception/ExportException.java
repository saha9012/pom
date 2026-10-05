package ru.webstudio.exception;

/**
 * Ошибка записи файла экспорта.
 */
public class ExportException extends RuntimeException {
    public ExportException(String message, Throwable cause) {
        super(message, cause);
    }
}
