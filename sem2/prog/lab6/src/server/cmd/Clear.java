package server.cmd;

import models.TicketManager;
import server.ServerContext;
import utils.Response;
import utils.ServerCommand;

/**
 * Команда для очистки коллекции билетов.
 * Удаляет все элементы из текущей коллекции.
 */
public class Clear implements ServerCommand {
  @Override
  public Response execute(ServerContext context) {
    try {
        TicketManager ticketManager = context.getTicketManager();
        int count = ticketManager.size();
        ticketManager.clear();
        return Response.ok("Коллекция успешно очищена. Удалено элементов: " + count);
    } catch (Exception e) {
        return Response.error("Ошибка при очистке коллекции: " + e.getMessage());
        }
    }
}