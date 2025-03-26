package cli.commands;

import cli.Terminal;
import models.Ticket;
import models.TicketManager;
import utils.Command;
import java.util.Objects;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда для вывода элементов коллекции в порядке возрастания.
 * Сортирует билеты по их естественному порядку (определяется в классе Ticket)
 * и выводит их в терминал.
 */
public class PrintAscending extends Command {
    private final Terminal terminal;
    private final TicketManager ticketManager;

    /**
     * Конструктор команды сортировки.
     *
     * @param terminal терминал для вывода информации
     * @param ticketManager менеджер коллекции билетов
     * @throws NullPointerException если terminal или ticketManager равен null
     */
    public PrintAscending(Terminal terminal, TicketManager ticketManager) {
        super("print_ascending", 
             "вывести элементы коллекции в порядке возрастания (по ID)");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.ticketManager = Objects.requireNonNull(ticketManager, "Менеджер билетов не может быть null");
    }

    /**
     * Выводит элементы коллекции в отсортированном порядке.
     *
     * @param args аргументы команды (игнорируются)
     */
    @Override
    public void execute(String[] args) {
        List<Ticket> sortedTickets = getSortedTickets();
        
        if (sortedTickets.isEmpty()) {
            terminal.println("Коллекция билетов пуста");
            return;
        }

        printTickets(sortedTickets);
    }

    /**
     * Возвращает отсортированную коллекцию билетов.
     */
    private List<Ticket> getSortedTickets() {
        return ticketManager.getAllTickets().stream()
            .sorted(Comparator.comparingLong(Ticket::getId))
            .collect(Collectors.toList());
    }

    /**
     * Выводит билеты в терминал.
     */
    private void printTickets(List<Ticket> tickets) {
        terminal.println("\nБилеты в порядке возрастания (по ID):");
        terminal.println("----------------------------------");
        
        tickets.forEach(ticket -> 
            terminal.println(ticket.toString())
        );
    }
}