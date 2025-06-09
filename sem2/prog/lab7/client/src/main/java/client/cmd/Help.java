package client.cmd;

import utils.Command;
import client.CommandManager;
import utils.Terminal;
import java.util.Objects;

/**
 * Команда для вывода справки по доступным командам.
 * Показывает список всех команд с их описаниями.
 */
public class Help extends Command {
    private final CommandManager commandManager;
    private final Terminal terminal;

    /**
     * Конструктор команды помощи.
     *
     * @param terminal терминал для вывода информации
     * @param commandManager менеджер команд для получения списка команд
     * @throws NullPointerException если terminal или commandManager равен null
     */
    public Help(Terminal terminal, CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.commandManager = Objects.requireNonNull(commandManager, "Менеджер команд не может быть null");
        this.terminal = Objects.requireNonNull(terminal, "Терминал не может быть null");
    }

    /**
     * Выводит список всех доступных команд с их описаниями.
     *
     * @param args аргументы команды (игнорируются)
     */
    @Override
    public void execute(String[] args){
        commandManager.getCommands().forEach(cmd -> 
            terminal.println(cmd.getName() + " - " + cmd.getDescription())
        );
    }
}