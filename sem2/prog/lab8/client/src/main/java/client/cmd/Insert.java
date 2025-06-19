package client.cmd;

import utils.AbstractPrompt;
import utils.Command;
import utils.CommandException;
import utils.RequestException;
import utils.Response;

import java.util.Objects;

import client.ConnectionHandler;
import utils.Terminal;
import client.prompts.TicketPrompt;
import models.Ticket;

/**
 * Команда для добавления нового элемента в коллекцию.
 */
public class Insert extends Command {
    private Terminal terminal;
    private final ConnectionHandler chandler;
    private static final String CANCELLED_MESSAGE = 
        "Создание билета отменено пользователем";

    /**
     * Создает команду insert.
     * @param terminal терминал для ввода/вывода
     * @param chandler обработчик соединения с сервером
     */
    public Insert(Terminal terminal, ConnectionHandler chandler) {
        super("insert", "добавить новый элемент с указанным ключом");
        this.terminal = Objects.requireNonNull(terminal);
        this.chandler = Objects.requireNonNull(chandler, "Обработчик запросов не может быть null");
    }

    public Insert(ConnectionHandler chandler) {
        super("insert", "добавить новый элемент с указанным ключом");
        this.chandler = Objects.requireNonNull(chandler, "Обработчик запросов не может быть null");
    }

    /**
     * Выполняет команду insert.
     * @param args аргументы команды (ожидается ключ)
     * @throws CommandException если выполнение команды завершилось ошибкой
     */
    public void execute(String[] args) throws CommandException {
        try {
            validateArguments(args);
            int key = parseKey(args[0]);
            Ticket ticket = createNewTicket();
            
            Response resp = chandler.request(new server_cmd.Insert(key, ticket));
            terminal.println(resp.getMessage());
        } catch (AbstractPrompt.InputCancelledException e) {
            if(terminal.checkScanner()) {
                terminal.printWarning(CANCELLED_MESSAGE);
            } else {
                throw new CommandException(e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            terminal.printError(e.getMessage());
        } catch (RequestException e) {
            throw new CommandException("Ошибка сервера: " + e.getMessage());
        } catch (Exception e) {
            terminal.printError("Системная ошибка: " + e.getMessage());
        }
    }

    public long execute_gui(Ticket ticket) throws CommandException {
        try {
            // validateArguments(args);
            // int key = parseKey(args[0]);
            // Ticket ticket = createNewTicket();
            Response resp = chandler.request(new server_cmd.Insert((int)(Math.random() * Integer.MAX_VALUE), ticket));
            // terminal.println(resp.getMessage());
            // System.out.println(resp.getMessage());
            return (long) resp.getData();
        } catch (IllegalArgumentException e) {
            throw new CommandException("Неверный аргумент: " + e.getMessage());
        } catch (RequestException e) {
            throw new CommandException("Ошибка сервера insert: " + e.getMessage());
        } catch (Exception e) {
            throw new CommandException("Непредвиденная ошибка: " + e.getMessage());
        }
    }

    /**
     * Проверяет корректность аргументов.
     * @param args аргументы команды
     * @throws IllegalArgumentException если аргументы некорректны
     */
    private void validateArguments(String[] args) {
        if (args == null || args.length < 1) {
            throw new IllegalArgumentException("Не указан ключ для вставки");
        }
    }

    /**
     * Парсит ключ из строки.
     * @param keyStr строка с ключом
     * @return числовое значение ключа
     * @throws IllegalArgumentException если ключ некорректен
     */
    private int parseKey(String keyStr) {
        try {
            int key = Integer.parseInt(keyStr);
            if (key <= 0)
                throw new IllegalArgumentException("Ключ должен быть положительным");
            return key;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверный формат ключа", e);
        }
    }

    /**
     * Создает новый билет через диалог с пользователем.
     * @return созданный билет
     * @throws AbstractPrompt.InputCancelledException если ввод отменен
     */
    private Ticket createNewTicket() throws AbstractPrompt.InputCancelledException {
        terminal.println("Создание нового билета:");
        TicketPrompt prompt = new TicketPrompt(terminal);
        return prompt.ask();
    }
}