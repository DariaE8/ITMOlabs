package cli.commands;

import cli.Terminal;
import cli.prompts.TicketPrompt;
import utils.AbstractPrompt;
import models.Ticket;
import models.TicketManager;
import utils.Command;
import java.util.Objects;

/**
 * Команда для удаления из коллекции всех элементов, превышающих заданный.
 * Сравнение производится по естественному порядку элементов (определяется в классе Ticket).
 */
public class RemoveGreater extends Command {
    private final Terminal terminal;
    private final TicketManager ticketManager;
    private static final String REMOVED_COUNT_MSG = "Удалено элементов: %d. Осталось: %d";
    private static final String CANCELLED_MSG = "Операция отменена пользователем";
    private static final String NO_TICKETS_MSG = "Не найдено билетов, превышающих заданный";
    private static final String INPUT_PROMPT = "\nВведите данные билета для сравнения (или 'отмена' для прерывания):";

    /**
     * Конструктор команды удаления.
     */
    public RemoveGreater(Terminal terminal, TicketManager ticketManager) {
        super("remove_greater", 
             "удалить из коллекции все билеты, превышающие заданный");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.ticketManager = Objects.requireNonNull(ticketManager, "Менеджер билетов не может быть null");
    }

    /**
     * Выполняет удаление элементов, превышающих заданный.
     */
    @Override
    public void execute(String[] args) {
        try {
            Ticket referenceTicket = getReferenceTicket(args);
            
            int removedCount = ticketManager.removeGreater(referenceTicket);
            
            if (removedCount > 0) {
                terminal.printSuccess(String.format(REMOVED_COUNT_MSG, 
                    removedCount, ticketManager.size()));
            } else {
                terminal.printWarning(NO_TICKETS_MSG);
            }
                
        } catch (AbstractPrompt.InputCancelledException e) {
            terminal.printWarning(CANCELLED_MSG);
        } catch (IllegalArgumentException e) {
            terminal.printError("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            terminal.printError("Системная ошибка: " + e.getMessage());
        }
    }

    /**
     * Получает билет для сравнения.
     */
    private Ticket getReferenceTicket(String[] args) throws AbstractPrompt.InputCancelledException {
        if (args.length > 0) {
            try {
                return Ticket.fromArgs(args);
            } catch (IllegalArgumentException e) {
                terminal.printWarning("Некорректные аргументы, перехожу в интерактивный режим...");
            }
        }
        
        terminal.println(INPUT_PROMPT);
        TicketPrompt ticketPrompt = new TicketPrompt(terminal);
        return ticketPrompt.ask();
    }
}