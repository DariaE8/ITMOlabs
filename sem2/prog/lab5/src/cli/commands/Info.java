package cli.commands;
import cli.Terminal;
import models.TicketManager;
import utils.Command;
import java.util.Objects;

/**
 * Команда для вывода информации о коллекции билетов.
 * Отображает тип коллекции, дату инициализации, количество элементов и другую статистику.
 */
public class Info extends Command {
    private Terminal terminal;
    private TicketManager ticketManager;

    
    /**
     * Конструктор команды информации.
     *
     * @param terminal терминал для вывода информации
     * @param ticketManager менеджер коллекции билетов
     * @throws NullPointerException если terminal или ticketManager равен null
     */
    public Info(Terminal terminal, TicketManager ticketManager) {
        super("info", "вывести информацию о коллекции (тип, размер, дата инициализации)");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.ticketManager = Objects.requireNonNull(ticketManager, "Менеджер билетов не может быть null");
    }
    
    /**
    * Выводит подробную информацию о коллекции билетов.
    *
    * @param args аргументы команды (игнорируются)
     */
    @Override
    public void execute(String[] args) {
        terminal.println("Тип коллекции: " + ticketManager.getCollectionType()  + ", Размер: " + ticketManager.size());
    }
} 