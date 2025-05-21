package client;

import java.io.IOException;
import utils.CommandException;

/**
 * Основной класс командной строки (CLI) приложения.
 * Управляет жизненным циклом приложения, обработкой команд и ошибок.
 */
public class Client {
    private final Terminal terminal;
    private CommandManager commandManager;
    private ConnectionHandler connectionHandler;
    private boolean isRunning = true;

    /**
     * Создает новый экземпляр CLI интерфейса.
     */
    public Client() {
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
            initializeComponents();
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

    private void initializeComponents() throws IOException {
        this.commandManager = new CommandManager();
        this.connectionHandler = new ConnectionHandler();
        commandManager.init(terminal, connectionHandler);
        connectionHandler.init();
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
