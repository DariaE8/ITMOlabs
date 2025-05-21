package client;

import java.util.*;
import utils.Command;
import client.cmd.*;

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
   * @param dumpManager   менеджер для работы с сохранением данных
   * @param terminal      интерфейс для вывода результатов команд
   * @throws IllegalArgumentException если любой из параметров равен null
   */
  public void init(Terminal terminal, ConnectionHandler chandler) {
    Objects.requireNonNull(terminal, "Терминал не может быть null");
    Objects.requireNonNull(chandler, "Обработчик соединений не может быть null");
    // registerCommands(terminal);
    registerClientOnlyCommands(terminal);
    registerClientServerCommands(terminal, chandler);
  }

  /**
   * Регистрирует новую команду.
   * 
   * @param name    название команды
   * @param command реализация команды
   * @throws IllegalArgumentException если имя команды или реализация равны null
   */
  public void addCommand(String name, Command command) {
    Objects.requireNonNull(name, "Имя команды не может быть null");
    Objects.requireNonNull(command, "Реализация команды " + name + " не может быть null");
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
   * @param args        аргументы команды
   * @throws CommandException если команда не найдена или возникла ошибка при
   *                          выполнении
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
    // return new LinkedList<>(commandHistory);
    return commandHistory;
  }

  private void registerClientOnlyCommands(Terminal terminal) {
    addCommand("help", new Help(terminal, this));
    addCommand("history", new History(terminal, commandHistory));
  }

  private void registerClientServerCommands(Terminal terminal, ConnectionHandler chandler) {
    addCommand("exit", new Exit(terminal, chandler));
    addCommand("clear", new Clear(terminal, chandler));
    addCommand("count_by_venue", new CountByVenue(terminal, chandler));
    addCommand("filter_starts_with_name", new FilterStartsWithNameName(terminal, chandler));
    addCommand("info", new Info(terminal, chandler));
    addCommand("insert", new Insert(terminal, chandler));
    addCommand("remove_greater", new RemoveGreater(terminal, chandler));
    addCommand("remove_greater_key", new RemoveGreaterKey(terminal, chandler));
    addCommand("show", new Show(terminal, chandler));
    addCommand("show_table", new ShowTable(terminal, chandler));
    addCommand("update", new UpdateId(terminal, chandler));
    addCommand("execute_script", new ExecuteScriptFileName(terminal, this));
  }
}
