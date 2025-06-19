package client.cmd;

import client.ConnectionHandler;
import utils.Terminal;
import models.Ticket;
import utils.Command;
import utils.CommandException;
import utils.RequestException;
import utils.Response;

import java.util.Objects;
import java.util.Collection;

/**
 * Команда для отображения всех элементов коллекции.
 * Выводит полную информацию о каждом билете в удобочитаемом формате.
 */
public class Show extends Command {
    private Terminal terminal;
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
    public Show(Terminal terminal, ConnectionHandler chandler) {
        super("show", "вывести все элементы коллекции с подробной информацией");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.chandler = Objects.requireNonNull(chandler, "Обработчик запросов не может быть null");
    }

    public Show(ConnectionHandler chandler) {
        super("show", "вывести все элементы коллекции с подробной информацией");
        this.chandler = Objects.requireNonNull(chandler, "Обработчик запросов не может быть null");
    }

    /**
     * Выводит все элементы коллекции в стандартный поток вывода.
     *
     * @param args аргументы команды (игнорируются)
     */
    @Override
    public void execute(String[] args) throws CommandException{
        try{
            Response resp = chandler.request(new server_cmd.Show());
            if (resp.isError()) {
                throw new CommandException(resp.getMessage());
            }

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

    public Collection<Ticket> execute_gui() throws CommandException{
        try{
            Response resp = chandler.request(new server_cmd.Show());
            if (resp.isError()) {
                throw new CommandException(resp.getMessage());
            }

            Collection<Ticket> tickets = (Collection<Ticket>) resp.getData();

            return tickets;
        } catch (RequestException e) {
            throw new CommandException("Ошибка сервера show: " + e.getMessage());
        }
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