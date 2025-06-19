package server_cmd;


import auth_utils.User;
import managers.TicketManagerInterface;
import utils.ExecutionContext;
import utils.Response;
import utils.ServerCommand;
import utils.UserAwareCommand;

/**
 * Команда для очистки коллекции билетов.
 * Удаляет все элементы из текущей коллекции.
 */
public class Clear implements ServerCommand, UserAwareCommand {
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
        try {
            TicketManagerInterface ticketManager = context.getTicketManager();
            long count = ticketManager.countByOwner(user.getId());
            ticketManager.clear(user.getId());
            return Response.ok("Коллекция успешно очищена. Удалено элементов: " + count);
        } catch (Exception e) {
            return Response.error("Ошибка при очистке коллекции: " + e.getMessage());
            }
    }
}