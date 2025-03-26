package cli.commands;

import cli.Terminal;
import models.TicketManager;
import utils.Command;
import java.util.Objects;

/**
 * Команда для удаления из коллекции всех элементов, ключ которых превышает заданный.
 * Удаляет все элементы с ключами, большими чем указанный ключ.
 */
public class RemoveGreaterKey extends Command {
    private final Terminal terminal;
    private final TicketManager ticketManager;
    private static final String SUCCESS_MSG = "Удалено элементов: %d. Текущий размер коллекции: %d";
    private static final String KEY_FORMAT_ERROR = "Ключ должен быть целым числом";
    private static final String NO_ELEMENTS_MSG = "Элементы с ключами больше %d не найдены";

    /**
     * Конструктор команды удаления.
     *
     * @param terminal терминал для ввода/вывода
     * @param ticketManager менеджер коллекции билетов
     * @throws NullPointerException если terminal или ticketManager равен null
     */
    public RemoveGreaterKey(Terminal terminal, TicketManager ticketManager) {
        super("remove_greater_key", 
             "удалить из коллекции все элементы с ключами больше заданного");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
        this.ticketManager = Objects.requireNonNull(ticketManager, "Менеджер билетов не может быть null");
    }

    /**
     * Выполняет удаление элементов с ключами больше заданного.
     *
     * @param args аргументы команды [ключ]
     */
    @Override
    public void execute(String[] args) {
        try {
            validateArguments(args);
            int key = parseKey(args[0]);
            int removedCount = removeElements(key);
            
            if (removedCount > 0) {
                terminal.printSuccess(String.format(SUCCESS_MSG, 
                    removedCount, ticketManager.size()));
            } else {
                terminal.printWarning(String.format(NO_ELEMENTS_MSG, key));
            }
        } catch (IllegalArgumentException e) {
            terminal.printError(e.getMessage());
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

    /**
     * Удаляет элементы с ключами больше заданного.
     *
     * @return количество удаленных элементов
     */
    private int removeElements(int key) {
        int initialSize = ticketManager.size();
        ticketManager.removeGreaterKeys(key);
        return initialSize - ticketManager.size();
    }
}