package cli.commands;

import cli.Terminal;
import models.TicketManager;
import utils.Command;
import java.util.Objects;

import utils.CommandException;

/**
 * Команда для очистки коллекции билетов.
 * Удаляет все элементы из текущей коллекции.
 */
public class Clear extends Command {
    private final Terminal terminal;
    private final TicketManager ticketManager;

    /**
     * Конструктор команды Clear.
     *
     * @param terminal терминал для вывода результатов
     * @param ticketManager менеджер коллекции билетов
     * @throws NullPointerException если terminal или ticketManager равен null
     */
    public Clear(Terminal terminal, TicketManager ticketManager) {
        super("clear", "очистить коллекцию");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.ticketManager = Objects.requireNonNull(ticketManager, "Менеджер билетов не может быть null");
    }

    /**
     * Выполняет очистку коллекции билетов.
     *
     * @param args аргументы команды (не используются)
     * @throws CommandException если произошла ошибка при очистке коллекции
     */
    @Override
    public void execute(String[] args) throws CommandException {
        try {
            int count = ticketManager.size();
            ticketManager.clear();
            terminal.println("Коллекция успешно очищена. Удалено элементов: " + count);
        } catch (Exception e) {
            throw new CommandException("Ошибка при очистке коллекции: " + e.getMessage());
        }
    }
}