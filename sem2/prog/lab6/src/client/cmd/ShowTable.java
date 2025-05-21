package client.cmd;

import client.ConnectionHandler;
import client.Terminal;
import utils.Command;
import utils.CommandException;
import utils.RequestException;
import utils.Response;

import java.util.Objects;
import java.util.List;

/**
 * Команда для отображения всех элементов коллекции в виде таблицы.
 * Выводит данные в удобном табличном формате с выравниванием колонок.
 */
public class ShowTable extends Command {
    private final Terminal terminal;
    private final ConnectionHandler chandler;
    private static final String EMPTY_MSG = "Коллекция билетов пуста";

    /**
     * Конструктор команды отображения таблицы.
     *
     * @param terminal терминал для вывода информации
     * @param ticketManager менеджер коллекции билетов
     * @throws NullPointerException если terminal или ticketManager равен null
     */
    public ShowTable(Terminal terminal, ConnectionHandler chandler) {
        super("show_table", "вывести все элементы коллекции в виде форматированной таблицы");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.chandler = Objects.requireNonNull(chandler, "Обработчик запросов не может быть null");
    }

    /**
     * Выводит все элементы коллекции в виде таблицы.
     *
     * @param args аргументы команды (игнорируются)
     */
    @Override
    public void execute(String[] args) throws CommandException {
        try {
            Response resp = chandler.request(new server.cmd.ShowTable());
            
            List<String> tableData = (List<String>) resp.getData();
            
            if (tableData.size() <= 1) { // Только заголовок или пусто
                terminal.println(EMPTY_MSG);
                return;
            }

            terminal.printTable(tableData);
        } catch (RequestException e) {
            throw new CommandException("Ошибка сервера: " + e.getMessage());
        }
    }
}