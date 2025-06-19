package server_cmd;

import auth_utils.User;
import managers.TicketManagerInterface;
import utils.Response;
import utils.ServerCommand;
import utils.UserAwareCommand;
import utils.ExecutionContext;

/**
 * Команда для вывода информации о коллекции билетов.
 * Отображает тип коллекции, дату инициализации, количество элементов и другую статистику.
 */
public class Info implements ServerCommand, UserAwareCommand{
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
        TicketManagerInterface ticketManager = context.getTicketManager();
        return Response.ok("Тип коллекции: " + ticketManager.getCollectionType()  + ", Размер: " + ticketManager.size());
    }
}