package server.modules;

import utils.Response;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

/**
 * Класс для отправки ответов клиентам через UDP-соединение.
 * Обеспечивает сериализацию объектов Response и их отправку по указанному адресу.
 */
public class ResponseSender {
    /**
     * UDP-сокет для отправки пакетов с ответами.
     */
    private final DatagramSocket socket;

    /**
     * Логгер для записи информации о процессе отправки ответов.
     */
    private static final Logger logger = Logger.getLogger(ResponseSender.class.getName());

    /**
     * Создает новый экземпляр отправителя ответов с указанным сокетом.
     *
     * @param socket UDP-сокет для отправки пакетов
     * @throws IllegalArgumentException если переданный сокет равен null
     */
    public ResponseSender(DatagramSocket socket) {
        if (socket == null) {
            throw new IllegalArgumentException("Сокет не может быть null");
        }
        this.socket = socket;
    }

    /**
     * Отправляет ответ клиенту по указанному адресу и порту.
     *
     * @param response объект ответа для отправки
     * @param address IP-адрес клиента
     * @param port порт клиента
     * @throws IOException если произошла ошибка ввода-вывода при сериализации или отправке
     * @throws IllegalArgumentException если response, address или port имеют недопустимые значения
     * @throws SecurityException если менеджер безопасности запрещает операцию отправки
     */
    public void send(Response response, InetAddress address, int port) throws Exception {
        if (response == null) {
            throw new IllegalArgumentException("Ответ не может быть null");
        }
        if (address == null) {
            throw new IllegalArgumentException("Адрес не может быть null");
        }
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Недопустимый номер порта: " + port);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(response);
        byte[] responseBytes = baos.toByteArray();

        DatagramPacket responsePacket = new DatagramPacket(responseBytes, responseBytes.length, address, port);
        socket.send(responsePacket);
        logger.info("Ответ отправлен клиенту по адресу " + address + ":" + port);
    }
}