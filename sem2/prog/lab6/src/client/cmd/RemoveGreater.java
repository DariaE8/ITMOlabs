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
 * Команда для удаления из коллекции всех элементов, превышающих заданный.
 * Сравнение производится по естественному порядку элементов (определяется в классе Ticket).
 */
public class RemoveGreater extends Command {
    private final Terminal terminal;
    private final ConnectionHandler chandler;
    private static final String CANCELLED_MSG = "Операция отменена пользователем";
    private static final String INPUT_PROMPT = "\nВведите данные билета для сравнения:";

    /**
     * Конструктор команды удаления.
     */
    public RemoveGreater(Terminal terminal, ConnectionHandler chandler) {
        super("remove_greater", 
             "удалить из коллекции все билеты, превышающие заданный");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.chandler = Objects.requireNonNull(chandler, "Обработчик запросов не может быть null");
    }

    /**
     * Выполняет удаление элементов, превышающих заданный.
     * @throws CommandException 
     */
    @Override
    public void execute(String[] args) throws CommandException {
        try {
            Ticket referenceTicket = getReferenceTicket(args);

            Response resp = chandler.request(new server.cmd.RemoveGreater(referenceTicket));
            
            terminal.println(resp.getMessage());
            
                
        } catch (AbstractPrompt.InputCancelledException e) {
            if(terminal.checkScanner()) {
                terminal.printWarning(CANCELLED_MSG);
            } else {
                throw new CommandException(e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            terminal.printError("Ошибка: " + e.getMessage());
        } catch (RequestException e) {
            throw new CommandException("Ошибка сервера: " + e.getMessage());
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
        if (terminal.checkScanner()){
            terminal.println(INPUT_PROMPT);
        }
        TicketPrompt ticketPrompt = new TicketPrompt(terminal);
        return ticketPrompt.ask();
    }
}