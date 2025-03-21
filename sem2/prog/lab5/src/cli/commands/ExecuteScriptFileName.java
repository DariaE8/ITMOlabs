package cli.commands;

import models.Command;
import cli.CLI;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ExecuteScriptFileName extends Command {
    private static final int MAX_RECURSION_DEPTH = 10;
    private final Set<String> executingScripts = new HashSet<>();
    private int currentRecursionDepth = 0;
    private final CLI cli; // Ссылка на CLI для обработки команд

    public ExecuteScriptFileName(CLI cli) {
        super("execute_script", "выполнить команды из файла");
        this.cli = cli;
    }

    @Override
    public void run(String[] args) {
        if (args.length == 0) {
            terminal.printError("Ошибка: необходимо указать имя файла.");
            return;
        }

        String filename = args[0];

        if (currentRecursionDepth >= MAX_RECURSION_DEPTH) {
            terminal.printError("Ошибка: превышена максимальная глубина рекурсии (" + MAX_RECURSION_DEPTH + ").");
            return;
        }

        if (!checkFileExist(filename)) {
            return;
        }

        if (executingScripts.contains(filename)) {
            terminal.printError("Ошибка: обнаружена рекурсия! Скрипт " + filename + " уже выполняется.");
            return;
        }

        executingScripts.add(filename);
        currentRecursionDepth++;

        try (Scanner fileScanner = new Scanner(new File(filename))) {
            terminal.selectFileScanner(fileScanner);

            while (terminal.isCanReadln()) {
                String command = terminal.readln().trim();
                if (!command.isEmpty()) {
                    String[] parts = command.split(" ", 2);
                    String cmd = parts[0];
                    String arg = parts.length > 1 ? parts[1] : "";
                    cli.processCommand(cmd, arg); // Теперь команды выполняются через CLI
                }
            }
            terminal.selectConsoleScanner();
        } catch (Exception e) {
            terminal.printError("Ошибка: не удалось открыть файл " + filename);
        } finally {
            executingScripts.remove(filename);
            currentRecursionDepth--;
        }
    }

    private boolean checkFileExist(String fname) {
        File file = new File(fname);
        if (!file.exists() || !file.isFile()) {
            terminal.printError("Ошибка: файл " + fname + " не найден.");
            return false;
        }
        return true;
    }
}
