package cli.commands;
import models.Command;
import cli.CLI;
import cli.Terminal;

public class Exit extends Command {
    public Exit(CLI cli, Terminal terminal) {
        super("exit", "завершить программу (без сохранения в файл)");
    }

    @Override
    public void run(String[] args) {
        System.exit(0);
    }    
}
