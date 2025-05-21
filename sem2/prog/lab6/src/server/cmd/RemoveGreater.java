package server.cmd;

import models.Ticket;
import models.TicketManager;
import server.ServerContext;
import utils.Response;
import utils.ServerCommand;

/**
 * Команда для удаления из коллекции всех элементов, превышающих заданный.
 * Сравнение производится по естественному порядку элементов (определяется в классе Ticket).
 */
public class RemoveGreater implements ServerCommand{
    private final Ticket ticket;
    private static final String REMOVED_COUNT_MSG = "Удалено элементов: %d. Осталось: %d";
    private static final String NO_TICKETS_MSG = "Не найдено билетов, превышающих заданный";
    public RemoveGreater(Ticket ticket) {
        this.ticket = ticket;
    }
    @Override
    public Response execute(ServerContext context) {
        TicketManager tm = context.getTicketManager();
         try {
            int removedCount = tm.removeGreater(ticket);
            
            if (removedCount > 0) {
                return Response.ok(String.format(REMOVED_COUNT_MSG, 
                    removedCount, tm.size()));
            } else {
                return Response.ok(NO_TICKETS_MSG);
            }
        } catch (IllegalArgumentException e) {
            return Response.error("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            return Response.error("Системная ошибка: " + e.getMessage());
        }
    }
}
