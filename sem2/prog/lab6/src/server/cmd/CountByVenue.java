package server.cmd;

import models.Venue;
import server.ServerContext;
import utils.Response;
import utils.ServerCommand;

/**
 * Команда для подсчета билетов по указанному месту проведения (venue).
 * Выводит количество элементов коллекции, у которых поле venue равно заданному значению.
 */
public class CountByVenue implements ServerCommand {
    private final Venue venue;

    public CountByVenue(Venue venue) {
        this.venue = venue;
    }

    @Override
    public Response execute(ServerContext context) {
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
