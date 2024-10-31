package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LogWriter {
    private String filePath;

    public LogWriter(String filePath) {
        this.filePath = filePath;

        Path path = Paths.get(filePath).getParent();
        if (path != null) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                System.out.println("Ошибка создания директории для логов: " + e.getMessage());
            }
        }
    }

    public void logGameStart(List<Player> players) {
        StringBuilder logEntry = new StringBuilder();
        logEntry.append("Игра началась - ").append(getCurrentTime()).append("\n");
        for (Player player : players) {
            logEntry.append("Игрок ").append(player.getName()).append(" с костями: ").append(player.getDice()).append("\n");
        }
        logEntry.append("\n");
        writeLog(logEntry.toString());
    }

    public void logRoundEnd(Player roundLoser) {
        String logEntry = "Раунд завершен - " + getCurrentTime() + "\nПроигравший: " + roundLoser.getName() + "\n\n";
        writeLog(logEntry);
    }

    public void logGameEnd(Player winner) {
        String logEntry = "Игра завершена - " + getCurrentTime() + "\nПобедитель: " + winner.getName() + "\n\n";
        writeLog(logEntry);
    }

    private void writeLog(String logEntry) {
        try (FileWriter fileWriter = new FileWriter(filePath, true)) {
            fileWriter.write(logEntry);
            System.out.println("Запись добавлена в лог: " + logEntry);
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл лога: " + e.getMessage());
        }
    }

    private String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
