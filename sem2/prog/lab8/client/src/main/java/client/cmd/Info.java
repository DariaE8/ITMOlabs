package client.cmd;

import client.ConnectionHandler;
import utils.Terminal;
import utils.Command;
import utils.CommandException;
import utils.RequestException;
import utils.Response;

import java.util.Objects;

/**
 * Команда для вывода информации о коллекции билетов.
 * Отображает тип коллекции, дату инициализации, количество элементов и другую статистику.
 */
public class Info extends Command {
    private Terminal terminal;
    private ConnectionHandler chandler;

    /**
     * Конструктор команды информации.
     *
     * @param terminal терминал для вывода информации
     * @param chandler обработчик соединения с сервером
     * @throws NullPointerException если terminal или chandler равен null
     */
    public Info(Terminal terminal, ConnectionHandler chandler) {
        super("info", "вывести информацию о коллекции (тип, размер, дата инициализации)");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.chandler = Objects.requireNonNull(chandler, "Обработчик соединения не может быть null");
    }
    
    /**
     * Выводит подробную информацию о коллекции билетов.
     *
     * @param args аргументы команды (игнорируются)
     * @throws CommandException если произошла ошибка при выполнении команды
     */
    @Override
    public void execute(String[] args) throws CommandException {
        try {
            Response resp = chandler.request(new server_cmd.Info());
            if (resp.isError()) {
                throw new CommandException(resp.getMessage());
            }
            terminal.println(resp.getMessage());
        } catch (RequestException e) {
            throw new CommandException("Ошибка сервера: " + e.getMessage());
        } catch (Exception e) {
            throw new CommandException(e.getMessage());
        }
    }
}