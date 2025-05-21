package client.cmd;

import client.ConnectionHandler;
import client.Terminal;
import client.prompts.TicketPrompt;
import utils.AbstractPrompt;
import models.Ticket;
import utils.Command;
import utils.CommandException;
import utils.RequestException;
import utils.Response;

import java.util.Objects;

/**
 * Команда для обновления элемента коллекции по указанному ID.
 * Поддерживает как аргументы командной строки, так и интерактивный режим ввода.
 */
public class UpdateId extends Command {
    private final Terminal terminal;
    private final ConnectionHandler chandler;
    private static final String CANCELLED_MSG = "Обновление билета отменено";
    private static final String ID_ERROR = "ID должен быть положительным целым числом";
    private static final String USAGE = "Использование: update_id <ID> [данные] или update_id <ID> (интерактивный режим)";
    private static final String INPUT_PROMPT = "Введите новые данные билета:";

    /**
     * Конструктор команды обновления.
     */
    public UpdateId(Terminal terminal,ConnectionHandler chandler) {
        super("update_id", "обновить билет по указанному ID");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.chandler = Objects.requireNonNull(chandler, "Обработчик запросов не может быть null");
    }

    /**
     * Выполняет обновление билета по ID.
     * @throws CommandException 
     */
    @Override
    public void execute(String[] args) throws CommandException {
        try {
            validateArguments(args);
            int id = parseId(args[0]);
           
            Ticket updatedTicket = getUpdatedTicket(args);
            Response resp = chandler.request(new server.cmd.UpdateId(id, updatedTicket));

            if (resp.isError()) {
                throw new CommandException(resp.getMessage());
            }
            // if (resp) {
                
            //     terminal.printSuccess(String.format(SUCCESS_MSG, id));
            // } else {
            //     terminal.printError(String.format(NOT_FOUND_MSG, id));
            // }
            terminal.print(resp.getMessage());

        } catch (AbstractPrompt.InputCancelledException e) {
            if(terminal.checkScanner()) {
                terminal.printWarning(CANCELLED_MSG);
            } else {
                throw new CommandException(e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            terminal.printError(e.getMessage());
        } catch (RequestException e) {
            throw new CommandException("Ошибка сервера: " + e.getMessage());
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
        if (terminal.checkScanner()){
            terminal.println(INPUT_PROMPT);
        } 
        TicketPrompt prompt = new TicketPrompt(terminal);
        return prompt.ask();
    }
}