package cli;

import models.TicketManager;
import java.io.IOException;

import utils.CommandException;

/**
 * Основной класс командной строки (CLI) приложения.
 * Управляет жизненным циклом приложения, обработкой команд и ошибок.
 */
public class CLI {
    private final Terminal terminal;
    private TicketManager ticketManager;
    private DumpManager dumpManager;
    private CommandManager commandManager;
    private boolean isRunning = true;

    /**
     * Создает новый экземпляр CLI интерфейса.
     */
    public CLI() {
        this.terminal = new Terminal();
    }

    /**
     * Инициализирует приложение с указанными аргументами.
     * 
     * @param args аргументы командной строки
     * @return true если инициализация прошла успешно, false в случае ошибки
     */
    public boolean init(String[] args) {
        try {
            validateArguments(args);
            initializeComponents(args[0]);
            showWelcomeMessage();
            return true;
        } catch (IllegalArgumentException e) {
            terminal.printError("Ошибка аргументов: " + e.getMessage());
        } catch (IOException e) {
            terminal.printError("Ошибка загрузки данных: " + e.getMessage());
        } catch (Exception e) {
            terminal.printError("Критическая ошибка инициализации: " + e.getMessage());
        }
        return false;
    }

    public void run() {
        while (isRunning) {
            try {
                processUserCommand();
            } catch (Exception e) {
                terminal.printError("Ошибка выполнения: " + e.getMessage());
            }
        }
    }

    public void shutdown() {
        this.isRunning = false;
    }

    private void validateArguments(String[] args) throws IllegalArgumentException {
        if (args.length == 0) {
            throw new IllegalArgumentException("Не указан файл данных.");
        }

        // File dataFile = new File(args[0]);
        // if (!dataFile.exists()) {
        //     throw new IllegalArgumentException("Файл данных '" + args[0] + "' не существует");
        // }
        // if (!dataFile.canRead()) {
        //     throw new IllegalArgumentException("Нет прав на чтение файла '" + args[0] + "'");
        // }
    }

    private void initializeComponents(String filename) throws IOException {
        this.dumpManager = new DumpManager(filename, terminal);
        this.ticketManager = new TicketManager(dumpManager.load());
        this.commandManager = new CommandManager();
        commandManager.init(ticketManager, dumpManager, terminal);
    }

    private void showWelcomeMessage() {
        terminal.println("=== Система управления билетами ===");
        terminal.println("Введите 'help' для списка команд");
    }

    private void processUserCommand() {
        terminal.print("Введите команду> ");
        String input = terminal.readln().trim();
        
        if (input.isEmpty()) {
            return;
        }

        String[] parts = input.split(" ", 2);
        String commandName = parts[0];
        String[] arguments = parts.length > 1 ? parseArguments(parts[1]) : new String[0];
        
        try {
            commandManager.executeCommand(commandName, arguments);
        } catch (CommandException e) {
            terminal.printError(e.getMessage());
        }
    }

    private String[] parseArguments(String argumentString) {
        // Улучшенный парсинг аргументов с обработкой кавычек
        return argumentString.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    }
}