package server_cmd;

import auth_utils.User;
import models.Ticket;
import utils.ExecutionContext;
import utils.Response;
import utils.ServerCommand;
import utils.UserAwareCommand;

public class Insert implements ServerCommand, UserAwareCommand {
  private final Integer key;
  private final Ticket ticket;
  private User user;

  public Insert(Integer key, Ticket ticket) {
    this.key = key;
    this.ticket = ticket;
  }

  @Override
  public void attachUser(User user) {
      this.user = user;
  }

  @Override
  public Response execute(ExecutionContext context) {
    if (user == null) {
      return Response.error("Пользователь не авторизован");
    }
    ticket.setOwnerId(user.getId());
    try{
      ticket.updateId();
      context.getTicketManager().insert(key, ticket);
      return Response.ok("Ticket added successfully.", ticket.getId());
      } catch (IllegalArgumentException e) {
          return Response.error(e.getMessage());
      } catch (Exception e) {
          return Response.error("Системная ошибка: " + e.getMessage());
      }
  }
}
