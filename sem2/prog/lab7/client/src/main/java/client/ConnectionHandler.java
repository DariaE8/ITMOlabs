package client;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

import auth_utils.AuthorizedCommandDecorator;
import utils.RequestException;
import utils.Response;
import utils.ServerCommand;

/**
 * Обработчик соединения с сервером через UDP с использованием NIO.
 */
public class ConnectionHandler {
    private static final int SERVER_PORT = 9999;
    private static final String HOST = "localhost";
    private static final int CONN_TIMEOUT = 3000; // 3 секунды
    private DatagramChannel channel;
    private Selector selector;

    private LoginManager loginManager;

    /**
     * Инициализирует соединение с сервером.
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public void init(LoginManager loginManager) throws IOException {
        try {
            this.channel = DatagramChannel.open();
            channel.configureBlocking(false);
            this.selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
            this.loginManager = loginManager;
        } catch (Exception e) {
            System.out.println("AHAH " + e.getMessage());
            throw e;
        }
    }

    /**
     * Отправляет команду серверу и получает ответ.
     * @param cmd команда для отправки
     * @return ответ сервера
     * @throws RequestException если произошла ошибка при выполнении запроса
     */
    public Response request(ServerCommand serv_cmd) throws RequestException {
        ServerCommand cmd = loginManager.isLoggedIn()
            ? new AuthorizedCommandDecorator(serv_cmd, loginManager.getCurrentUser())
            : serv_cmd;
        try {
            // Сериализация команды
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(cmd);

            // Отправка данных
            ByteBuffer buf = ByteBuffer.wrap(baos.toByteArray());
            channel.send(buf, new InetSocketAddress(HOST, SERVER_PORT));
            
            // Ожидание ответа с таймаутом
            int ready = selector.select(CONN_TIMEOUT);
            if (ready == 0) {
                throw new RequestException("[!] Сервер временно недоступен");
            }
            
            // Обработка ответа
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            
            if (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isReadable()) {
                    ByteBuffer receiveBuf = ByteBuffer.allocate(65535);
                    SocketAddress addr = channel.receive(receiveBuf);
                    if (addr != null) {
                        ObjectInputStream ois = new ObjectInputStream(
                            new ByteArrayInputStream(receiveBuf.array()));
                        return (Response) ois.readObject();
                    } else {
                        throw new RequestException("[!] Сервер временно недоступен");
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw new RequestException(e.getMessage());
        }
    }

    /**
     * Закрывает соединение и освобождает ресурсы.
     */
    public void close() {
        try {
            if (selector != null && selector.isOpen()) {
                selector.close();
            }
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            System.out.println("[i] Соединение закрыто");
        } catch (IOException e) {
            System.err.println("[!] Ошибка при закрытии соединения: " + e.getMessage());
        }
    }
}