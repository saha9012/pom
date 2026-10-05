package ru.webstudio.export;

import ru.webstudio.exception.ExportException;
import ru.webstudio.model.ClientRequest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Экспорт заявок в CSV, который можно открыть в Excel.
 */
public class CsvExporter {

    public void export(List<ClientRequest> requests, Path file) {
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            // BOM помогает Excel правильно определить русскую кодировку UTF-8.
            writer.write('\uFEFF');
            writer.write("ID;Клиент;Описание проекта;Статус");
            writer.newLine();

            for (ClientRequest request : requests) {
                writer.write(String.valueOf(request.getId()));
                writer.write(';');
                writer.write(escape(request.getClientName()));
                writer.write(';');
                writer.write(escape(request.getProjectDescription()));
                writer.write(';');
                writer.write(escape(request.getStatus().getDisplayName()));
                writer.newLine();
            }
        } catch (IOException exception) {
            throw new ExportException("Не удалось создать CSV: " + file, exception);
        }
    }

    private String escape(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
