package client.cmd;

import utils.Command;
import client.Terminal;
import java.util.Objects;
import java.util.Queue;

/**
 * Команда для вывода истории выполненных команд.
 * Показывает последние выполненные команды (по умолчанию 5).
 */
public class History extends Command {
    private final Queue<String> commandHistory;
    private static final int HISTORY_SIZE = 5;
    private final Terminal terminal;

    /**
     * Конструктор команды истории.
     *
     * @param terminal терминал для вывода информации
     * @param commandHistory очередь с историей команд
     * @throws NullPointerException если terminal или commandHistory равен null
     */
    public History(Terminal terminal, Queue<String> commandHistory) {
        super("history", "вывести историю выполненных команд (последние " + HISTORY_SIZE + ")");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.commandHistory = Objects.requireNonNull(commandHistory, "История команд не может быть null");
    }

    /**
     * Выводит историю выполненных команд.
     *
     * @param args аргументы команды (игнорируются)
     */
    @Override
    public void execute(String[] args) {
        if (commandHistory.isEmpty()) {
            terminal.println("История команд пуста");
        }

        terminal.println("\nИстория выполненных команд:");
        terminal.println("---------------------------");
        
        int counter = 1;
        for (String cmd : commandHistory) {
            terminal.println(String.format("%d. %s", counter++, cmd));
        }
    }
}