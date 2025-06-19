package server_cmd;

import auth_utils.User;
import models.Venue;
import utils.ExecutionContext;
import utils.Response;
import utils.ServerCommand;
import utils.UserAwareCommand;

/**
 * Команда для подсчета билетов по указанному месту проведения (venue).
 * Выводит количество элементов коллекции, у которых поле venue равно заданному значению.
 */
public class CountByVenue implements ServerCommand, UserAwareCommand {
    private final Venue venue;
    private User user;
    @Override
    public void attachUser(User user) {
        this.user = user;
    }

    public CountByVenue(Venue venue) {
        this.venue = venue;
    }

    @Override
    public Response execute(ExecutionContext context) {
        if (user == null) {
        return Response.error("Пользователь не авторизован");
        }
        try {
            long count = context.getTicketManager().countByVenue(venue);
            return Response.ok(String.format(
                "Найдено билетов с указанным местом проведения: %d", 
                count));
        } catch (Exception e) {
            return Response.error(
                "Ошибка при подсчете по месту проведения: " + e.getMessage());
        }
    }
    
}
