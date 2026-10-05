package ru.webstudio.model.enums;

/**
 * Enum ограничивает набор допустимых статусов заявки.
 * Случайную строку вместо статуса записать уже нельзя.
 */
public enum RequestStatus {
    NEW("Новая"),
    IN_PROGRESS("В работе"),
    COMPLETED("Завершена"),
    CANCELLED("Отменена");

    private final String displayName;

    RequestStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static RequestStatus fromNumber(int number) {
        switch (number) {
            case 1:
                return NEW;
            case 2:
                return IN_PROGRESS;
            case 3:
                return COMPLETED;
            case 4:
                return CANCELLED;
            default:
                throw new IllegalArgumentException("Статус должен быть от 1 до 4");
        }
    }
}
