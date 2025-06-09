package server_cmd;

import auth_utils.User;
import managers.TicketManagerInterface;
import models.Ticket;
import utils.ExecutionContext;
import utils.Response;
import utils.ServerCommand;
import utils.UserAwareCommand;

/**
 * Команда для обновления элемента коллекции по указанному ID.
 */
public class UpdateId implements ServerCommand, UserAwareCommand{
    private final Integer id;
    private final Ticket ticket;

    private static final String SUCCESS_MSG = "Билет с id %d успешно обновлён\n";
    private static final String NOT_FOUND_MSG = "Билет с id %d не найден";
    private static final String RESTRICTED_MSG = "У вас нет прав на изменение этого билета";
    private static final String ERROR_MSG = "Возникла ошибка при обновлении билета в id %d";

    private User user;
    @Override
    public void attachUser(User user) {
        this.user = user;
    }
        
    public UpdateId(Integer id, Ticket ticket) {
        this.id = id;
        this.ticket = ticket;
    }

    @Override
    public Response execute(ExecutionContext context) {
        if (user == null) {
            return Response.error("Пользователь не авторизован");
        }
        ticket.setOwnerId(user.getId());
        try{
            TicketManagerInterface ticketManager = context.getTicketManager();
            if(!ticketManager.checkIdExist(id)) {
                return Response.error(String.format(NOT_FOUND_MSG, id));
            }
            if(user.getId() != ticketManager.getTicketById(id).getOwnerId()) {
                return Response.error(RESTRICTED_MSG);
            }

            if (ticketManager.update(id, ticket)) {
                return Response.ok(String.format(SUCCESS_MSG, id));
            } else {
                return Response.error(String.format(ERROR_MSG, id));
            }
        } catch (IllegalArgumentException e) {
            return new Response("Неверный аргумен:" + e.getMessage());
        } catch (Exception e) {
            return new Response("Ошибка при обновлении: " + e.getMessage());
        }
    }
}

