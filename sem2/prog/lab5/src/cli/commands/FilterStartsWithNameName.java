package cli.commands;

import cli.Terminal;
import models.TicketManager;
import utils.Command;
import utils.CommandException;
import java.util.Objects;
import models.Ticket;
import java.util.Collection;

/**
 * Команда для фильтрации билетов по началу имени.
 * Выводит все элементы коллекции, имя которых начинается с заданной подстроки.
 */
public class FilterStartsWithNameName extends Command {
    private final Terminal terminal;
    private final TicketManager ticketManager;

    /**
     * Конструктор команды фильтрации по имени.
     *
     * @param terminal терминал для ввода/вывода
     * @param ticketManager менеджер коллекции билетов
     * @throws NullPointerException если terminal или ticketManager равен null
     */
    public FilterStartsWithNameName(Terminal terminal, TicketManager ticketManager) {
        super("filter_starts_with_name", 
             "вывести элементы, имя которых начинается с заданной подстроки");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.ticketManager = Objects.requireNonNull(ticketManager, "Менеджер билетов не может быть null");
    }

    /**
     * Выполняет фильтрацию билетов по началу имени.
     *
     * @param args аргументы команды (подстрока для поиска)
     * @throws CommandException если не указана подстрока для поиска
     */
    @Override
    public void execute(String[] args) throws CommandException {
        if (args == null || args.length == 0) {
            throw new CommandException("Не указана подстрока для поиска");
        }

        String searchString = args[0].trim();
        if (searchString.isEmpty()) {
            throw new CommandException("Подстрока для поиска не может быть пустой");
        }

        Collection<Ticket> result = ticketManager.filterStartsWithName(searchString);
        if (result.isEmpty()) {
            terminal.println("Билетов с именем, начинающимся на '" + searchString + "', не найдено");
        } else {
            terminal.println("Найденные билеты:\n" + result);
        }
    }
}