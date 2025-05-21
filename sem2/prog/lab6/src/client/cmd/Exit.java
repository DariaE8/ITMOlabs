package client.cmd;

import java.util.Objects;

import client.ConnectionHandler;
import client.Terminal;
import utils.Command;
import utils.CommandException;

/**
 * Команда для завершения работы приложения.
 * Завершает программу с кодом возврата 0 (успешное завершение).
 * Внимание: не сохраняет данные перед выходом!
 */
public class Exit extends Command {
    private final Terminal terminal;
    private final ConnectionHandler chandler;
    /**
     * Конструктор команды Exit.
     */
    public Exit(Terminal terminal, ConnectionHandler chandler) {
        super("exit", "завершить программу");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.chandler = Objects.requireNonNull(chandler, "Обработчик запросов не может быть null");
    }

    /**
     * Выполняет завершение работы приложения.
     * 
     * @param args аргументы команды (игнорируются)
     * @throws CommandException никогда не выбрасывается (переопределено для совместимости)
     */
    @Override
    public void execute(String[] args) throws CommandException {
        terminal.println("[i] Завершение работы клиента...");
        try {
            chandler.close();
        } catch (Exception e) {
            throw new CommandException("[!] Не удалось закрыть соединение: " + e.getMessage());
        }
        System.exit(0);
    }
}