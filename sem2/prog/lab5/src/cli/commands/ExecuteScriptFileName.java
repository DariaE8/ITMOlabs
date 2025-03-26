package cli.commands;

import cli.CommandManager;
import cli.Terminal;
import utils.Command;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import utils.CommandException;
/**
 * Команда для выполнения скрипта из файла.
 * Читает команды из указанного файла и выполняет их последовательно.
 * Защищена от рекурсии и циклических вызовов.
 */
public class ExecuteScriptFileName extends Command {
    private static final int MAX_RECURSION_DEPTH = 10;
    private static final long MAX_SCRIPT_SIZE_KB = 1024; // 1MB max script size
    
    private final Set<String> executingScripts = new HashSet<>();
    private int currentRecursionDepth = 0;
    
    private final Terminal terminal;
    private final CommandManager commandManager;

    /**
     * Конструктор команды выполнения скрипта.
     *
     * @param terminal терминал для ввода/вывода
     * @param commandManager менеджер команд для выполнения
     * @throws NullPointerException если terminal или commandManager равен null
     */
    public ExecuteScriptFileName(Terminal terminal, CommandManager commandManager) {
        super("execute_script", "выполнить команды из указанного файла");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.commandManager = Objects.requireNonNull(commandManager, "Менеджер команд не может быть null");
    }

    /**
     * Выполняет команды из указанного файла.
     *
     * @param args аргументы команды (имя файла скрипта)
     * @throws CommandException если произошла ошибка выполнения скрипта
     */
    @Override
    public void execute(String[] args) throws CommandException {
        if (args == null || args.length == 0) {
            throw new CommandException("Не указано имя файла скрипта");
        }

        String filename = args[0].trim();
        
        try {
            validateScriptFile(filename);
            executeScript(filename);
        } catch (ScriptExecutionException e) {
            throw new CommandException(e.getMessage());
        }
    }

    /**
     * Проверяет возможность выполнения скрипта.
     *
     * @param filename имя файла скрипта
     * @throws ScriptExecutionException если файл не может быть выполнен
     */
    private void validateScriptFile(String filename) throws ScriptExecutionException {
        if (currentRecursionDepth >= MAX_RECURSION_DEPTH) {
            throw new ScriptExecutionException(
                "Превышена максимальная глубина рекурсии (" + MAX_RECURSION_DEPTH + ")");
        }

        if (executingScripts.contains(filename)) {
            throw new ScriptExecutionException(
                "Обнаружена циклическая зависимость! Скрипт " + filename + " уже выполняется");
        }

        File file = new File(filename);
        validateFileProperties(file);
    }

    /**
     * Проверяет свойства файла скрипта.
     *
     * @param file файл скрипта
     * @throws ScriptExecutionException если файл не соответствует требованиям
     */
    private void validateFileProperties(File file) throws ScriptExecutionException {
        if (!file.exists()) {
            throw new ScriptExecutionException("Файл скрипта не найден");
        }
        
        if (!file.isFile()) {
            throw new ScriptExecutionException("Указанный путь не является файлом");
        }
        
        if (!file.canRead()) {
            throw new ScriptExecutionException("Нет прав на чтение файла");
        }
        
        if (file.length() > MAX_SCRIPT_SIZE_KB * 1024) {
            throw new ScriptExecutionException(
                "Файл слишком большой (максимум " + MAX_SCRIPT_SIZE_KB + " KB)");
        }
    }

    /**
     * Выполняет команды из файла скрипта.
     *
     * @param filename имя файла скрипта
     * @throws ScriptExecutionException если произошла ошибка при выполнении
     */
    private void executeScript(String filename) throws ScriptExecutionException {
        executingScripts.add(filename);
        currentRecursionDepth++;
        
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            terminal.selectFileScanner(fileScanner);
            
            while (terminal.isCanReadln()) {
                processScriptLine(terminal.readln());
            }
        } catch (FileNotFoundException e) {
            throw new ScriptExecutionException("Файл скрипта не найден: " + filename);
        } catch (Exception e) {
            throw new ScriptExecutionException("Ошибка чтения файла: " + e.getMessage());
        } finally {
            cleanUpAfterExecution(filename);
        }
    }

    /**
     * Обрабатывает строку скрипта.
     *
     * @param line строка из файла скрипта
     */
    private void processScriptLine(String line) {
        String command = line.trim();
        if (command.isEmpty() || command.startsWith("#")) {
            return; // Пропускаем пустые строки и комментарии
        }
        
        try {
            String[] parts = command.split(" ", 2);
            String cmd = parts[0];
            String[] args = parts.length > 1 ? parts[1].split(" ") : new String[0];
            
            if ("execute_script".equalsIgnoreCase(cmd)) {
                terminal.printWarning("Вложенные скрипты запрещены. Команда пропущена.");
                return;
            }
            
            commandManager.executeCommand(cmd, args);
        } catch (Exception e) {
            terminal.printError("Ошибка выполнения команды: " + e.getMessage());
        }
    }

    /**
     * Очищает ресурсы после выполнения скрипта.
     *
     * @param filename имя выполненного файла скрипта
     */
    private void cleanUpAfterExecution(String filename) {
        terminal.selectConsoleScanner();
        executingScripts.remove(filename);
        currentRecursionDepth--;
    }

    /**
     * Возвращает подробное описание команды.
     *
     * @return строка с описанием команды
     */
    @Override
    public String getDescription() {
        return "Выполняет команды из указанного файла.\n"
             + "Ограничения:\n"
             + "- Максимальный размер файла: " + MAX_SCRIPT_SIZE_KB + " KB\n"
             + "- Максимальная глубина рекурсии: " + MAX_RECURSION_DEPTH + "\n"
             + "- Вложенные execute_script игнорируются";
    }

    /**
     * Исключение при выполнении скрипта.
     */
    private static class ScriptExecutionException extends Exception {
        public ScriptExecutionException(String message) {
            super(message);
        }
    }
}