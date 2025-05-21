package server.cmd;

import models.Ticket;
import models.TicketManager;
import server.ServerContext;
import utils.Response;
import utils.ServerCommand;

/**
 * Команда для обновления элемента коллекции по указанному ID.
 */
public class UpdateId implements ServerCommand {
    private final Integer id;
    private final Ticket ticket;

    private static final String SUCCESS_MSG = "Билет с ID %d успешно обновлён";
    private static final String NOT_FOUND_MSG = "Билет с ID %d не найден";
    private static final String CANCELLED_MSG = "Обновление билета отменено";
    
    public UpdateId(Integer id, Ticket ticket) {
        this.id = id;
        this.ticket = ticket;
    }

    @Override
    public Response execute(ServerContext context) {
        try{
            TicketManager ticketManager = context.getTicketManager();
            if(!ticketManager.checkKeyExist(id)) {
                return Response.error(CANCELLED_MSG);
            }

            if (ticketManager.update(id, ticket)) {
                return Response.ok(String.format(SUCCESS_MSG, id));
            } else {
                return Response.error(String.format(NOT_FOUND_MSG, id));
            }
        } catch (IllegalArgumentException e) {
            return new Response("Неверный аргумен:" + e.getMessage());
        } catch (Exception e) {
            return new Response("Ошибка при обновлении: " + e.getMessage());
        }
    }
}

