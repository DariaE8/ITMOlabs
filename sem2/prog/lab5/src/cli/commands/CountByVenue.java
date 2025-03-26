package cli.commands;

import cli.Terminal;
import cli.prompts.VenuePrompt;
import models.TicketManager;
import models.Venue;
import utils.Command;
import java.util.Objects;

import utils.CommandException;

/**
 * Команда для подсчета билетов по указанному месту проведения (venue).
 * Выводит количество элементов коллекции, у которых поле venue равно заданному значению.
 */
public class CountByVenue extends Command {
    private final TicketManager ticketManager;
    private final Terminal terminal;

    /**
     * Конструктор команды CountByVenue.
     *
     * @param terminal терминал для ввода/вывода
     * @param ticketManager менеджер коллекции билетов
     * @throws NullPointerException если terminal или ticketManager равен null
     */
    public CountByVenue(Terminal terminal, TicketManager ticketManager) {
        super("count_by_venue", 
             "вывести количество элементов, значение поля venue которых равно заданному");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.ticketManager = Objects.requireNonNull(ticketManager, "Менеджер билетов не может быть null");
    }

    /**
     * Выполняет подсчет билетов по месту проведения.
     *
     * @param args аргументы команды (не используются)
     * @throws CommandException если произошла ошибка при выполнении команды
     */
    @Override
    public void execute(String[] args) throws CommandException {
        try {
            VenuePrompt venuePrompt = new VenuePrompt(terminal);
            Venue venue = venuePrompt.ask();
            long count = ticketManager.countByVenue(venue);
            
            terminal.println(String.format(
                "Найдено билетов с указанным местом проведения: %d", 
                count));
                
        } catch (Exception e) {
            throw new CommandException(
                "Ошибка при подсчете по месту проведения: " + e.getMessage(), 
                e);
        }
    }
}