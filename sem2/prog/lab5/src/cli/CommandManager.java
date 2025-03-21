package cli;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import models.Command;
import cli.commands.*;

public class CommandManager{
    private final Queue<String> commandHistory = new LinkedList<>();
    private static final int HISTORY_SIZE = 5;
    private final Map<String, Command> commands = new HashMap<>();
    private final CLI cli;

    public CommandManager(CLI cli) {
        this.cli = cli;
        init();
    }

    public void addCommand(String name, Command command) {
        this.commands.put(name, command);
    }

    private void init () {
        addCommand("help", new Help());
        addCommand("info", new Info());
        addCommand("show", new Show());
        addCommand("сount_by_venue", new CountByVenue());
        addCommand("insert", new InsertNull());
        addCommand("update", new UpdateId());
        addCommand("remove_greater", new RemoveGreater());
        addCommand("remove_by_key", new RemoveGreaterKeyNull());
        addCommand("clear", new Clear());
        addCommand("history", new History(cli.getTerminal(), getHistory()));
        addCommand("save", new Save());
        addCommand("execute_script", new ExecuteScriptFileName(cli));
        addCommand("exit", new Exit(cli, cli.getTerminal()));
        addCommand("remove_greater", new RemoveGreaterKeyNull());
        addCommand("filter_by_type", new FilterStartsWithNameName());
        addCommand("print_unique_color", new PrintAscending());

    }
    

    public void run(String command) {
    Command cmd = commands.get(command);
    if (cmd != null) {
        cmd.run(new String[]{}); // Пустой массив, если команда не требует аргументов
    } else {
        System.out.println("Неизвестная команда: " + command);
    }
}

    
    private void addToHistory(String command) {
        if (commandHistory.size() >= HISTORY_SIZE) {
            commandHistory.poll(); // Удаляем самую старую команду
        }
        commandHistory.offer(command);
    }


    public Queue<String> getHistory() {
        return commandHistory;
    }
    
    

    
}
