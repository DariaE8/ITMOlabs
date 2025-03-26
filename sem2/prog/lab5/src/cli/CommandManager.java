package cli;

import java.util.*;
import utils.Command;
import models.TicketManager;
import cli.commands.*;

import utils.CommandException;

/**
 * Менеджер команд для работы с CLI-приложением.
 * Обеспечивает регистрацию, хранение и выполнение команд,
 * а также ведение истории выполненных команд.
 */
public class CommandManager {
    private final Queue<String> commandHistory = new LinkedList<>();
    private static final int MAX_HISTORY_SIZE = 5;
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Инициализирует менеджер команд, регистрируя все поддерживаемые команды.
     * 
     * @param ticketManager менеджер для работы с коллекцией билетов
     * @param dumpManager менеджер для работы с сохранением данных
     * @param terminal интерфейс для вывода результатов команд
     * @throws IllegalArgumentException если любой из параметров равен null
     */
    public void init(TicketManager ticketManager, DumpManager dumpManager, Terminal terminal) {
        Objects.requireNonNull(ticketManager, "Менеджер билетов не может быть null");
        Objects.requireNonNull(dumpManager, "Менеджер сохранения не может быть null");
        Objects.requireNonNull(terminal, "Терминал не может быть null");

        registerCoreCommands(terminal, ticketManager);
        registerTicketCommands(terminal, ticketManager);
        registerUtilityCommands(dumpManager, ticketManager, terminal);
    }

    /**
     * Регистрирует новую команду.
     * 
     * @param name название команды
     * @param command реализация команды
     * @throws IllegalArgumentException если имя команды или реализация равны null
     */
    public void addCommand(String name, Command command) {
        Objects.requireNonNull(name, "Имя команды не может быть null");
        Objects.requireNonNull(command, "Реализация команды не может быть null");
        this.commands.put(name, command);
    }

    /**
     * Возвращает коллекцию всех зарегистрированных команд.
     * 
     * @return коллекция команд
     */
    public Collection<Command> getCommands() {
        return Collections.unmodifiableCollection(commands.values());
    }
    
    /**
     * Выполняет указанную команду с заданными аргументами.
     * 
     * @param commandName название команды для выполнения
     * @param args аргументы команды
     * @throws CommandException если команда не найдена или возникла ошибка при выполнении
     */
    public void executeCommand(String commandName, String[] args) throws CommandException {
        Command command = commands.get(commandName);
        if (command == null) {
            throw new CommandException("Неизвестная команда: " + commandName);
        }
        
        try {
            addToHistory(command.getName());
            command.execute(args);
        } catch (Exception e) {
            throw new CommandException("Ошибка при выполнении команды '" + commandName + "': " + e.getMessage(), e);
        }
    }

    /**
     * Добавляет команду в историю выполненных команд.
     * Если история достигла максимального размера, удаляет самую старую команду.
     * 
     * @param command название команды для добавления в историю
     */
    private void addToHistory(String command) {
        if (commandHistory.size() >= MAX_HISTORY_SIZE) {
            commandHistory.poll();
        }
        commandHistory.offer(command);
    }

    /**
     * Возвращает историю выполненных команд.
     * 
     * @return очередь с историей команд (новые команды в конце)
     */
    public Queue<String> getHistory() {
        return new LinkedList<>(commandHistory);
    }

    private void registerCoreCommands(Terminal terminal, TicketManager ticketManager) {
        addCommand("help", new Help(terminal, this));
        addCommand("info", new Info(terminal, ticketManager));
        addCommand("exit", new Exit());
    }

    private void registerTicketCommands(Terminal terminal, TicketManager ticketManager) {
        addCommand("show", new Show(terminal, ticketManager));
        addCommand("show_table", new ShowTable(terminal, ticketManager));
        addCommand("insert", new Insert(terminal, ticketManager));
        addCommand("update", new UpdateId(terminal, ticketManager));
        addCommand("clear", new Clear(terminal, ticketManager));
        addCommand("count_by_venue", new CountByVenue(terminal, ticketManager));
        addCommand("filter_starts_with_name", new FilterStartsWithNameName(terminal, ticketManager));
        addCommand("print_ascending", new PrintAscending(terminal, ticketManager));
        addCommand("remove_greater", new RemoveGreater(terminal, ticketManager));
        addCommand("remove_by_key", new RemoveGreaterKey(terminal, ticketManager));
        addCommand("remove_greater_key", new RemoveGreaterKey(terminal, ticketManager));
    }

    private void registerUtilityCommands(DumpManager dumpManager, TicketManager ticketManager, Terminal terminal) {
        addCommand("history", new History(terminal, getHistory()));
        addCommand("save", new Save(dumpManager, ticketManager));
        addCommand("execute_script", new ExecuteScriptFileName(terminal, this));
    }
}