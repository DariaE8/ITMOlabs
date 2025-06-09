package utils;

import java.io.Serializable;

/**
 * Класс, представляющий запрос от клиента на выполнение команды.
 */
public class CommandRequest implements Serializable {
    private final String commandName;
    private final String[] arguments;

    public CommandRequest(String commandName, String[] arguments) {
        this.commandName = commandName;
        this.arguments = arguments;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgument() {
        return arguments;
    }
}

