package server.cmd;

import models.Ticket;
import server.ServerContext;
import utils.Response;
import utils.ServerCommand;

public class Insert implements ServerCommand {
  private final Integer key;
  private final Ticket ticket;

  public Insert(Integer key, Ticket ticket) {
    this.key = key;
    this.ticket = ticket;
  }

  @Override
  public Response execute(ServerContext context) {
    try{
      ticket.updateId();
      context.getTicketManager().insert(key, ticket);
      return Response.ok("Ticket added successfully.");
      } catch (IllegalArgumentException e) {
          return Response.error(e.getMessage());
      } catch (Exception e) {
          return Response.error("Системная ошибка: " + e.getMessage());
      }
  }
}
