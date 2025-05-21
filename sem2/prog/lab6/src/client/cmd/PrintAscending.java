package client.cmd;

import client.ConnectionHandler;
import client.Terminal;
import models.Ticket;
import utils.Command;
import utils.CommandException;
import utils.RequestException;
import utils.Response;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.Comparator;

/**
 * Команда для отображения всех элементов коллекции.
 * Выводит полную информацию о каждом билете в удобочитаемом формате.
 */
public class PrintAscending extends Command {
    private final Terminal terminal;
    private final ConnectionHandler chandler;
    private static final String EMPTY_MSG = "Коллекция билетов пуста";
    private static final String HEADER = "\nСписок всех билетов (%d):\n----------------------------------";
    /**
     * Конструктор команды отображения.
     *
     * @param terminal терминал для вывода информации
     * @param ticketManager менеджер коллекции билетов
     * @throws NullPointerException если terminal или ticketManager равен null
     */
    public PrintAscending(Terminal terminal, ConnectionHandler chandler) {
        super("print_ascending", 
             "вывести элементы коллекции в порядке возрастания (по ID)");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.chandler = Objects.requireNonNull(chandler, "Обработчик запросов не может быть null");
    }

    /**
     * Выводит все элементы коллекции в стандартный поток вывода.
     *
     * @param args аргументы команды (игнорируются)
     */
    @Override
    public void execute(String[] args) throws CommandException {
        try {
        Response resp = chandler.request(new server.cmd.Show());

        Collection<Ticket> tickets = (Collection<Ticket>) resp.getData();
        
        if (tickets.isEmpty()) {
            terminal.println(EMPTY_MSG);
            return;
        }

        printTickets(tickets);
        } catch (RequestException e) {
            throw new CommandException("Ошибка сервера: " + e.getMessage());
        }
    }
    /**
     * Форматированный вывод билетов.
     */
    private void printTickets(Collection<Ticket> tickets) {
        terminal.println(String.format(HEADER, tickets.size()));

        tickets.stream()
            .sorted(Comparator.comparingLong(Ticket::getId))
            .collect(Collectors.toList());

        tickets.forEach(ticket -> {
            terminal.println(ticket.toString());
            terminal.println("----------------------------------");
        });
    }
}