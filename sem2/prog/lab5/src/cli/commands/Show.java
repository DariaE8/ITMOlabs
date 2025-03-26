package cli.commands;

import cli.Terminal;
import models.Ticket;
import models.TicketManager;
import utils.Command;
import java.util.Objects;
import java.util.Collection;

/**
 * Команда для отображения всех элементов коллекции.
 * Выводит полную информацию о каждом билете в удобочитаемом формате.
 */
public class Show extends Command {
    private final Terminal terminal;
    private final TicketManager ticketManager;
    private static final String EMPTY_MSG = "Коллекция билетов пуста";
    private static final String HEADER = "\nСписок всех билетов (%d):\n----------------------------------";

    /**
     * Конструктор команды отображения.
     *
     * @param terminal терминал для вывода информации
     * @param ticketManager менеджер коллекции билетов
     * @throws NullPointerException если terminal или ticketManager равен null
     */
    public Show(Terminal terminal, TicketManager ticketManager) {
        super("show", "вывести все элементы коллекции с подробной информацией");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.ticketManager = Objects.requireNonNull(ticketManager, "Менеджер билетов не может быть null");
    }

    /**
     * Выводит все элементы коллекции в стандартный поток вывода.
     *
     * @param args аргументы команды (игнорируются)
     */
    @Override
    public void execute(String[] args) {
        Collection<Ticket> tickets = ticketManager.getAllTickets();
        
        if (tickets.isEmpty()) {
            terminal.println(EMPTY_MSG);
            return;
        }

        printTickets(tickets);
    }

    /**
     * Форматированный вывод билетов.
     */
    private void printTickets(Collection<Ticket> tickets) {
        terminal.println(String.format(HEADER, tickets.size()));
        
        tickets.forEach(ticket -> {
            terminal.println(ticket.toString());
            terminal.println("----------------------------------");
        });
    }
}