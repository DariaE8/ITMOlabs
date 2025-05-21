package client.cmd;

import client.ConnectionHandler;
import client.Terminal;
import client.prompts.VenuePrompt;
import models.Venue;
import utils.Command;
import java.util.Objects;

import utils.CommandException;
import utils.RequestException;
import utils.Response;

/**
 * Команда для подсчета билетов по указанному месту проведения (venue).
 * Выводит количество элементов коллекции, у которых поле venue равно заданному значению.
 */
public class CountByVenue extends Command {
    private final Terminal terminal;
    private final ConnectionHandler chandler;

    /**
     * Конструктор команды CountByVenue.
     *
     * @param terminal терминал для ввода/вывода
     * @param ticketManager менеджер коллекции билетов
     * @throws NullPointerException если terminal или ticketManager равен null
     */
    public CountByVenue(Terminal terminal, ConnectionHandler chandler) {
        super("count_by_venue", 
             "вывести количество элементов, значение поля venue которых равно заданному");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.chandler = Objects.requireNonNull(chandler, "Обработчик запросов не может быть null");
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
            Response resp = chandler.request(new server.cmd.CountByVenue(venue));
            if(resp.isError()){
                throw new CommandException(resp.getMessage());
            }
            terminal.println(resp.getMessage());
        } catch (RequestException e) {
            throw new CommandException("Ошибка сервера: " + e.getMessage());
        } catch (Exception e) {
            throw new CommandException(
                "Ошибка при подсчете по месту проведения: " + e.getMessage(), 
                e);
        }
    }
   
}