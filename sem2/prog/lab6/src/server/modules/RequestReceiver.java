package server.modules;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Logger;

/**
 * Класс для приема входящих UDP-пакетов от клиентов.
 * Обеспечивает получение данных через DatagramSocket и их преобразование в DatagramPacket.
 */
public class RequestReceiver {
    /**
     * Сокет для приема UDP-пакетов.
     */
    private final DatagramSocket socket;
    
    /**
     * Максимальный размер буфера для приема данных.
     */
    private static final int BUFFER_SIZE = 65535;
    
    /**
     * Логгер для записи информации о работе приемника запросов.
     */
    private static final Logger logger = Logger.getLogger(RequestReceiver.class.getName());

    /**
     * Создает новый экземпляр приемника запросов с указанным сокетом.
     *
     * @param socket UDP-сокет для приема пакетов
     * @throws IllegalArgumentException если переданный сокет равен null
     */
    public RequestReceiver(DatagramSocket socket) {
        if (socket == null) {
            throw new IllegalArgumentException("Сокет не может быть null");
        }
        this.socket = socket;
    }

    /**
     * Ожидает и принимает входящий UDP-пакет.
     *
     * @return полученный DatagramPacket с данными от клиента
     * @throws IOException если произошла ошибка ввода-вывода при приеме пакета
     * @throws SecurityException если существует менеджер безопасности и он не разрешает операцию
     */
    public DatagramPacket receive() throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        logger.info("Получен пакет от " + packet.getAddress() + ":" + packet.getPort());
        return packet;
    }
}