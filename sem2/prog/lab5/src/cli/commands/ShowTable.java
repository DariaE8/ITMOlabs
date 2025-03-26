package cli.commands;

import cli.Terminal;
import models.TicketManager;
import utils.Command;
import java.util.Objects;
import java.util.List;

/**
 * Команда для отображения всех элементов коллекции в виде таблицы.
 * Выводит данные в удобном табличном формате с выравниванием колонок.
 */
public class ShowTable extends Command {
    private final Terminal terminal;
    private final TicketManager ticketManager;
    private static final String EMPTY_MSG = "Коллекция билетов пуста";

    /**
     * Конструктор команды отображения таблицы.
     *
     * @param terminal терминал для вывода информации
     * @param ticketManager менеджер коллекции билетов
     * @throws NullPointerException если terminal или ticketManager равен null
     */
    public ShowTable(Terminal terminal, TicketManager ticketManager) {
        super("show_table", "вывести все элементы коллекции в виде форматированной таблицы");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.ticketManager = Objects.requireNonNull(ticketManager, "Менеджер билетов не может быть null");
    }

    /**
     * Выводит все элементы коллекции в виде таблицы.
     *
     * @param args аргументы команды (игнорируются)
     */
    @Override
    public void execute(String[] args) {
        List<String> tableData = ticketManager.toCSV();
        
        if (tableData.size() <= 1) { // Только заголовок или пусто
            terminal.println(EMPTY_MSG);
            return;
        }

        terminal.printTable(tableData);
    }
}