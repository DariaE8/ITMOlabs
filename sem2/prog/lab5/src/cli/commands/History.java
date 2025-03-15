package cli.commands;

import models.Command;
import cli.Terminal;

import java.util.Queue;

public class History extends Command {
    private final Queue<String> commandHistory;
    private static final int HISTORY_SIZE = 5;
    private final Terminal terminal;

    public History(Terminal terminal, Queue<String> commandHistory) {
        super("history", "вывести последние 5 команд");
        this.terminal = terminal; // Сохраняем терминал в поле
        this.commandHistory = commandHistory;
    }

            // Print the command history
     @Override
    public void run(String[] args) {
        terminal.println("Последние " + HISTORY_SIZE + " команд:");
        for (String cmd : commandHistory) {
            terminal.println(cmd);
         }
      }
 }
    