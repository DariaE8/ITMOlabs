package client.cmd;

import client.CommandManager;
import utils.Terminal;
import utils.Command;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import utils.CommandException;

public class ExecuteScriptFileName extends Command {
    private static final int MAX_RECURSION_DEPTH = 10;
    private static final long MAX_SCRIPT_SIZE_KB = 1024;
    
    private final Set<String> executingScripts = new HashSet<>();
    private int currentRecursionDepth = 0;
    
    private final Terminal terminal;
    private final CommandManager commandManager;

    public ExecuteScriptFileName(Terminal terminal, CommandManager commandManager) {
        super("execute_script", "выполнить команды из указанного файла");
        this.terminal = Objects.requireNonNull(terminal);
        this.commandManager = Objects.requireNonNull(commandManager);
    }

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

    private void executeScript(String filename) throws ScriptExecutionException {
        executingScripts.add(filename);
        currentRecursionDepth++;
        
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            terminal.selectFileScanner(fileScanner);
            
            while (terminal.isCanReadln()) {
                String line = terminal.readln().trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                String[] parts = line.split(" ", 2);
                String cmd = parts[0];
                String[] args = parts.length > 1 ? parts[1].split(" ") : new String[0];
                
                if ("execute_script".equalsIgnoreCase(cmd)) {
                    terminal.printWarning("Вложенные скрипты запрещены. Команда пропущена.");
                    continue;
                }
                
                try {
                    commandManager.executeCommand(cmd, args);
                } catch (CommandException e) {
                    // Прерываем выполнение скрипта при первой же ошибке
                    throw new ScriptExecutionException(e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            throw new ScriptExecutionException("Файл скрипта не найден: " + filename);
        } catch (Exception e) {
            throw new ScriptExecutionException(e.getMessage());
        } finally {
            cleanUpAfterExecution(filename);
        }
    }

    private void cleanUpAfterExecution(String filename) {
        terminal.selectConsoleScanner();
        executingScripts.remove(filename);
        currentRecursionDepth--;
    }

    private static class ScriptExecutionException extends Exception {
        public ScriptExecutionException(String message) {
            super(message);
        }
    }
}