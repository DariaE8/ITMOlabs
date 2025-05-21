package server.cmd;

import server.ServerContext;
import utils.Response;
import utils.ServerCommand;

/**
 * Возвращает данные в виде CSV таблицы
 */
public class ShowTable implements ServerCommand {
    @Override
    public Response execute(ServerContext context) {
        return Response.ok("show_table", context.getTicketManager().toCSV());
    } 
}
