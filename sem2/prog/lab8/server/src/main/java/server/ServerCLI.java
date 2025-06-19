package server;

import java.util.logging.Logger;

import utils.Terminal;

/**
 * Поток для прослушивания команд с консоли во время работы сервера.
 */
public class ServerCLI extends Thread {
    private final Server server;
    private static final Logger logger = Logger.getLogger(ServerCLI.class.getName());

    public ServerCLI(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        Terminal terminal = new Terminal();
        logger.info("Ожидание команд администратора (например: exit)");

        while (true) {
            terminal.print("> ");
            String input = terminal.readln().trim();
            if (input.isEmpty()) {
                return;
            }

            switch (input) {
                case "exit" :{
                    server.shutdown(); // корректное завершение
                    return;
                }
                default : {
                    logger.warning("Неизвестная команда: " + input);
                }
            }
        }
    }
}
