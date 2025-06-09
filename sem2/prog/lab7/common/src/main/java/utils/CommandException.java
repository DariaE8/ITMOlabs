package utils;

/**
 * Исключение, выбрасываемое при ошибках выполнения команд.
 */
public class CommandException extends Exception {
    public CommandException(String message) {
        super(message);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
}