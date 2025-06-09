package server.modules;

import utils.Response;
import utils.ServerCommand;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.logging.Logger;

import server.ServerContext;



/**
 * Класс для обработки входящих запросов от клиентов.
 */
public class RequestHandler {
    /**
     * Контекст сервера, содержащий необходимые для обработки запросов данные.
     */
    private final ServerContext context;
    
    /**
     * Логгер для записи информации о работе обработчика запросов.
     */
    private static final Logger logger = Logger.getLogger(RequestHandler.class.getName());

    /**
     * Создает новый обработчик запросов с указанным контекстом сервера.
     *
     * @param context контекст сервера
     */
    public RequestHandler(ServerContext context) {
        this.context = context;
    }

    /**
     * Обрабатывает входящий UDP-пакет, содержащий команду сервера.
     *
     * @param packet UDP-пакет с данными команды
     * @return ответ на выполненную команду
     * @throws Exception если произошла ошибка при обработке запроса
     */

    public Response handle(DatagramPacket packet) {
        try {
            logger.info("Начата обработка запроса");

            ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
            ObjectInputStream ois = new ObjectInputStream(bais);

            Object obj = ois.readObject();

            if (!(obj instanceof ServerCommand cmd)) {
                return Response.error("Получен недопустимый тип команды");
            }

            logger.info("Команда получена: " + cmd.getClass().getSimpleName());
            return cmd.execute(context);

        } catch (Exception e) {
            logger.severe("Ошибка при обработке запроса: " + e.getMessage());
            return Response.error("Системная ошибка: " + e.getMessage());
        }
    }
}