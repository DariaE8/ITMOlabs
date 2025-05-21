package server;

import models.TicketManager;
import utils.Response;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Logger;

import server.modules.*;

/**
 * Главный серверный класс, координирующий приём, обработку и отправку команд.
 */
public class Server {
    private static final int PORT = 9999;
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    private TicketManager ticketManager;
    private DumpManager dumpManager; 
    private ServerContext context;

    private DatagramSocket socket;
    private volatile boolean running = true;
    private Thread consoleThread;

    public void start() {
        try {
            initializeComponents("./src/db.csv");
            logger.info("Starting server on port " + PORT);
            this.socket = new DatagramSocket(PORT);

            // Модуль команд с консоли сервера
            consoleThread = new ServerCLI(context, this);
            consoleThread.start();

            RequestReceiver receiver = new RequestReceiver(socket);
            RequestHandler handler = new RequestHandler(context);
            ResponseSender sender = new ResponseSender(socket);

            while (running) {
                logger.fine("Waiting for packet...");
                DatagramPacket packet = receiver.receive();
                Response response = handler.handle(packet);
                sender.send(response, packet.getAddress(), packet.getPort());
            }
        } catch (SocketException e) {
            logger.severe("Socket error: " + e.getMessage());
        } catch (IOException e) {
            logger.severe("IO error: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Ошибка сервера: " + e.getMessage());
        } finally {
            shutdown();
        }
    }

    private void initializeComponents(String filename) throws IOException {
        this.dumpManager = new DumpManager(filename);
        this.ticketManager = new TicketManager(dumpManager.load());
        this.context =  new ServerContext(ticketManager, dumpManager);
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
        try {
            dumpManager.save(ticketManager.toCSV());
            logger.info("Коллекция успешно сохранена");
        } catch (IOException e){
            logger.severe("Ошибка сохранения: " + e.getMessage());
        }

        if (consoleThread != null && consoleThread.isAlive()) {
            consoleThread.interrupt();
        }
    }
}
