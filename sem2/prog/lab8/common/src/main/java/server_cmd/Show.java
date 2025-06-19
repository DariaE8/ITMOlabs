package server_cmd;

import auth_utils.User;
import utils.ExecutionContext;
import utils.Response;
import utils.ServerCommand;
import utils.UserAwareCommand;

/**
 * Возвращает данные в виде списка
 */
public class Show implements ServerCommand,UserAwareCommand {
  private User user;
  @Override
  public void attachUser(User user) {
      this.user = user;
  }
  @Override
  public Response execute(ExecutionContext context) {
    if (user == null) {
      return Response.error("Пользователь не авторизован");
    }
    System.out.println(user);
    return Response.ok("All tickets", context.getTicketManager().getAllTickets());
  }
}
