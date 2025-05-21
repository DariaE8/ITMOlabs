package server.cmd;

import server.ServerContext;
import utils.Response;
import utils.ServerCommand;

/**
 * Возвращает данные в виде списка
 */
public class Show implements ServerCommand {
  @Override
  public Response execute(ServerContext context) {
    return Response.ok("All tickets", context.getTicketManager().getAllTickets());
  }
}
