package client.cmd;

import client.ConnectionHandler;
import client.Terminal;
import utils.Command;
import utils.CommandException;
import utils.RequestException;
import utils.Response;

import java.util.Objects;

/**
 * Команда для удаления из коллекции всех элементов, ключ которых превышает заданный.
 * Удаляет все элементы с ключами, большими чем указанный ключ.
 */
public class RemoveGreaterKey extends Command {
    private final Terminal terminal;
    private final ConnectionHandler chandler;
    private static final String KEY_FORMAT_ERROR = "Ключ должен быть целым числом";

    /**
     * Конструктор команды удаления.
     *
     * @param terminal терминал для ввода/вывода
     * @param ticketManager менеджер коллекции билетов
     * @throws NullPointerException если terminal или ticketManager равен null
     */
    public RemoveGreaterKey(Terminal terminal, ConnectionHandler chandler) {
        super("remove_greater_key", 
             "удалить из коллекции все элементы с ключами больше заданного");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.chandler = Objects.requireNonNull(chandler, "Обработчик запросов не может быть null");
    }

    /**
     * Выполняет удаление элементов с ключами больше заданного.
     *
     * @param args аргументы команды [ключ]
     */
    @Override
    public void execute(String[] args) throws CommandException {
        try {
            validateArguments(args);
            int key = parseKey(args[0]);
            
            Response resp = chandler.request(new server.cmd.RemoveGreaterKey(key));
            // String[] data = resp.getMessage().split(" ", 2);
            // int removedCount = parseKey(data[0]);
            // int currentSize = parseKey(data[1]);

            // if (removedCount > 0) {
            //     terminal.printSuccess(String.format(SUCCESS_MSG, 
            //         removedCount, currentSize));
            // } else {
            //     terminal.printWarning(String.format(NO_ELEMENTS_MSG, key));
            // }
            terminal.println(resp.getMessage());
        } catch (IllegalArgumentException e) {
            terminal.printError(e.getMessage());
        } catch (RequestException e) {
            throw new CommandException("Ошибка сервера: " + e.getMessage());
        }
    }

    /**
     * Проверяет аргументы команды.
     */
    private void validateArguments(String[] args) {
        if (args == null || args.length < 1) {
            throw new IllegalArgumentException("Не указан ключ для удаления");
        }
    }

    /**
     * Парсит и валидирует ключ.
     */
    private int parseKey(String keyStr) {
        try {
            return Integer.parseInt(keyStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(KEY_FORMAT_ERROR);
        }
    }
}