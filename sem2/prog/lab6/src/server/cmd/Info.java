package server.cmd;

import models.TicketManager;
import server.ServerContext;
import utils.Response;
import utils.ServerCommand;

/**
 * Команда для вывода информации о коллекции билетов.
 * Отображает тип коллекции, дату инициализации, количество элементов и другую статистику.
 */
public class Info implements ServerCommand {
    @Override
    public Response execute(ServerContext context) {
        TicketManager ticketManager = context.getTicketManager();
        return Response.ok("Тип коллекции: " + ticketManager.getCollectionType()  + ", Размер: " + ticketManager.size());
    }
}