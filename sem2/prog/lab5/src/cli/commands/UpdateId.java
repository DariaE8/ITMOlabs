package cli.commands;

import cli.Terminal;
import cli.prompts.TicketPrompt;
import utils.AbstractPrompt;
import models.Ticket;
import models.TicketManager;
import utils.Command;
import java.util.Objects;

/**
 * Команда для обновления элемента коллекции по указанному ID.
 * Поддерживает как аргументы командной строки, так и интерактивный режим ввода.
 */
public class UpdateId extends Command {
    private final Terminal terminal;
    private final TicketManager ticketManager;
    private static final String SUCCESS_MSG = "Билет с ID %d успешно обновлён";
    private static final String NOT_FOUND_MSG = "Билет с ID %d не найден";
    private static final String CANCELLED_MSG = "Обновление билета отменено";
    private static final String ID_ERROR = "ID должен быть положительным целым числом";
    private static final String USAGE = "Использование: update_id <ID> [данные] или update_id <ID> (интерактивный режим)";
    private static final String INPUT_PROMPT = "\nВведите новые данные билета (или 'отмена' для прерывания):";

    /**
     * Конструктор команды обновления.
     */
    public UpdateId(Terminal terminal, TicketManager ticketManager) {
        super("update_id", "обновить билет по указанному ID");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.ticketManager = Objects.requireNonNull(ticketManager, "Менеджер билетов не может быть null");
    }

    /**
     * Выполняет обновление билета по ID.
     */
    @Override
    public void execute(String[] args) {
        try {
            validateArguments(args);
            int id = parseId(args[0]);
            if(!ticketManager.checkKeyExist(id)) {
                terminal.printError(String.format(NOT_FOUND_MSG, id));
                return;
            }
            Ticket updatedTicket = getUpdatedTicket(args);

            
            if (ticketManager.update(id, updatedTicket)) {
                terminal.printSuccess(String.format(SUCCESS_MSG, id));
            } else {
                terminal.printError(String.format(NOT_FOUND_MSG, id));
            }
        } catch (AbstractPrompt.InputCancelledException e) {
            terminal.printWarning(CANCELLED_MSG);
        } catch (IllegalArgumentException e) {
            terminal.printError(e.getMessage());
        } catch (Exception e) {
            terminal.printError("Ошибка при обновлении: " + e.getMessage());
        }
    }

    private void validateArguments(String[] args) {
        if (args == null || args.length < 1) {
            throw new IllegalArgumentException("Не указан ID билета\n" + USAGE);
        }
    }

    private int parseId(String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0) throw new IllegalArgumentException(ID_ERROR);
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ID_ERROR);
        }
    }

    /**
     * Получает обновленные данные билета.
     * @throws AbstractPrompt.InputCancelledException если пользователь отменил ввод
     */
    private Ticket getUpdatedTicket(String[] args) throws AbstractPrompt.InputCancelledException {
        if (args.length > 1) {
            try {
                return Ticket.fromArgs(args);
            } catch (IllegalArgumentException e) {
                terminal.printWarning("Некорректные аргументы, перехожу в интерактивный режим...");
            }
        }
        
        terminal.println(INPUT_PROMPT);
        TicketPrompt prompt = new TicketPrompt(terminal);
        return prompt.ask();
    }
}