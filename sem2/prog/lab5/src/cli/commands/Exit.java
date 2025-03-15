package cli.commands;
import models.Command;
import cli.CLI;
import cli.Terminal;

public class Exit extends Command {
    public Exit(CLI cli, Terminal terminal) {
        super("exit", "завершить программу (без сохранения в файл)");
    }

    public void run(String[] args) {
        tm.clear();
    }
}
