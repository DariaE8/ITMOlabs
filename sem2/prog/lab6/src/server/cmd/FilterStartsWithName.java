package server.cmd;

import java.util.Collection;

import models.Ticket;
import server.ServerContext;
import utils.Response;
import utils.ServerCommand;

/**
 * Команда для фильтрации билетов по началу имени.
 * Выводит все элементы коллекции, имя которых начинается с заданной подстроки.
 */
public class FilterStartsWithName implements ServerCommand {
  private final String searchString;

  public FilterStartsWithName(String searchString) {
    this.searchString = searchString;
  }

  @Override
  public Response execute(ServerContext context) {
    Collection<Ticket> result = context.getTicketManager().filterStartsWithName(searchString);
    if (result.isEmpty()) {
        return Response.ok("Билетов с именем, начинающимся на '" + searchString + "', не найдено");
    } else {
        return Response.ok("Найденные билеты:\n" + result, result);
    }
  }
}
