package server;

import server.auth.AuthManager;
import server.modules.*;
import utils.Response;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;

/**
 * Главный серверный класс, координирующий приём, обработку и отправку команд.
 */
public class Server {
    private static final int PORT = 9999;
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    private TicketManager ticketManager;
    private AuthManager authManager;
    private ServerContext context;

    private DatagramSocket socket;
    private volatile boolean running = true;
    private Thread consoleThread;

    private ForkJoinPool receivePool;

    public void start() {
        try {
            initializeComponents();
            logger.info("Starting server on port " + PORT);
            this.socket = new DatagramSocket(PORT);
            this.receivePool = new ForkJoinPool();

            consoleThread = new ServerCLI(this);
            consoleThread.start();

            RequestReceiver receiver = new RequestReceiver(socket);
            RequestHandler handler = new RequestHandler(context);
            ResponseSender sender = new ResponseSender(socket);

            // Асинхронный цикл приёма пакетов
            receivePool.execute(() -> {
                while (running) {
                    try {
                        DatagramPacket packet = receiver.receive();

                        // Обработка запроса в отдельном потоке
                        new Thread(() -> {
                            Response response = handler.handle(packet);

                            // Отправка ответа в отдельном потоке
                            new Thread(() -> {
                                try {
                                    sender.send(response, packet.getAddress(), packet.getPort());
                                } catch (Exception e) {
                                    logger.severe("Ошибка отправки ответа: " + e.getMessage());
                                }
                            }).start();

                        }).start();

                    } catch (Exception e) {
                        if (running) {
                            logger.severe("Ошибка при обработке входящего пакета: " + e.getMessage());
                        }
                    }
                }
            });

        } catch (SocketException e) {
            if (running) {
                logger.severe("Socket error: " + e.getMessage());
            } else {
                logger.info("Серверное соединение закрыто.");
            }
        } catch (IOException e) {
            logger.severe("IO error: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Ошибка сервера: " + e.getMessage());
        }
    }

    private void initializeComponents() throws IOException {
        this.authManager = new AuthManager();
        this.ticketManager = new TicketManager();
        this.context = new ServerContext(authManager, ticketManager);
    }

    /**
     * Метод завершения работы сервера.
     */
    public void shutdown() {
        logger.info("Выключение сервера...");
        running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }

        if (consoleThread != null && consoleThread.isAlive()) {
            consoleThread.interrupt();
        }

        if (receivePool != null) {
            receivePool.shutdown();
        }
    }
}

