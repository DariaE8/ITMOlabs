package client.cmd;

import java.util.Objects;

import client.ConnectionHandler;
import utils.Terminal;
import utils.Command;

import utils.CommandException;
import utils.RequestException;
import utils.Response;

/**
 * Команда для очистки коллекции билетов.
 * Удаляет все элементы из текущей коллекции.
 */
public class Clear extends Command {
    private final Terminal terminal;
    private final ConnectionHandler chandler;
    /**
     * Конструктор команды Clear.
     *
     * @param terminal терминал для вывода результатов
     * @throws NullPointerException если terminal или ticketManager равен null
     */
    public Clear(Terminal terminal, ConnectionHandler chandler) {
        super("clear", "Очистить коллекцию");
        this.chandler = Objects.requireNonNull(chandler, "Обработчик запросов не может быть null");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
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
            Response resp = chandler.request(new server_cmd.Clear());
            if (resp.isError()) {
                throw new CommandException(resp.getMessage());
            }
            terminal.println(resp.getMessage());
        } catch (RequestException e) {
            throw new CommandException("Ошибка сервера: " + e.getMessage());
        } catch (Exception e) {
            throw new CommandException("Ошибка при очистке коллекции: " + e.getMessage());
        }
    }
}