# Консольное приложение «Веб-студия»

Учебный проект на Java 8. Приложение управляет заявками клиентов веб-студии
и сохраняет их в PostgreSQL через JDBC.

## Возможности

- полный CRUD: создание, чтение, изменение и удаление заявок;
- модель с конструкторами, getters и setters;
- PostgreSQL-репозиторий;
- сервисный слой с проверкой данных;
- enum для статусов и собственные exceptions;
- прямая и обратная сортировка по ID, имени и статусу;
- фильтрация по имени, описанию и статусу;
- экспорт в `client_requests.csv`;
- консольное меню для всех операций;
- прямое JDBC-подключение и подключение через абстрактный класс.

## Подготовка PostgreSQL

Установите и запустите PostgreSQL, затем создайте базу:

```sql
CREATE DATABASE webstudio;
```

Значения по умолчанию:

```text
URL:      jdbc:postgresql://localhost:5432/webstudio
User:     postgres
Password: postgres
```

Другие значения можно передать через переменные окружения `DB_URL`,
`DB_USER` и `DB_PASSWORD`. Таблица `client_requests` создаётся автоматически.

## Запуск

Откройте `src/main/java/ru/webstudio/Main.java` и нажмите **Run**
над методом `main`, либо выполните:

```text
mvn clean compile exec:java
```

## Структура

```text
Main -> ClientRequestService -> RequestRepository
                                  |
                                  -> PostgresRequestRepository -> PostgreSQL
```

- `model` — данные заявки и enum;
- `repository` — SQL и JDBC;
- `service` — CRUD, проверка, сортировка и фильтрация;
- `export` — создание CSV;
- `exception` — собственные ошибки;
- `Main` — взаимодействие с пользователем.

Подсказка для рассказа о проекте находится в `DEFENSE.md`.
