package cli.commands;

import cli.Terminal;
import cli.prompts.TicketPrompt;
import utils.AbstractPrompt;
import models.Ticket;
import models.TicketManager;
import utils.Command;
import utils.CommandException;

import java.util.Objects;

/**
 * Команда для добавления нового элемента в коллекцию.
 * Создает новый билет и добавляет его в коллекцию по указанному ключу.
 */
public class Insert extends Command {
    private final Terminal terminal;
    private final TicketManager ticketManager;
    private static final String KEY_FORMAT_ERROR = 
        "Ключ должен быть целым положительным числом";
    private static final String CANCELLED_MESSAGE = 
        "Создание билета отменено пользователем";

    /**
     * Конструктор команды вставки.
     */
    public Insert(Terminal terminal, TicketManager ticketManager) {
        super("insert", "добавить новый элемент с указанным ключом");
        this.terminal = Objects.requireNonNull(terminal);
        this.ticketManager = Objects.requireNonNull(ticketManager);
    }

    /**
     * Выполняет добавление нового билета в коллекцию.
     */
    @Override
    public void execute(String[] args) throws CommandException{
        try {
            validateArguments(args);
            int key = parseKey(args[0]);
            Ticket ticket = createNewTicket();
            
            ticketManager.insert(key, ticket);
            terminal.println(String.format(
                "Билет успешно добавлен с ключом %d. Всего билетов: %d",
                key, ticketManager.size()));
                
        } catch (AbstractPrompt.InputCancelledException e) {
            if(terminal.checkScanner()) {
                terminal.printWarning(CANCELLED_MESSAGE);
            } else {
                throw new CommandException(e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            terminal.printError(e.getMessage());
        } catch (Exception e) {
            terminal.printError("Системная ошибка: " + e.getMessage());
        }
    }

    private void validateArguments(String[] args) {
        if (args == null || args.length < 1) {
            throw new IllegalArgumentException("Не указан ключ для вставки");
        }
    }

    private int parseKey(String keyStr) {
        try {
            int key = Integer.parseInt(keyStr);
            if (key <= 0) throw new IllegalArgumentException(KEY_FORMAT_ERROR);
            return key;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(KEY_FORMAT_ERROR, e);
        }
    }

    private Ticket createNewTicket() throws AbstractPrompt.InputCancelledException {
        if (terminal.checkScanner()){
            terminal.println("\nСоздание нового билета (введите 'отмена' для отмены):");
        }
        TicketPrompt ticketPrompt = new TicketPrompt(terminal);
        return ticketPrompt.ask();
    }
}