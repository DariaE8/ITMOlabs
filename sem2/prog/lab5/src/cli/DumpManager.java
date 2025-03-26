package cli;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Менеджер для работы с файлами данных.
 * Обеспечивает загрузку и сохранение данных в файл с буферизацией.
 */
public class DumpManager {
    private final String filename;
    private final Terminal terminal;

    /**
     * Создает новый экземпляр DumpManager.
     * 
     * @param filename имя файла для работы с данными
     * @param terminal терминал для вывода сообщений
     * @throws NullPointerException если terminal равен null
     */
    public DumpManager(String filename, Terminal terminal) {
        this.filename = Objects.requireNonNull(filename, "Имя файла не может быть null");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
    }

    /**
     * Сохраняет данные в файл.
     * 
     * @param data список строк для сохранения
     * @throws IOException если произошла ошибка ввода-вывода
     * @throws IllegalArgumentException если data равен null
     */
    public void save(List<String> data) throws IOException {
        Objects.requireNonNull(data, "Данные для сохранения не могут быть null");
        
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename))) {
            for (String line : data) {
                bos.write((line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
            }
            terminal.println("Данные успешно сохранены в файл: " + filename);
        } catch (FileNotFoundException e) {
            throw new IOException("Файл не найден: " + filename, e);
        }
    }

    /**
     * Загружает данные из файла.
     * 
     * @return список загруженных строк
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public List<String> load() throws IOException {
        List<String> data = new ArrayList<>();

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename))) {
            StringBuilder currentLine = new StringBuilder();
            int byteRead;
            
            while ((byteRead = bis.read()) != -1) {
                char ch = (char) byteRead;
                
                if (ch == '\n' || ch == '\r') {
                    if (ch == '\n' || currentLine.length() > 0) {
                        data.add(currentLine.toString());
                        currentLine.setLength(0);
                    }
                } else {
                    currentLine.append(ch);
                }
            }
            
            // Добавляем последнюю строку, если файл не заканчивается переводом строки
            if (currentLine.length() > 0) {
                data.add(currentLine.toString());
            }

            terminal.println("Данные успешно загружены из файла: " + filename);
        } catch (FileNotFoundException e) {
            throw new IOException("Файл не найден: " + filename, e);
        }

        return data;
    }

    /**
     * Возвращает имя файла, с которым работает менеджер.
     * 
     * @return имя файла
     */
    public String getFilename() {
        return filename;
    }
}