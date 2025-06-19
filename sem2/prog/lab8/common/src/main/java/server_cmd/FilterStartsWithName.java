package server_cmd;

import java.util.Collection;

import auth_utils.User;
import models.Ticket;
import utils.ExecutionContext;
import utils.Response;
import utils.ServerCommand;
import utils.UserAwareCommand;

/**
 * Команда для фильтрации билетов по началу имени.
 * Выводит все элементы коллекции, имя которых начинается с заданной подстроки.
 */
public class FilterStartsWithName implements ServerCommand, UserAwareCommand{
  private final String searchString;
  private User user;

  public FilterStartsWithName(String searchString) {
    this.searchString = searchString;
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
    Collection<Ticket> result = context.getTicketManager().filterStartsWithName(searchString);
    if (result.isEmpty()) {
        return Response.ok("Билетов с именем, начинающимся на '" + searchString + "', не найдено");
    } else {
        return Response.ok("Найденные билеты:\n" + result, result);
    }
  }
}
