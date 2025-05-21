package server;

import java.io.IOException;
import java.util.logging.Logger;

import client.Terminal;

/**
 * Поток для прослушивания команд с консоли во время работы сервера.
 */
public class ServerCLI extends Thread {
    private final ServerContext context;
    private final Server server;
    private static final Logger logger = Logger.getLogger(ServerCLI.class.getName());

    public ServerCLI(ServerContext context, Server server) {
        this.context = context;
        this.server = server;
    }

    @Override
    public void run() {
        Terminal terminal = new Terminal();
        logger.info("Ожидание команд администратора (например: save, exit)");

        while (true) {
            terminal.print("> ");
            String input = terminal.readln().trim();
            if (input.isEmpty()) {
                return;
            }

            switch (input) {
                case "save" -> {
                    try {
                        context.getDumpManager().save(context.getTicketManager().toCSV());
                        logger.info("Коллекция сохранена вручную по команде");
                    } catch (IOException e){
                        logger.severe("Ошибка сохранения: " + e.getMessage());
                    }
                }
                case "exit" -> {
                    server.shutdown(); // корректное завершение
                    return;
                }
                default -> {
                    logger.warning("Неизвестная команда: " + input);
                }
            }
        }
    }
}
