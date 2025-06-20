package client.cmd;


import client.ConnectionHandler;
import utils.Terminal;
import utils.Command;
import utils.CommandException;
import utils.RequestException;
import utils.Response;

import java.util.Objects;
import models.Ticket;
import java.util.Collection;

/**
 * Команда для фильтрации билетов по началу имени.
 * Выводит все элементы коллекции, имя которых начинается с заданной подстроки.
 */
public class FilterStartsWithNameName extends Command {
    private final Terminal terminal;
    private final ConnectionHandler chandler;

    /**
     * Конструктор команды фильтрации по имени.
     *
     * @param terminal терминал для ввода/вывода
     * @param ticketManager менеджер коллекции билетов
     * @throws NullPointerException если terminal или ticketManager равен null
     */
    public FilterStartsWithNameName(Terminal terminal, ConnectionHandler chandler) {
        super("filter_starts_with_name", 
             "вывести элементы, имя которых начинается с заданной подстроки");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.chandler = Objects.requireNonNull(chandler, "Обработчик запросов не может быть null");
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
        try {
            Response resp = chandler.request(new server_cmd.FilterStartsWithName(searchString));
            if(resp.isError()){
                throw new CommandException(resp.getMessage());
            }
            Collection<Ticket> result = (Collection<Ticket>) resp.getData();
            if (result.isEmpty()) {
                terminal.println("Билетов с именем, начинающимся на '" + searchString + "', не найдено");
            } else {
                terminal.println("Найденные билеты:\n" + result);
            }
        } catch (RequestException e) {
            throw new CommandException("Ошибка сервера: " + e.getMessage());
        } catch (Exception e) {
            throw new CommandException(e.getMessage());
        }
    }
}